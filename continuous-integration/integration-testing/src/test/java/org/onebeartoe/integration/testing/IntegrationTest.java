
package org.onebeartoe.integration.testing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.ScreenshotException;

public class IntegrationTest
{
    protected RemoteWebDriver driver;	
    
    protected static Logger logger;
    
    protected WebDriverService webdriverService;

    @Rule
    /**
     * This is used for adding the test name to the screenshot file name.
     */
    public TestName testName = new TestName();
    
    private static PropertiesConfiguration configuration;
    
    @After
    public void tearDown() throws ScreenshotException, IOException
    {
    	String screenshotName = "tearDown";
    	takeScreenshot(screenshotName);
    	
        driver.quit();
    }
    
	public void takeScreenshot(String screenshotName) throws ScreenshotException, IOException
	{
		// apparently this is how you take a screen shot in Selenium (cast to TakesScreenshot)
		TakesScreenshot pjsd = ( (TakesScreenshot) driver );
		Object o = pjsd.getScreenshotAs(OutputType.BASE64);

		byte [] bytes = o.toString().getBytes();

		byte [] outdata = Base64.decodeBase64(bytes);

		String testClass = getClass().getSimpleName();
		String testName = this.testName.getMethodName();

		String outpath = "./target/screenshots/";
		File outdir = new File(outpath);
		outdir.mkdirs();

                //TODO: Change to use the logger object
		String outname = testClass + "-" + testName + "-" + screenshotName + "-"
							+ "screenshot.png";

		outname = sanitizeFilename(outname);

		File outfile = new File (outdir, outname);
		FileOutputStream fos = new FileOutputStream(outfile);
		fos.write(outdata);
		fos.flush();
		fos.close();
	}   
    
    @BeforeClass
    public static void loadConfiguration() throws ConfigurationException
    {
        String loggerName = IntegrationTest.class.getSimpleName();
        logger = Logger.getLogger(loggerName);
                
        configuration = new PropertiesConfiguration("webdriver.properties");
        String s = (String) configuration.getProperty("webdriver");
    }
    
    @Before
    public void initializeTest() throws Exception
    {
        webdriverService = new WebDriverService();

        String s = (String) configuration.getProperty("webdriver");
        s = s.trim().toUpperCase();

        WebDriverType type = WebDriverType.valueOf(s);

        driver = webdriverService.load(type, configuration);

//        driver.manage().window().maximize();
    }
    
	private String sanitizeFilename(String dirty)
	{
		String clean = dirty.replace(":", "");
		clean = clean.replace("[", "");
		clean = clean.replace("]",  "");

		return clean;
	}    
}
