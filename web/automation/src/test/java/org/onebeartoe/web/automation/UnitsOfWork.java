
package org.onebeartoe.web.automation;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * The class represents the units of work that are preformed on any given Web 
 * page.
 * 
 * The UnitsOfWork class' operations navigate between Page objects.
 * 
 * Some setup configuration was partially inspired by this blog entry:
 * 
 *      https://www.hindsightsoftware.com/blog/selenium-webdriver-java
 * 
 * @author Roberto Marquez
 */
public class UnitsOfWork
{
    protected RemoteWebDriver driver;
    
    public UnitsOfWork(RemoteWebDriver driver)
    {
        this.driver = driver;
    }
}
