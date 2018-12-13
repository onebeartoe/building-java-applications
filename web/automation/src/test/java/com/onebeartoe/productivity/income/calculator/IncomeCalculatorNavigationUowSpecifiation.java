
package com.onebeartoe.productivity.income.calculator;

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
        unitsOfWork = new IncomeCalculatorNavigationUow();
    }
    
    @Test(groups = {"web-automation"})    
    public void navigateAllPages()
    {
        SalaryToRatePage strPage = unitsOfWork.navigateToSalaryToRate();
        
        
    }
    
    @Override
    public String subpath()
    {
        return "productivity/income-calculator/";
    }    
}
