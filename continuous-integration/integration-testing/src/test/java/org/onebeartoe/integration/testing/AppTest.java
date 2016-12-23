
package org.onebeartoe.integration.testing;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import net.anthavio.phanbedder.Phanbedder;

/**
 * Unit test for simple App that demonstrates the Phanbedder:
 * 
 * 		http://blog.anthavio.net/2014/04/phantomjs-embedder-for-selenium.html
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	File phantomjs = Phanbedder.unpack(); //Phanbedder to the rescue!
    	DesiredCapabilities dcaps = new DesiredCapabilities();
    	dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, phantomjs.getAbsolutePath());
    	PhantomJSDriver driver = new PhantomJSDriver(dcaps);
    	 
    	//Usual Selenium stuff follows
    	try {
    	 driver.get("https://www.google.com");
    	 WebElement query = driver.findElement(By.name("q"));
    	 query.sendKeys("Phanbedder");
    	 query.submit();
    	 assertTrue( driver.getTitle().contains("Phanbedder") );
//    	 Assertions.assertThat(driver.getTitle()).contains("Phanbedder");
    	} finally {
    	 driver.quit();
    	}
    }
}
