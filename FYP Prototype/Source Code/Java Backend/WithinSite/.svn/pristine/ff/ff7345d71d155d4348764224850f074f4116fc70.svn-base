package Model.API.Requests.Users;

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteServiceModuleResult;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUserLocation;
import Model.RecommendationModel.Models.RecommendationModel;
import Model.RecommendationModel.Models.RecommendationModelInterface;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.SortedSet;

/**
 * User: adav
 */
public class UserRecommendationsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream out = response.getOutputStream();

        try {

            WithinSiteUser user = WithinSiteUser.getUserByEmail(request.getParameter("email"));
            SortedSet<WithinSitePlace> recommendations = user.getPreviousRecommendationsOrderedByDistance(
                    new LongLat(Double.valueOf(
                            request.getParameter("longitude")),
                            Double.valueOf(request.getParameter("latitude"))));

            WithinSiteServiceModuleResult result = new WithinSiteServiceModuleResult("recommendations", recommendations);

            out.print(result.toString(4));

        } catch (Exception e) {
            out.print("{\"result\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
            e.printStackTrace();
        }
        out.close();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DB db = Model.Util.getDB();

        try {
            db.requestStart();
            db.requestEnsureConnection();


            DBCollection userColl = db.getCollection("withinsite.users");

            BasicDBObject user = new BasicDBObject()
                    .append("email", req.getParameter("email"));

            //This removes the field: previously_recommended which contains all recommendations
            BasicDBObject unset = new BasicDBObject().append("$unset", new BasicDBObject().append("previously_recommended", 1));

            userColl.update(user, unset);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.requestDone();
        }
    }
}
