
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.onebeartoe.io.TextFileReader;
import org.onebeartoe.io.buffered.BufferedTextFileReader;

/**
 * @author Roberto Marquez
 */
public class JenkinsRssPollerService
{
    
    
    /**
     * This method is only called once per application run.
     * It loads a configuration file with the LED strip to RSS feed mapping, 
     * as well as the mapping per strip of the Jenkins job to strip index mapping.
     * 
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void loadConfiguration(JenkinsRssRunProfile runProfile, 
                                  Map<Integer, LedStatusIndicatorStrip> knownIndicatorStrips)
                    throws FileNotFoundException, IOException
    {
        TextFileReader textFileReader = new BufferedTextFileReader();
        
        String configPath = runProfile.getJobMappingPath();
        
        File infile = new File(configPath);
        InputStream instream = new FileInputStream(infile);
        List<String> lines = textFileReader.readLines(instream);
        
        lines.stream()
            .filter( s -> !s.trim().startsWith("#") )
            .filter( s -> s.trim().length() != 0 )
            .forEach(c -> 
            {
                String[] split = c.split(":");

                int ledStrip = Integer.valueOf(split[0]);
                int ledIndex = Integer.valueOf(split[1]);
                String jobName = split[2];

                JenkinsJob jj = new JenkinsJob();
                jj.setJobName(jobName);
                jj.setNeopixelIndex(ledIndex);
                
                LedStatusIndicatorStrip lsis = knownIndicatorStrips.get(ledStrip);
            
                if(lsis == null)
                {
                    lsis = new LedStatusIndicatorStrip();
                    knownIndicatorStrips.put(ledStrip, lsis);
                }

                lsis.jobs.add(jj);
            });
    }
}
