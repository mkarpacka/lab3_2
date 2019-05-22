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

import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {
        ConfigurationLoader.class,
        NewsReaderFactory.class,
})
public class NewsLoaderTest {

    private ConfigurationLoader mockConfigurationLoader;
    private Configuration config;
    private IncomingNews incomingNews;
    private NewsReader newsReader;
    private NewsLoader newsLoader;
    private PublishableNews publishableNews;
    private NewsReaderFactory newsReaderFactory;

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

        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(Mockito.any())).thenReturn(newsReader);
    }

    @Test
    public void testLoadConfigurationShouldBeCalledOnce(){

        publishableNews = newsLoader.loadNews();
        verify(mockConfigurationLoader, times(1)).loadConfiguration();
    }

}
