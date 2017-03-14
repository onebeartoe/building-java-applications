
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.onebeartoe.continuous.integration.extream.notifications.HttpJenkinsRssFeedService;
import org.onebeartoe.continuous.integration.extream.notifications.JenkinsJob;
import org.onebeartoe.continuous.integration.extream.notifications.JenkinsJobStatus;
import org.onebeartoe.continuous.integration.extream.notifications.JenkinsRssFeedService;

//todo: Correct the spelling
/**
 * @author Roberto Marquez <https://www.youtube.com/user/onebeartoe>
 */
@RunWith(Parameterized.class)
public class JenkisJobTest 
{
    private JenkinsJob jj;
    
    public JenkisJobTest(JenkinsJob jj)
    {
        this.jj = jj;
    }
    
    @Parameterized.Parameters(name="{index} - {0}")
    public static Collection<Object[]> dataSet() throws MalformedURLException, FeedException, IOException
    {                
        String u = "https://onebeartoe.ci.cloudbees.com/rssLatest";
        
        URL rssUrl = new URL(u);
        JenkinsRssFeedService rssService = new HttpJenkinsRssFeedService();
        List jobs = rssService.getJobs(rssUrl);
        
        List dataSet = new ArrayList();
     
        jobs.forEach( j -> 
        {
            Object [] oa = {j};
            dataSet.add(oa);
        });
        
        return dataSet;
    }

    @Test
    public void setJobStatus()
    {
        JenkinsJobStatus jobStatus = jj.getJobStatus();
        
        assert(jobStatus != JenkinsJobStatus.UNKOWN);
    }
}
