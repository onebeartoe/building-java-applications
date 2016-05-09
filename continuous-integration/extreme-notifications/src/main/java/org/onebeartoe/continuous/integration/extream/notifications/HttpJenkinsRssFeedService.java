
package org.onebeartoe.continuous.integration.extream.notifications;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
public class HttpJenkinsRssFeedService implements JenkinsRssFeedService
{

    @Override
    public List<JenkinsJob> getJobs(URL rssUrl) 
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

            System.out.println(published);
            System.out.println(title);
            System.out.println();

            titles.add(title);
        }        
        
        return 
    }
    
}
