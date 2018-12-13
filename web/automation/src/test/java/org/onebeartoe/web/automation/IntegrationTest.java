
package org.onebeartoe.web.automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
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

        driver = webdriverService.load(type);

//TODO: Add this to the run time configuration
        boolean maximize = false;
        if(maximize)
        {
            driver.manage().window().maximize();
        }
    }
}
