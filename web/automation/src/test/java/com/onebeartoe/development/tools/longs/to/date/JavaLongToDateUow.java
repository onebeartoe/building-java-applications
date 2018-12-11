/*
 */
package com.onebeartoe.development.tools.longs.to.date;

import org.onebeartoe.web.automation.UnitsOfWork;

/**
 *
 * @author Roberto Marquez
 */
public class JavaLongToDateUow extends UnitsOfWork
{
    private JavaLongToDatePage page;
    
    public JavaLongToDateUow()
    {
        page = new JavaLongToDatePage();
    }
    
    public JavaLongToDatePage convert(long milliseconds)
    {
        page.setMilliseconds(milliseconds);
        
        JavaLongToDatePage resultsPage = page.submitMilliseconds();
        
        return resultsPage;
    }
}
