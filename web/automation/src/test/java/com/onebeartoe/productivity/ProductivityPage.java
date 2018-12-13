
package com.onebeartoe.productivity;

import com.onebeartoe.productivity.income.calculator.IncomeCalculatorPage;
import org.onebeartoe.web.automation.WebAutomationPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Roberto Marquez
 */
public class ProductivityPage extends WebAutomationPage
{
    
    public ProductivityPage(RemoteWebDriver driver)
    {
        super(driver);
    }
    
    public IncomeCalculatorPage clickIncomeCalculatorNavigationLink()
    {
        WebElement link = driver.findElementById("incomeCalculatorLink");
        
        link.click();

//TODO: Verify that 'this' should not be returned and that this class should be IncomeCalculatorPage.        
        return new IncomeCalculatorPage(driver);
    }
}
