
package org.onebeartoe.web.automation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;

/**
 * A UnitOfWorkSpecification is the Web automation test that invokes operations 
 * on a UnitsOfWork instance.
 * 
 * @author Roberto Marquez
 */
public class UnitOfWorkSpecification
{
    protected RemoteWebDriver driver;

    protected Logger logger;

    protected String testUrl;
    
    protected Properties properties;
    
    private WebDriverService webdriverService;
    
    private String currentTest = "test-name-not-set";

    @BeforeMethod(alwaysRun = true)
    public void testName(Method method)
    {
        currentTest = method.getName();
        
        System.out.println("test name set as: " + currentTest);
    }

    public UnitOfWorkSpecification() throws IOException, Exception
    {
        logger = Logger.getLogger( getClass().getName() );

        properties = new Properties();
        String classpathInpath = "/unit-of-work.properties";
        InputStream inStream = getClass().getResourceAsStream(classpathInpath);
        properties.load(inStream);
        
        testUrl = properties.getProperty("test.url");
        
        if(testUrl == null)
        {
            throw new Exception("the test url is null.");
        }
        
        // Append any subpath that inheriting classes override.
        testUrl += subpath();

        String s = properties.getProperty("webdriver.type");
        String name = s.toUpperCase();
        WebDriverType type = WebDriverType.valueOf(name);

        String driverPathKey = properties.getProperty("webdriver.pathKey");
        
        String driverPath = properties.getProperty("webdriver.path");

        String b = properties.getProperty("webdriver.headless");
        boolean headless = Boolean.valueOf(b);
        
        TestProfile testProfile = new TestProfile();
        testProfile.setType(type);
        testProfile.setDriverPathKey(driverPathKey);
        testProfile.setDriverPath(driverPath);
        testProfile.setHeadless(headless);
        
        webdriverService = new WebDriverService();
                
        driver = webdriverService.load(testProfile);

        logger.info("getting: " + testUrl);
        driver.get(testUrl);
    }

    private String sanitizeFilename(String dirty)
    {
            String clean = dirty.replace(":", "");
            clean = clean.replace("[", "");
            clean = clean.replace("]",  "");

            return clean;
    }
    
    /**
     * Override this method to provide a subpath to the Web appliation 
     * under test (AUT).
     * 
     * @return 
     */
    protected String subpath()
    {
        return "";
    }
    
    public void takeScreenshot(String screenshotName) throws ScreenshotException, IOException
    {
        // apparently this is how you take a screen shot in Selenium (cast to TakesScreenshot)
        TakesScreenshot pjsd = ( (TakesScreenshot) driver );
        Object o = pjsd.getScreenshotAs(OutputType.BASE64);

        byte [] bytes = o.toString().getBytes();

        byte [] outdata = Base64.decodeBase64(bytes);

        String testClass = getClass().getSimpleName();
        
        String outpath = "./target/screenshots/";
        File outdir = new File(outpath);
        outdir.mkdirs();

        //TODO: Change to use the logger object
        String outname = testClass + 
                        "-" +
                        currentTest +
                        "-" + 
                        screenshotName + 
                        "-" + 
                        "screenshot.png";

        outname = sanitizeFilename(outname);

        File outfile = new File (outdir, outname);
        FileOutputStream fos = new FileOutputStream(outfile);
        fos.write(outdata);
        fos.flush();
        fos.close();
    }    
    
    // this is done after each test method
    @AfterMethod(alwaysRun = true)
    protected void endOfTestScreenShot() throws ScreenshotException, IOException
    {
        logger.info("end of test screenshot");
        
    	String screenshotName = "tearDown";

        takeScreenshot(screenshotName);
    }
    
    @AfterTest
    protected void tearDown()
    {
        driver.close();
    }
}
