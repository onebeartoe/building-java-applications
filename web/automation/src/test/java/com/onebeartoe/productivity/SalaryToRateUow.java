//TODO: Implement java-long-to-date

package com.onebeartoe.productivity;

import com.onebeartoe.web.automation.UnitOfWork;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class SalaryToRateUow extends UnitOfWork
{
    @Test(groups = {"web-automation"})
   public void defaults()
   {
        logger.info(" testing the print");
       
        driver.get(testUrl);

        String target1 = "Consider earning $55000 annually.  Having worked 52 weeks";

        WebElement bodyElement = driver.findElementByTagName("body");

        String text = bodyElement.getText();
        
        assert( text.contains(target1) );
        
        String target2 = "$26.44230842590332 an hour";
        
        assert( text.contains(target2) );
   }
}

//TODO: Implement java-long-to-date
