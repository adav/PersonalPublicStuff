package Model.RecommendationModel.CustomTypes.POJOs.Results;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: adav
 */
public class WithinSiteResults extends JSONObject {

    public WithinSiteResults() {
    }

    public WithinSiteResults(Collection<WithinSiteServiceModuleResult> resultsCollection) {
        try {
            put("results", resultsCollection);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addResult(WithinSiteServiceModuleResult result) {
        try {

            append("results", result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<WithinSitePlace> getAllPlacesUnmodified() {

        List<WithinSitePlace> list = new ArrayList<WithinSitePlace>();

        try {
            JSONArray resultsArray = optJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {
                WithinSiteServiceModuleResult result = (WithinSiteServiceModuleResult) resultsArray.get(i);

                //places
                JSONArray placesForResultArray = result.optJSONArray("places");
                if(placesForResultArray != null){
                    for (int j = 0; j < placesForResultArray.length(); j++) {
                        list.add((WithinSitePlace) placesForResultArray.get(j));
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
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
