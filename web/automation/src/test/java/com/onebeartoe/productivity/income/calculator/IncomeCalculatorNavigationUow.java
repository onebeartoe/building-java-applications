
package com.onebeartoe.productivity.income.calculator;

import com.onebeartoe.productivity.income.calculator.rate.to.salary.RateToSalaryPage;
import com.onebeartoe.productivity.income.calculator.salary.to.rate.SalaryToRatePage;
import org.onebeartoe.web.automation.UnitsOfWork;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Roberto Marquez
 */
public class IncomeCalculatorNavigationUow extends UnitsOfWork
{
    IncomeCalculatorPage page;
    
    public IncomeCalculatorNavigationUow(RemoteWebDriver driver)
    {
        super(driver);
        
        page = new IncomeCalculatorPage(driver);
    }
    
    public SalaryToRatePage navigateToSalaryToRate()
    {
        SalaryToRatePage strPage = page.clickSalaryToRateNavigationLink();

        return strPage;
    }
    
    public RateToSalaryPage navigateToRateToSalary()
    {
        RateToSalaryPage rtsPage = page.clickRateToSalaryNavigationLink();
        
        return rtsPage;
    }
}
