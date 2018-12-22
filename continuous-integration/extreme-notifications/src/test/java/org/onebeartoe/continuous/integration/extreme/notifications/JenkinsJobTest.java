
//TODO: Convert this from a unit test to an integration test.
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.testng.annotations.Test;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.Parameterized;


/**
 * @author Roberto Marquez <https://www.youtube.com/user/onebeartoe>
 */
//@RunWith(Parameterized.class)
public class JenkinsJobTest 
{
    private JenkinsJob implementation;
    
    public JenkinsJobTest()
    {
        this.implementation = new JenkinsJob();
    }
    
//    @Parameterized.Parameters(name="{index} - {0}")
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
        implementation.setJobStatus("none");
    }

    @Test
    public void getJobStatus()
    {
        JenkinsJobStatus jobStatus = implementation.getJobStatus();
        
        assert(jobStatus != JenkinsJobStatus.UNKOWN);
    }
}
