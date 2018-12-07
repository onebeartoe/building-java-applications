
package com.onebeartoe.web.automation;

//import com.github.paulakimenko.webdriver.service.Driver;
//import com.github.paulakimenko.webdriver.service.WDProperties;
//
//import com.github.paulakimenko.webdriver.service.WDService;
//import com.github.paulakimenko.webdriver.service.WDServiceProvider;


import java.util.logging.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

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

//    protected WDService webdriverService;

    public UnitOfWork()
    {
        logger = Logger.getLogger( getClass().getName() );
        
//        String driverPath = "/usr/bin/google-chrome";
        String driverPath = "/opt/google/chromedriver/chromedriver-2.37/chromedriver";
        System.setProperty("webdriver.chrome.driver", driverPath);
        
//        WDProperties properties = new WDProperties.Builder()
//        .driver(Driver.CHROME)
//        .implicitlyWait(5)
//        .fluentWaitTimeout(10)
//        .timeUnit(TimeUnit.SECONDS)
//        .build();
//        
//        webdriverService = WDServiceProvider.getInstance();
//        webdriverService.setProperties(properties);
//        webdriverService.init();
        
        //TODO: Wah?
//        driver = (RemoteWebDriver) webdriverService.getDriver();

            driver = new ChromeDriver();
    }
}



