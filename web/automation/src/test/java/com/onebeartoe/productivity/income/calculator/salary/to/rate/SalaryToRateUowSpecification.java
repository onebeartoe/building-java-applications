
package com.onebeartoe.productivity.income.calculator.salary.to.rate;

import java.io.IOException;
import org.onebeartoe.web.automation.UnitOfWorkSpecification;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class SalaryToRateUowSpecification extends UnitOfWorkSpecification
{
    public SalaryToRateUowSpecification() throws IOException, Exception
    {
        
    }
    
   @Test(groups = {"web-automation"})
   public void defaults()
   {
        logger.info(" testing the printing remove");
       
//        driver.get(testUrl);

        WebElement bodyElement = driver.findElementByTagName("body");

        String text = bodyElement.getText();
        
        String target1 = "Consider earning $55000 annually.  Having worked 52 weeks";        

        assert( text.contains(target1) );
        
        String target2 = "$26.44230842590332 an hour";
        
        assert( text.contains(target2) );
   }
   
   @Override
   public String subpath()
   {
       return "productivity/income-calculator/salary-to-rate/";
   }
}
