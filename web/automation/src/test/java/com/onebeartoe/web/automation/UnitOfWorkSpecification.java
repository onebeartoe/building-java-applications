
package com.onebeartoe.web.automation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;

/**
 * @author Roberto Marquez
 */
public class UnitOfWorkSpecification
{
    protected RemoteWebDriver driver;

    protected Logger logger;

    protected String testUrl;
    
    protected Properties properites;
  
    @BeforeClass
    public void initializeClass() throws IOException, Exception
    {
        logger = Logger.getLogger( getClass().getName() );
        

        String driverPath = "/opt/google/chromedriver/chromedriver-2.37/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);

        properites = new Properties();
        String classpathInpath = "/unit-of-work.properties";
        InputStream inStream = getClass().getResourceAsStream(classpathInpath);
        properites.load(inStream);
        
        testUrl = properites.getProperty("test.url");
        
        if(testUrl == null)
        {
            throw new Exception("the test url is null.");
        }
        
        driver = new ChromeDriver();
    }
    
    @AfterTest
    protected void tearDown()
    {
        driver.close();
    }
}
