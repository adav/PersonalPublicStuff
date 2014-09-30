package Model.API.Requests.Users;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import Model.RecommendationModel.CustomTypes.SupportedDevices;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import com.mongodb.*;

/**
 * User: adav
 */
public class RegisterUserServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");

        try {
            DB db = Model.Util.getDB();
            DBCollection userColl = db.getCollection("withinsite.users");

//            BasicDBObject user = new BasicDBObject()
//            .append("email", request.getParameter("email"))
//            .append("fb_access_token", request.getParameter("fb_access_token"))
//            .append("push_token", request.getParameter("push_token"))
//            .append("device_registered", request.getParameter("device_registered"));


            DBCursor dbCursor = userColl.find(new BasicDBObject("email", request.getParameter("email")));

            if(dbCursor.count() > 0){
                dbCursor.close();
                throw new Exception("User already exists with email provided.");
            }

            // save it into collection named "yourCollection"
            //userColl.insert(user);

            userColl.insert(new WithinSiteUser(request.getParameter("email"),
                    request.getParameter("fb_access_token"),
                    request.getParameter("push_token"),
                    SupportedDevices.valueOf(request.getParameter("device_registered"))));

            dbCursor = userColl.find(new BasicDBObject("email", request.getParameter("email")));

            //InputStream certStream = getClass().getClassLoader().getResourceAsStream("/Model/API/ASyncResponses/PushNotifications/ApplePushNotification/pushsandbox.p12");

            //Push.alert("Welcome to WithinSite. Your device is now registered.", certStream, "withinsite", false, request.getParameter("push_token"));

            try {
                while(dbCursor.hasNext()) {
                    out.print(((BasicDBObject)dbCursor.next()).toString());
                }
            } finally {
                dbCursor.close();
            }

        } catch (Exception e) {
            out.print("{\"result\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
        }
        out.close();

    }
}
