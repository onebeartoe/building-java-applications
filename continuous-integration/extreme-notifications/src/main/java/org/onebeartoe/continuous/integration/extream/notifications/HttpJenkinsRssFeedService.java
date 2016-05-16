
package org.onebeartoe.continuous.integration.extream.notifications;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
public class HttpJenkinsRssFeedService implements JenkinsRssFeedService
{

    @Override
    public List<JenkinsJob> getJobs(URL rssUrl) throws IOException, FeedException 
    {
        SyndFeedInput input = new SyndFeedInput();
        XmlReader xmlReader = new XmlReader(rssUrl);
        SyndFeed feed = input.build(xmlReader);

        List entries = feed.getEntries();

        List<JenkinsJob> jobs = new ArrayList();
        for(Object e : entries)
        {
            SyndEntry se = (SyndEntry) e;
            se.getAuthor();
            String title = se.getTitle();
            JenkinsJob job = JenkinsJob.fromRssTitle(title);
            Date jobRunDate = se.getPublishedDate();
            job.setJobRunDate(jobRunDate);
            
//            System.out.println(jobRunDate);
//            System.out.println(title);
//            System.out.println();

            jobs.add(job);
        }
        System.out.println();
        
        return jobs;
    }
    
}
