
package com.onebeartoe.web.automation;

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

    public UnitOfWork()
    {
        logger = Logger.getLogger( getClass().getName() );
        

        String driverPath = "/opt/google/chromedriver/chromedriver-2.37/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);

        driver = new ChromeDriver();
    }
    
    @AfterTest
    protected void tearDown()
    {
        driver.close();
    }
}
