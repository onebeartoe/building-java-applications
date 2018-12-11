
package com.onebeartoe.development.tools.longs.to.date;

import org.onebeartoe.web.automation.WebAutomationPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Roberto Marquez
 */
public class JavaLongToDatePage extends WebAutomationPage
{
    private final String CONVERTED_DATE_TEXT_ID = "convertedDate";
    
    private final String MILLISECONDS_FIELD_ID = "milliseconds";
    
    private final String SUBMIT_MILLIS_BUTTON_ID = "submitMillis";

    public JavaLongToDatePage(RemoteWebDriver driver)
    {
        super(driver);
    }

    public String getConvertedDate()
    {        
        WebElement element = driver.findElementById(CONVERTED_DATE_TEXT_ID);
        
        return element.getText();
    }

    public void setMilliseconds(long milliseconds)
    {
        WebElement element = driver.findElementById(MILLISECONDS_FIELD_ID);
        
        String ms = String.valueOf(milliseconds);
        
        element.sendKeys(ms);
    }

    public JavaLongToDatePage submitMilliseconds()
    {
        WebElement submitButton = driver.findElementById(SUBMIT_MILLIS_BUTTON_ID);
        
        submitButton.click();
        
        return this;
    }    
}
