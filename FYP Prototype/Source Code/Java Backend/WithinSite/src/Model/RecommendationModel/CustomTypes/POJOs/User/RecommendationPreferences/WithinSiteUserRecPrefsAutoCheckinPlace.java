package Model.RecommendationModel.CustomTypes.POJOs.User.RecommendationPreferences;

import com.mongodb.BasicDBObject;

/**
 * User: adav
 */
public class WithinSiteUserRecPrefsAutoCheckinPlace extends BasicDBObject{

    public WithinSiteUserRecPrefsAutoCheckinPlace(){}

    public WithinSiteUserRecPrefsAutoCheckinPlace(BasicDBObject basic){
        super(basic.toMap());
    }

    /*
    .append("name", frequentedPlace.getPlaceName())
    .append("location", frequentedPlace.getPlaceLocation().getDBObjForQuery())
    .append("categories", frequentedPlace.getPlaceCategories())
    .append("service", "foursquare") //TODO programmatic service in place
    .append("service_id", frequentedPlace.getPlaceServiceId())
     */

    public WithinSiteUserRecPrefsAutoCheckinPlace(String name, BasicDBObject location, String[] categories, String serviceName, String serviceId){
        put("name", name);
        put("location", location);
        put("categories", categories);
        put("service", serviceName);
        put("service_id", serviceId);
    }

    public String getName(){
        return getString("name");
    }

    public BasicDBObject getLocation(){
        return (BasicDBObject) get("location");
    }

    public String[] getCategories(){
        return (String[]) get("categories");
    }

    public String getServiceName(){
        return getString("service");
    }

    public String getServiceId(){
        return getString("service_id");
    }
}
