
package org.onebeartoe.web.automation;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;

public class IntegrationTest
{
    protected RemoteWebDriver driver;	
    
    protected static Logger logger;
    
    protected WebDriverService webdriverService;

    private static PropertiesConfiguration configuration;
    
    @After
    public void tearDown() throws ScreenshotException, IOException
    {
    	String screenshotName = "tearDown";
//    	takeScreenshot(screenshotName);
    	
        driver.quit();
    }
    
    @BeforeClass
    public static void loadConfiguration() throws ConfigurationException
    {
        String loggerName = IntegrationTest.class.getSimpleName();
        logger = Logger.getLogger(loggerName);
                
        configuration = new PropertiesConfiguration("webdriver.properties");
        String s = (String) configuration.getProperty("webdriver");
    }
    
    @Before
    public void initializeTest() throws Exception
    {
        webdriverService = new WebDriverService();

        String s = (String) configuration.getProperty("webdriver");
        s = s.trim().toUpperCase();

        WebDriverType type = WebDriverType.valueOf(s);

        driver = webdriverService.load(null);
    }
}
