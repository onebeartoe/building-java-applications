
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
    
    private String jobStatusDescription;
    
    private int neopixelIndex;

    public String getJobStatusDescription() 
    {
        return jobStatusDescription;
    }

    public void setJobStatusDescription(String jobStatusDescription) 
    {
        this.jobStatusDescription = jobStatusDescription;
    }    
    
    public int getNeopixelIndex() 
    {
        return neopixelIndex;
    }

    public void setNeopixelIndex(int neopixelIndex) 
    {
        this.neopixelIndex = neopixelIndex;
    }
    
    public String getJobName() 
    {
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
        enumName = enumName.replaceAll(" ", "_");
        
        try
        {
            this.jobStatus = JenkinsJobStatus.valueOf(enumName);
        }
        catch(IllegalArgumentException e)
        {
            // ignore error and check for status name variants
        }
        
        if(this.jobStatus == null)
        {
            switch(enumName)
            {
                case "BROKEN_FOR_A_LONG_TIME":
                {
                    // fall through to the FAILING case
                }
                case "BROKEN_SINCE_THIS_BUILD":
                {
                    this.jobStatus = JenkinsJobStatus.FAILING;
                    
                    break;
                }
                case "BACK_TO_NORMAL":
                {
                    this.jobStatus = JenkinsJobStatus.STABLE;
                    
                    break;
                }
                case "1_TEST_IS_STILL_FAILING":
                {
                    this.jobStatus = JenkinsJobStatus.UNSTABLE;
                    
                    break;
                }
                default:
                {
                    String errorMessage = "An unknown status was encountered: " + jobStatus;
                    System.err.println(errorMessage);
                    
                    this.jobStatus = JenkinsJobStatus.UNKOWN;
                }
            }
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
        job.setJobStatusDescription(status);
        job.setJobStatus(status);
        
        return job;
    }
}