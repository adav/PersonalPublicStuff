package Model.RecommendationModel.CustomTypes.POJOs.User;

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.User.RecommendationPreferences.WithinSiteUserRecAutoCheckinPrefs;
import Model.RecommendationModel.CustomTypes.POJOs.User.RecommendationPreferences.WithinSiteUserRecPrefsAutoCheckinPlace;
import Model.RecommendationModel.CustomTypes.SupportedDevices;
import Model.RecommendationModel.Models.BasicModel.AndrewFYPModel;
import Model.RecommendationModel.Models.RecommendationModel;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import org.bson.types.ObjectId;
import org.json.JSONException;

import java.util.*;

/**
 * User: adav
 */
public class WithinSiteUser extends BasicDBObject {

    public WithinSiteUser() {
    }

    public WithinSiteUser(String email, String fbAccessToken, String pushNotificationToken, SupportedDevices deviceType) {
        put("email", email);
        put("fb_access_token", fbAccessToken);
        put("push_token", pushNotificationToken);
        put("device_registered", deviceType.toString());
    }

    public static WithinSiteUser getUserByEmail(String userEmail) {
        DB db = Model.Util.getDB();

        try {
            db.requestStart();
            db.requestEnsureConnection();

            DBCollection userColl = db.getCollection("withinsite.users");
            userColl.setObjectClass(WithinSiteUser.class);

            DBCursor dbCursor = userColl.find(new BasicDBObject("email", userEmail));
            try {

                if (dbCursor.count() == 1 & dbCursor.hasNext()) {
                    return (WithinSiteUser) dbCursor.next();
                }
            } finally {
                dbCursor.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.requestDone();
        }

        return null;
    }

    public ObjectId getMongoId() {
        return getObjectId("_id");
    }

    public String getEmail() {
        return getString("email");
    }

    public String getFbAccessToken() {
        return getString("fb_access_token");
    }

    public String getPushNotificationToken() {
        return getString("push_token");
    }

    public SupportedDevices getDeviceType() {
        return SupportedDevices.valueOf(getString("device_registered"));
    }

    public ArrayList<BasicDBObject> getLocations() {

        return (ArrayList<BasicDBObject>) get("locations");
    }

    public WithinSiteUserLocation getMostRecentLocation() {
        ArrayList<BasicDBObject> locations = getLocations();
        return new WithinSiteUserLocation(locations.get(locations.size() - 1));
    }

    public ArrayList<BasicDBObject> getAutoCheckins() {
        return (ArrayList<BasicDBObject>) get("auto_checkins");
    }

    public WithinSiteUserRecAutoCheckinPrefs getAutoCheckinPreferences() {
        return new WithinSiteUserRecAutoCheckinPrefs((BasicDBObject) get("auto_checkin_prefs"));
    }

    public SortedSet<WithinSitePlace> getPreviousRecommendationsOrderedByDistance(LongLat relativePosition) {
        /*
        .append("previously_recommended", new BasicDBObject()
                            .append("service_id", recommendedPlace.getPlaceServiceId())
                            .append("service_name", recommendedPlace.getPlaceServiceName())
                            .append("date_recommended", dateRecommended)
         */

        AndrewFYPModel model = RecommendationModel.getModel();
        SortedSet<WithinSitePlace> result = new TreeSet<WithinSitePlace>(new Comparator<WithinSitePlace>() {
            @Override
            public int compare(WithinSitePlace withinSitePlace, WithinSitePlace withinSitePlace2) {
                //returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
                if (withinSitePlace.optDouble("recommendation_distance") < withinSitePlace2.optDouble("recommendation_distance")) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        ArrayList<BasicDBObject> prevRecsArray = (ArrayList<BasicDBObject>) get("previously_recommended");

        for (BasicDBObject placePartial : prevRecsArray) {
            WithinSitePlace place = model.getPlaceForServiceId(this, placePartial.getString("service_name"), placePartial.getString("service_id"));
            try {
                place.put("recommendation_distance", relativePosition.distanceFromPosition(place.getPlaceLocation()));
                int days = (int) (((new Date()).getTime() - placePartial.getDate("date_recommended").getTime()) / (1000 * 60 * 60 * 24));
                place.put("recommendation_age", days);

                result.add(place);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public boolean placeHasPreviouslyBeenRecommended(String serviceId) {
        try {
            ArrayList<BasicDBObject> prevRecsArray = (ArrayList<BasicDBObject>) get("previously_recommended");
            if (prevRecsArray != null) {
                for (BasicDBObject rec : prevRecsArray) {
                    if (rec.getString("service_id").equalsIgnoreCase(serviceId)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
