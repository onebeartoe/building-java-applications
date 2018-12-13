
//TODO: Implement Date to milliseconds.    

package com.onebeartoe.development.tools.longs.to.date;

import java.io.IOException;
import org.onebeartoe.web.automation.UnitOfWorkSpecification;
import org.testng.annotations.Test;

/**
 * @author Roberto Marquez
 */
public class JavaLongToDateUowSpecification extends UnitOfWorkSpecification
{
    private final JavaLongToDateUow unitsOfWork;

    public JavaLongToDateUowSpecification() throws IOException, Exception
    {
        unitsOfWork = new JavaLongToDateUow(driver);
    }
    
    @Test(groups = {"web-automation"}, testName="milis2secs")
    public void millisecondsToDate()
    {
        long milliseconds = 8;
        
        JavaLongToDatePage resultPage = unitsOfWork.convert(milliseconds);
        
        String convertedDate = resultPage.getConvertedDate();
        
        assert( convertedDate.contains("1969") );
    }

   @Override
   public String subpath()
   {
       return "development-tools/java-long-to-date-converter/";
   }    
    
//TODO: Implement Date to milliseconds.    
}
