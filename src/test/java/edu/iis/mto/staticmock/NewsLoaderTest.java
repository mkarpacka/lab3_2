package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {
    @Mock
    private NewsLoader newsLoader;
    @Mock
    private ConfigurationLoader configurationLoader;
    @Mock
    private NewsReaderFactory newsReaderFactory;
    @Mock
    private NewsReader newsReader;

    private IncomingNews incomingNews;
    private PublishableNews publishableNews;

    @Before
    public void init(){
        newsLoader = new NewsLoader();
        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("A",SubsciptionType.A));
        incomingNews.add(new IncomingInfo("B",SubsciptionType.B));
        incomingNews.add(new IncomingInfo("C",SubsciptionType.C));
        incomingNews.add(new IncomingInfo("NONE",SubsciptionType.NONE));

        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);

        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(new Configuration());

        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(Mockito.any())).thenReturn(newsReader);
    }

    @Test
    public void publishableNewsShouldSeparateNews(){
        publishableNews = newsLoader.loadNews();

        List<String> publish = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");
        Assert.assertThat(publish.size(), is(equalTo(1)));
        Assert.assertThat(subscribed.size(), is(equalTo(3)));
    }

    @Test
    public void publishableNewsShouldProperInfoContent(){
        publishableNews = newsLoader.loadNews();

        List<String> publish = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");

        Assert.assertThat(publish.get(0), is(equalTo("NONE")));
        Assert.assertThat(subscribed.get(0), is(equalTo("A")));
        Assert.assertThat(subscribed.get(1), is(equalTo("B")));
        Assert.assertThat(subscribed.get(2), is(equalTo("C")));
    }
}
