
package com.onebeartoe.productivity.income.calculator;

import com.onebeartoe.productivity.income.calculator.rate.to.salary.RateToSalaryPage;
import com.onebeartoe.productivity.income.calculator.salary.to.rate.SalaryToRatePage;
import java.io.IOException;
import org.onebeartoe.web.automation.UnitOfWorkSpecification;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class IncomeCalculatorNavigationUowSpecifiation extends UnitOfWorkSpecification
{
    private IncomeCalculatorNavigationUow unitsOfWork;

    public IncomeCalculatorNavigationUowSpecifiation() throws IOException, Exception
    {
        unitsOfWork = new IncomeCalculatorNavigationUow(driver);
    }
    
    @Test(groups = {"web-automation"})    
    public void navigateAllPages()
    {
        SalaryToRatePage strPage = unitsOfWork.navigateToSalaryToRate();
        
        RateToSalaryPage rtsp = strPage.clickRateToSalaryNavigationLink();
        
        IncomeCalculatorPage icp = rtsp.clickIncomeCalculatorNavigationLink();
        
        String expectedTitle = "Income Calculators";
        assert( icp.getTitle().equals(expectedTitle) );
    }
    
    @Override
    public String subpath()
    {
        return "productivity/income-calculator/";
    }    
}
