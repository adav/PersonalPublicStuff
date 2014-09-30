package Model;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import com.mongodb.*;
import org.apache.log4j.BasicConfigurator;

/**
 * User: adav
 */
public class InitialiseDatabaseServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        super.init();

        //get log4j going:
        try {
            BasicConfigurator.configure();
        } catch (Exception e) {
        }

       /* DB db = Util.getDB();

        DBCollection usersColl = db.getCollection("withinsite.users");
          */

        javapns.communication.ProxyManager.setJVMProxy("webcache", "3128");

        /*System.clearProperty("https.proxyHost");
        System.clearProperty("https.proxyPort");
        System.clearProperty("http.proxyHost");
        System.clearProperty("http.proxyPort");


        System.clearProperty("javapns.communication.proxyHost");
        System.clearProperty("javapns.communication.proxyPort");
        System.clearProperty("javapns.communication.proxyAuthorization");
         */
        System.setProperty("java.net.useSystemProxies", "true");



    }
}
