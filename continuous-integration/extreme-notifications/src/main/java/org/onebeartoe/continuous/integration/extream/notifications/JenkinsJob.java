
package org.onebeartoe.continuous.integration.extream.notifications;

import java.util.Date;

/**
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

    private void checkRegularExpressionStatuses(String jobStatusLabel)
    {
        if( jobStatusLabel.matches("broken since build #\\d+") )
        {
            jobStatus = JenkinsJobStatus.FAILING;
        }
        
        if(jobStatus == null)
        {
            String errorMessage = "An unknown status was encountered: " + jobStatusLabel;
            System.err.println(errorMessage);

            jobStatus = JenkinsJobStatus.UNKOWN;
        }        
    }
    
    private void checkStatusNameVariants(String enumName)
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
            // Use no default case, since the status that need regular expressions
            // need checking.
        }        
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
    
    public void setJobStatus(String jobStatusLabel)
    {
        String enumName = jobStatusLabel.toUpperCase();
        enumName = enumName.replaceAll(" ", "_");
        
        try
        {
            jobStatus = JenkinsJobStatus.valueOf(enumName);
        }
        catch(IllegalArgumentException e)
        {
            // ignore error and check for status name variants
        }
        
        if(jobStatus == null)
        {
            checkStatusNameVariants(enumName);
        }
        
        if(jobStatus == null)
        {
            checkRegularExpressionStatuses(jobStatusLabel);
        }
    }

//    public void setJobStatus(JenkinsJobStatus jobStatus) 
//    {
//        this.jobStatus = jobStatus;
//    }
    
    @Override
    public String toString()
    {
        return jobStatus + " - >" + jobStatusDescription + "< for job: " + jobName;
    }
}