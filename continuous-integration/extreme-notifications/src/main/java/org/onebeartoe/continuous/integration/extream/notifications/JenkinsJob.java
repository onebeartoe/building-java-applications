
package org.onebeartoe.continuous.integration.extream.notifications;

import java.util.Date;

/**
 *
 * @author Roberto Marquez
 */
public class JenkinsJob 
{
    private String jobName;
    
    private Date jobRunDate;
    
    private int jobNumber;
    
    private JenkinsJobStatus jobStatus;
    
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Date getJobRunDate() {
        return jobRunDate;
    }

    public void setJobRunDate(Date jobRunDate) {
        this.jobRunDate = jobRunDate;
    }

    public int getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(int jobNumber) 
    {
        this.jobNumber = jobNumber;
    }

    public JenkinsJobStatus getJobStatus() 
    {
        return jobStatus;
    }
    
    public void setJobStatus(String jobStatus)
    {
        String enumName = jobStatus.toUpperCase();
        
        try
        {
            this.jobStatus = JenkinsJobStatus.valueOf(enumName);
        }
        catch(IllegalArgumentException e)
        {
            System.err.println("An error occured while loading status enum for: " + enumName);
        }
        
        if(this.jobStatus == null)
        {
            this.jobStatus = JenkinsJobStatus.UNKOWN;
        }
    }

    public void setJobStatus(JenkinsJobStatus jobStatus) 
    {
        this.jobStatus = jobStatus;
    }
    
    public static JenkinsJob fromRssTitle(String rssTitle)
    {
        JenkinsJob job = new JenkinsJob();
        
        int hashIndex = rssTitle.indexOf('#');        
        String name = rssTitle.substring(0, hashIndex).trim();        
        job.setJobName(name);
        
        int openParenIndex = rssTitle.indexOf('(');
        int startIndex = openParenIndex + 1;
        int closeParenIndex = rssTitle.lastIndexOf(')');
        String status = rssTitle.substring(startIndex, closeParenIndex);
        job.setJobStatus(status);
        
        return job;
    }
}
