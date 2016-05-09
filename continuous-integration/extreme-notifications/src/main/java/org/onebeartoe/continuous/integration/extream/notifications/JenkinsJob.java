
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

    public void setJobNumber(int jobNumber) {
        this.jobNumber = jobNumber;
    }

    public JenkinsJobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JenkinsJobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }
}
