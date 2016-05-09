
package org.onebeartoe.continuous.integration.extream.notifications;

import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
public class JenkinsRssFeed 
{    
    private List<JenkinsJob> jobs;

    public List<JenkinsJob> getJobs() 
    {
        return jobs;
    }

    public void setJobs(List<JenkinsJob> jobs) 
    {
        this.jobs = jobs;
    }
}