
package org.onebeartoe.web.automation;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * @author Roberto Marquez
 */
public class WebAutomationPage
{
   protected RemoteWebDriver driver;
   
   public WebAutomationPage(RemoteWebDriver driver)
   {
       this.driver = driver;
   }
}
