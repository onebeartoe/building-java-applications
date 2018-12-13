//TODO: Move this to the software-development-tools project.

package org.onebeartoe.web.automation;

import java.net.MalformedURLException;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverService
{
    public RemoteWebDriver load(TestProfile testProfile) throws ConfigurationException, MalformedURLException
    {
        RemoteWebDriver driver;

        String driverPathKey = testProfile.getDriverPathKey();
        
        String driverPath = testProfile.getDriverPath();
        
        boolean headless = testProfile.isHeadless();
        
        WebDriverType type = testProfile.getType();

        switch (type) 
        {
            case CHROME:
            {
                System.setProperty(driverPathKey, driverPath);

                ChromeOptions options = new ChromeOptions();
                options.setHeadless(headless);
                
                driver = new ChromeDriver(options);

                break;
            }
            case FIREFOX:
            {
                System.setProperty(driverPathKey, driverPath);
                
                FirefoxOptions options = new FirefoxOptions();
                options.setHeadless(headless);
                
                driver = new FirefoxDriver();

                break;
            }
            default:
            {
                driver = null;
                System.err.println("the Web driver was not initialized; type: " + type);

                break;
            }
        }
        
//TODO: Add this to the run time configuration
        boolean maximize = false;
        if(maximize)
        {
            driver.manage().window().maximize();
        }        

        return driver; 
    }
}
