
package com.onebeartoe.development.tools.longs.to.date;

import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class JavaLongToDateUowSpecification
{
    private JavaLongToDateUow unitsOfWork;

    public JavaLongToDateUowSpecification()
    {
        unitsOfWork = new JavaLongToDateUow();
    }
    
    @Test(groups = {"web-automation"})
    public void millisecondsToDate()
    {
        long milliseconds = 8;
        
        JavaLongToDatePage resultPage = unitsOfWork.convert(milliseconds);
        
        String convertedDate = resultPage.getConvertedDate();
        
        assert( convertedDate.contains("1969") );
    }
    
}
