
package org.onebeartoe.integration.testing;

import java.net.MalformedURLException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

//TODO: Is the ready for deprecation?
//TODO: See https://github.com/paulakimenko/webdriver-service/
public class WebDriverService
{
    public RemoteWebDriver load(WebDriverType type, Configuration configggg) throws ConfigurationException, MalformedURLException
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();
//        capabilities.setc
        RemoteWebDriver driver;

        //TODO find a better way to choose the driver implementation
        switch (type) 
        {
            case CHROME:
            {
                // run tests locally in chrome
                String driverPath = "C:\\opt\\webdriver\\chromedriver.exe";
                System.setProperty("webdriver.chrome.driver", driverPath);
                driver = new ChromeDriver(capabilities);

                break;
            }
            case PHANTOM_JS:
            {
            	capabilities.setJavascriptEnabled(true);
                    driver = loadPhantomJs(capabilities);
//                    driver.

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
     * run tests in headless mode with PhantomJS
     * @param capabilities
     * @return
     */
    private RemoteWebDriver loadPhantomJs(DesiredCapabilities capabilities)
    {
    	DesiredCapabilities dcaps = new DesiredCapabilities();
    	
    	String executablePath = "C:\\opt\\webdriver\\phantomjs.exe";
    	dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, executablePath);
    	
//        File phantomjs = Phanbedder.unpack(); //Phanbedder to the rescue!
//    	dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
    	
    	PhantomJSDriver driver = new PhantomJSDriver(dcaps);

        return driver;
    }  
}
