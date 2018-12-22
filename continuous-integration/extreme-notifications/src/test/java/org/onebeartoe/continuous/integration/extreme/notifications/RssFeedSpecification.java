
//TODO: Convert this from a unit test to an integration test.
package org.onebeartoe.continuous.integration.extreme.notifications;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultElement;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNotSame;
import org.testng.annotations.Test;

/**
 * This test is for CruiseControl builds.
 * 
 * @author Roberto Marquez
 */
public class RssFeedSpecification
{
    @Test
    public void builds() throws MalformedURLException, IOException, DocumentException
    {
        String key = "EXTREME_NOTIFICATIONS_RSSURL";
        String rssUrl = System.getenv(key);

rssUrl = "https://jenkins.mono-project.com/rssLatest";
//rssUrl = "http://xml.weather.yahoo.com/ns/rss/1.0";

        System.out.println(" env - rssUrl: " + rssUrl);                
        assertNotNull(rssUrl);        
        assertNotSame(rssUrl, "");
        assertFalse( rssUrl.equals("null") );

        // if we dont make it here then make sure that the 'Build Environment' section 
        // in the Jenkins job configuration has the 'Inject environment variables to the build process'
        // checkbox selected and that environments variables are correctly set in the 
        // 'Properties Content' input box.
        
        Map<String,String> uris = new HashMap<String,String>();
//            uris.put( "y", "http://xml.weather.yahoo.com/ns/rss/1.0" );

        DocumentFactory factory = new DocumentFactory();
        factory.setXPathNamespaceURIs( uris );

        SAXReader xmlReader = new SAXReader();
        xmlReader.setDocumentFactory( factory );
        
        URLConnection conn = new URL(rssUrl).openConnection();
        InputStream inputStream = conn.getInputStream();
        
        Document doc = xmlReader.read(inputStream);		
        String title = doc.valueOf("/rss/channel/title");        
        assertNotNull(title);
        System.out.println("title: " + title);
        
        System.out.println("\nitems:");
        String itemsPath = "/rss/channel/item";
        List<DefaultElement> selectNodes = doc.selectNodes(itemsPath);
        for(DefaultElement e : selectNodes)
        {
            System.out.println("\ne: " + e.toString() );
            
            String descriptionPath = "description";
            Object o = e.selectObject(descriptionPath);
            System.out.println("o: " + o.toString() );
            
            Node node = e.selectSingleNode(descriptionPath);
            String n = node.getText();
            System.out.println("n: " + n.toString() );
        }
        
        inputStream.close();
    }
}
