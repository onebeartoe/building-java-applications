
package com.onebeartoe.productivity.income.calculator;

import com.onebeartoe.productivity.ProductivityPage;
import com.onebeartoe.productivity.income.calculator.rate.to.salary.RateToSalaryPage;
import com.onebeartoe.productivity.income.calculator.salary.to.rate.SalaryToRatePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Roberto Marquez
 */
public class IncomeCalculatorPage extends ProductivityPage
{
    private final String RATE_TO_SALARYNAVIGATION_LINK_ID = "rateToSalaryLink";
    
    private final String SALARY_TO_RATE_NAVIGATION_LINK_ID = "salaryToRateLink";
    
    public IncomeCalculatorPage(RemoteWebDriver driver)
    {
        super(driver);
    }

    public SalaryToRatePage clickSalaryToRateNavigationLink()
    {
        WebElement linkElement = driver.findElementById(SALARY_TO_RATE_NAVIGATION_LINK_ID);
        
        linkElement.click();
        
        return new SalaryToRatePage(driver);
    }

    public RateToSalaryPage clickRateToSalaryNavigationLink()
    {
        WebElement link = driver.findElementById(RATE_TO_SALARYNAVIGATION_LINK_ID);
        
        link.click();
        
        return new RateToSalaryPage(driver);
    }

    Object getTitle()
    {
        return driver.getTitle();
    }
}
