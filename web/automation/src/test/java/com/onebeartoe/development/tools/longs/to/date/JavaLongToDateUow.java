
package com.onebeartoe.development.tools.longs.to.date;

import org.onebeartoe.web.automation.UnitsOfWork;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Roberto Marquez
 */
public class JavaLongToDateUow extends UnitsOfWork
{
    private JavaLongToDatePage page;
    
    public JavaLongToDateUow(RemoteWebDriver driver)
    {
        super(driver);
        
        page = new JavaLongToDatePage(driver);
    }
    
    public JavaLongToDatePage convert(long milliseconds)
    {
        page.setMilliseconds(milliseconds);
        
        JavaLongToDatePage resultsPage = page.submitMilliseconds();
        
        return resultsPage;
    }
}
