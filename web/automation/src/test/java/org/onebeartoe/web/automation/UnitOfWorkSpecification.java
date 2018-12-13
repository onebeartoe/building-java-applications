
package org.onebeartoe.web.automation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterTest;

/**
 * A UnitOfWorkSpecification is the Web automation test that invokes operations 
 * on a UnitsOfWork instnace.
 * 
 * @author Roberto Marquez
 */
public class UnitOfWorkSpecification
{
    protected RemoteWebDriver driver;

    protected Logger logger;

    protected String testUrl;
    
    protected Properties properties;
    
    private WebDriverService webdriverService;
  
    public UnitOfWorkSpecification() throws IOException, Exception
    {
        logger = Logger.getLogger( getClass().getName() );

        properties = new Properties();
        String classpathInpath = "/unit-of-work.properties";
        InputStream inStream = getClass().getResourceAsStream(classpathInpath);
        properties.load(inStream);
        
        testUrl = properties.getProperty("test.url");
        
        if(testUrl == null)
        {
            throw new Exception("the test url is null.");
        }
        
        // Append any subpath that inheriting classes override.
        testUrl += subpath();

        String s = properties.getProperty("webdriver.type");
        String name = s.toUpperCase();
        WebDriverType type = WebDriverType.valueOf(name);
        
//        WebDriverType type = WebDriverType.CHROME;

        webdriverService = new WebDriverService();
        
        driver = webdriverService.load(type);

        driver.get(testUrl);
    }
    
    /**
     * Override this method to provide a subpath to the Web appliation 
     * under test (AUT).
     * 
     * @return 
     */
    protected String subpath()
    {
        return "";
    }
    
    @AfterTest
    protected void tearDown()
    {
        driver.close();
    }
}