
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.onebeartoe.continuous.integration.extream.notifications.JenkinsRssPoller;

/**
 * This test is for the RSS feeds provided by Jenkins builds.
 * 
 * @author Roberto Marquez
 */
public class JenkinsRssFeedTest
{

    public JenkinsRssFeedTest()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
    }

    @AfterClass
    public static void tearDownClass()
    {
    }

    @Before
    public void setUp()
    {
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void builds() throws MalformedURLException, IOException, DocumentException
    {
        String key = "EXTREME_NOTIFICATIONS_RSSURL";
        String rssUrl = System.getenv(key);

        rssUrl = JenkinsRssPoller.rssUrl;
        
        System.out.println(" env - rssUrl: " + rssUrl);                
        assertNotNull(rssUrl);        
        assertNotSame(rssUrl, "");
        assertFalse( rssUrl.equals("null") );
        // if we dont make it here then make sure that the 'Build Environment' section 
        // in the Jenkins job configuration has the 'Inject environment variables to the build process'
        // checkbox selected and that environments variables are correctly set in the 
        // 'Properties Content' input box.
        
        boolean ok = false;

        try 
        {
            URL feedUrl = new URL(rssUrl);

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));
            
            List entries = feed.getEntries();
            
//            List <String> titles = entries.stream()
//                   .map( p -> p.getTitle() )
//                   .collectforEach( p -> {});
            
// steams and lambda this baby
            List<String> titles = new ArrayList();
            for(Object e : entries)
            {
                SyndEntry se = (SyndEntry) e;
                se.getAuthor();
                String title = se.getTitle();
                String published = se.getPublishedDate().toString();
                List links = se.getLinks();
                
                System.out.println(published);
                System.out.println(title);
//                links.forEach(System.out::println);
                System.out.println();
                
                titles.add(title);
            }

            ok = true;
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }

        if (!ok) 
        {
            System.out.println();
            System.out.println("FeedReader reads and prints any RSS/Atom feed type.");
            System.out.println("The first parameter must be the URL of the feed to read.");
            System.out.println();
        }
        else
        {
            
        }
    }

    @Test
    public void deploys()
    {
/*        
        String key = "temp";
        String rssUrl = System.getenv(key);        
        System.out.println(" env -   PATH: " + rssUrl);        
        assertNotNull(rssUrl);
        assertNotSame(rssUrl, "");
  */      
        Map<String, String> env = System.getenv();
//        for (String envName : env.keySet()) 
        {
//            System.out.format("%s=%s%n",
//                              envName,
//                              env.get(envName));
        }
    }
}
