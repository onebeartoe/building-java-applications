
package org.onebeartoe.integration.testing;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
//import org.openqa.selenium.browserlaunchers.Sleeper;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

import java.io.IOException;
import java.util.List;

/**
 * Unit test for simple App that demonstrates the Phanbedder:
 * 
 * 		http://blog.anthavio.net/2014/04/phantomjs-embedder-for-selenium.html
 */
public class AppTest extends IntegrationTest
{
    /**
     * @return the suite of tests being tested
     * @throws InterruptedException 
     * @throws IOException 
     * @throws ScreenshotException 
     */
//    public static Test suite()
//    {
//        return new TestSuite( AppTest.class );
//    }

    @Test
    /**
     * Rigourous Test :-)
     */
    public void testApp() throws InterruptedException, ScreenshotException, IOException
    {	 
    	//Usual Selenium stuff follows 
        
        driver.get("https://www.google.com");
        
//        Thread.sleep(2000);
        
        takeScreenshot("google home");
        
        WebDriverWait wait = new WebDriverWait(driver, 10);

//		By qByName = By.id("q");
		By qByName = By.name("q"); // works for Chorme
		
		wait.until( ExpectedConditions.presenceOfElementLocated(qByName) );
		
        WebElement query = driver.findElement(qByName);
        String searchQuery = "Phanbedder";        
        query.sendKeys(searchQuery);
        query.submit();
        
		wait.until(ExpectedConditions.titleContains(searchQuery));        
        
        Thread.sleep(2000);
        
        String title = driver.getTitle();
        assertTrue( title.contains(searchQuery) );
    }
}
