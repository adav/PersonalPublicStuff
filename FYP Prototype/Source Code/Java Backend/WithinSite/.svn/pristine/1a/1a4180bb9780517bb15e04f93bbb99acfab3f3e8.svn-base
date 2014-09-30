package Model.RecommendationModel.CustomTypes.POJOs.Results;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: adav
 */
public class WithinSiteSocialLink extends JSONObject {

    //{ "name" : "website", "meta" : "http://www.sellysausage.com/" }

    public WithinSiteSocialLink(String name, String metaURL){
        try {
            put("name", name);
            put("meta", metaURL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
