package org.onebeartoe.integration.testing.testng;

/**
 *
 * @author Roberto Marquez <https://www.youtube.com/user/onebeartoe>
 */
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;

public class TestBook 
{
    @Test
    public void testFireFox() 
    {
        WebDriver driver = new FirefoxDriver();

        driver.manage().window().maximize();

        driver.get("http://www.facebook.com");

        driver.quit();
    }
}
