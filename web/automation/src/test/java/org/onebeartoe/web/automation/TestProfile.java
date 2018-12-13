
package org.onebeartoe.web.automation;

/**
 * @author Roberto Marquez
 */
class TestProfile
{
   private WebDriverType type;

   private String driverPathKey;
   
   private String driverPath;
   
   private boolean headless;

    public WebDriverType getType()
    {
        return type;
    }

    public void setType(WebDriverType type)
    {
        this.type = type;
    }

    public boolean isHeadless()
    {
        return headless;
    }

    public void setHeadless(boolean headless)
    {
        this.headless = headless;
    }

    public String getDriverPathKey()
    {
        return driverPathKey;
    }

    public void setDriverPathKey(String driverPathKey)
    {
        this.driverPathKey = driverPathKey;
    }

    public String getDriverPath()
    {
        return driverPath;
    }

    public void setDriverPath(String driverPath)
    {
        this.driverPath = driverPath;
    }
   
   
}
