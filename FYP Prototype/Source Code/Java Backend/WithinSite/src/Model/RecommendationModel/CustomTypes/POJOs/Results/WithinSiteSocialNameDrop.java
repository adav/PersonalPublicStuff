package Model.RecommendationModel.CustomTypes.POJOs.Results;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: adav
 */
public class WithinSiteSocialNameDrop extends JSONObject {

    public enum Relation{
        FRIEND,
        RECOMMENDATION,
        MAYOR
    }

    public WithinSiteSocialNameDrop(String name, String username, String service, Relation relation, String pictureURL){
        try {
            put("name", name);
            put("username", username);
            put("service", service);
            put("relation", relation.name());
            put("profile_picture", pictureURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getSocialName(){
        return optString("name");
    }

    public String getSocialUsername(){
        return optString("username");
    }

    public String getSocialService(){
        return optString("service");
    }

    public Relation getSocialRelation(){
        return Relation.valueOf("relation");
    }

    public String getSocialProfilePicURL(){
        return optString("profile_picture");
    }
}
