package Model.API.Requests.Users;

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUserLocation;
import Model.RecommendationModel.Models.RecommendationModel;
import Model.RecommendationModel.Models.RecommendationModelInterface;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: adav
 */
public class UpdateUserLocationServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");

        DB db = Model.Util.getDB();

        RecommendationModelInterface model = RecommendationModel.getModel();

        try {
            db.requestStart();
            db.requestEnsureConnection();


            DBCollection userColl = db.getCollection("withinsite.users");

            BasicDBObject user = new BasicDBObject()
                    .append("email", request.getParameter("email"));

            BasicDBObject push = new BasicDBObject().append("$push", new BasicDBObject()
                    .append("locations", new BasicDBObject()
                            .append("longitude", Double.valueOf(request.getParameter("longitude")))
                            .append("latitude", Double.valueOf(request.getParameter("latitude")))
                            .append("accuracy", Double.valueOf(request.getParameter("accuracy"))) //TODO Clear collection
                            .append("updated", new Date())
                    ));
            //this appends a new location to the array locations (also creates the field should it not exist in the user document)

            userColl.update(user, push);

            WithinSiteUser userDBObject = WithinSiteUser.getUserByEmail(request.getParameter("email"));

            if (request.getParameter("region_update").equalsIgnoreCase("true")) {

                //check that it's due an update
                ArrayList<BasicDBObject> usersLocations = userDBObject.getLocations();
                WithinSiteUserLocation lastUserLocation = new WithinSiteUserLocation(usersLocations.get(usersLocations.size() - 2));
                WithinSiteUserLocation newUserLocation = new WithinSiteUserLocation(usersLocations.get(usersLocations.size() - 1));

                if (LongLat.newRecommendationsRequired(
                        lastUserLocation.getLongitude(),
                        lastUserLocation.getLatitude(),
                        lastUserLocation.getUpdated(),
                        newUserLocation.getLongitude(),
                        newUserLocation.getLatitude(),
                        newUserLocation.getUpdated())) {

                    model.determineIfAndSendPushNotificationRecommendation(userDBObject);

                }

            } else {
                model.updateUserPreferencesFromPlaceAtCurrentLocation(userDBObject);
            }


        } catch (Exception e) {
            out.print("{\"result\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        } finally {
            db.requestDone();
        }
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
