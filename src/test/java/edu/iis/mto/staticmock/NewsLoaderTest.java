package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {
        ConfigurationLoader.class,
        NewsReaderFactory.class
})
public class NewsLoaderTest {

    private ConfigurationLoader mockConfigurationLoader;
    private Configuration config;
    private IncomingNews incomingNews;
    private NewsReader newsReader;
    private NewsLoader newsLoader;

    @Before
    public void setUp() {
        newsLoader = new NewsLoader();
        config = new Configuration();
        incomingNews = new IncomingNews();

        incomingNews.add(new IncomingInfo("SubsciptionType A", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("SubsciptionType B", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("SubsciptionType C", SubsciptionType.C));
        incomingNews.add(new IncomingInfo("SubsciptionType NONE", SubsciptionType.NONE));

        mockStatic(ConfigurationLoader.class);
        mockConfigurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(mockConfigurationLoader);
        when(mockConfigurationLoader.loadConfiguration()).thenReturn(new Configuration());


        newsReader = () -> incomingNews;

        mockStatic(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader("reader")).thenReturn(newsReader);


    }

}
