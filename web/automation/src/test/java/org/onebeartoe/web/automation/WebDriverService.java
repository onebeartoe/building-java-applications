//TODO: Move this to the software-development-tools project.

package org.onebeartoe.web.automation;

import java.net.MalformedURLException;

import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverService
{
    public RemoteWebDriver load(WebDriverType type) throws ConfigurationException, MalformedURLException
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        RemoteWebDriver driver;

        //TODO find a better way to choose the driver implementation
        switch (type) 
        {
            case CHROME:
            {
                String driverPath = "/opt/google/chromedriver/chromedriver-2.37/chromedriver";
                System.setProperty("webdriver.chrome.driver", driverPath);
                
                ChromeOptions options = new ChromeOptions();
                options.setHeadless(true);
                
                driver = new ChromeDriver(options);

                break;
            }
            case FIREFOX:
            {
                driver = loadFirefox(capabilities);

                break;
            }
            default:
            {
                driver = null;
                System.err.println("the Web driver was not initialized; type: " + type);

                break;
            }
        }

        return driver; 
    }

    /**
     * This has the webdriver.tests through Firefox browser.
     * 
     * @return
     */
    private RemoteWebDriver loadFirefox(DesiredCapabilities capabilities)
    {
        FirefoxProfile profile = new FirefoxProfile();
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
        RemoteWebDriver driver = new FirefoxDriver(capabilities);

        return driver;
    }
}
