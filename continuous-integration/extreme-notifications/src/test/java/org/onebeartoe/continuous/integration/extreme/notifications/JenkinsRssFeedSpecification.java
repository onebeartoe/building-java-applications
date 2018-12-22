
//TODO: Convert this from a unit test to an integration test.
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

import org.dom4j.DocumentException;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNotSame;
import org.testng.annotations.Test;

/**
 * This test is for the RSS feeds provided by Jenkins builds.
 * 
 * @author Roberto Marquez
 */
public class JenkinsRssFeedSpecification
{
//TODO: Convert this from a unit test to an integration test.    
    @Test
    public void builds() throws MalformedURLException, IOException, DocumentException
    {
        String key = "EXTREME_NOTIFICATIONS_RSSURL";
        String rssUrl = System.getenv(key);

//        rssUrl = "https://onebeartoe.ci.cloudbees.com/rssLatest";
rssUrl = "https://jenkins.mono-project.com/rssLatest";
        
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
}
//TODO: Convert this from a unit test to an integration test.
