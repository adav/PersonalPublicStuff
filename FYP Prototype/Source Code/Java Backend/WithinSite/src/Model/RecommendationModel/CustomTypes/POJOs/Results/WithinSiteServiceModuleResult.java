package Model.RecommendationModel.CustomTypes.POJOs.Results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;

/**
 * User: adav
 */
public class WithinSiteServiceModuleResult extends JSONObject {

    public WithinSiteServiceModuleResult(String serviceName){
        try {
            put("name", serviceName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public WithinSiteServiceModuleResult(String serviceName, Collection<WithinSitePlace> places){
        try {
            put("name", serviceName);
            put("places", places);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addPlace(WithinSitePlace place) {
        try {
            append("places", place);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("\\\\r\\\\n|\\\\r|\\\\n", " ").replaceAll("[^\\u0000-\\uFFFF]", "");//.replaceAll( "([\\ud800-\\udbff\\udc00-\\udfff])", "");
    }

    @Override
    public String toString(int i) throws JSONException {
        return super.toString(i).replaceAll("\\\\r\\\\n|\\\\r|\\\\n", " ").replaceAll("[^\\u0000-\\uFFFF]", "");//.replaceAll( "([\\ud800-\\udbff\\udc00-\\udfff])", "");
    }

}
