
package org.onebeartoe.continuous.integration.extreme.notifications;

import com.sun.syndication.io.FeedException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author Roberto Marquez
 */
public interface JenkinsRssFeedService
{
    List<JenkinsJob> getJobs(URL rssUrl) throws FeedException, IOException;
}
