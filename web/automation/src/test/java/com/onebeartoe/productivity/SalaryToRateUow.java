//TODO: Implement java-long-to-date

package com.onebeartoe.productivity;

import com.onebeartoe.web.automation.UnitOfWork;
import org.openqa.selenium.By;
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
//        driver.get("http://www.onebeartoe.com/productivity/income-calculator/salary-to-rate/");

//        driver.findElement(By.id("id")).click();
   }
}

//TODO: Implement java-long-to-date
