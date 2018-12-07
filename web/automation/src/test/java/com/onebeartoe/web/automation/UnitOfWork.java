
package com.onebeartoe.web.automation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;

/**
 * The class represents the units of work that are preformed on any given Web 
 * page.
 * 
 * Some setup configuration was inspired by this blog entry:
 * 
 *      https://www.hindsightsoftware.com/blog/selenium-webdriver-java
 * 
 * @author Roberto Marquez
 */
public class UnitOfWork
{
    protected RemoteWebDriver driver;

    protected Logger logger;

    protected String testUrl;
    
    protected Properties properites;
    
    public UnitOfWork() throws IOException, Exception
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