
package org.onebeartoe.continuous.integration.extream.notifications;

import java.net.URL;
import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
public interface JenkinsRssFeedService
{
    List<JenkinsJob> getJobs(URL rssUrl);
}
