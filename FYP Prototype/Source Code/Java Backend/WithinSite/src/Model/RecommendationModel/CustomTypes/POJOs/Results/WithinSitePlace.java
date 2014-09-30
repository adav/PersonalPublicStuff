package Model.RecommendationModel.CustomTypes.POJOs.Results;

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.User.RecommendationPreferences.WithinSiteUserRecAutoCheckinPrefs;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.*;

/**
 * User: adav
 */
public class WithinSitePlace extends JSONObject {

    protected static final Logger logger = Logger.getLogger(WithinSitePlace.class);

    public WithinSitePlace(String name,
                           String description,
                           double longitude,
                           double latitude,
                           String[] categories,
                           int checkins,
                           int fans,
                           long footfall,
                           Collection<WithinSiteSocialNameDrop> socialNameDrops,
                           Collection<WithinSiteSocialLink> socialLinks,
                           String[] pictureURLs,
                           String serviceName,
                           String serviceId,
                           boolean verified) {
        try {
            put("name", name);
            put("description", description);
            put("location", new JSONObject().put("longitude", longitude).put("latitude", latitude));
            put("categories", categories);
            put("checkins", checkins);
            put("fans", fans);
            put("footfall", footfall);
            put("social_name_drop", socialNameDrops);
            put("links", socialLinks);
            put("pictures", pictureURLs);
            put("service_name", serviceName);
            put("service_id", serviceId);
            put("verified", verified);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getPlaceName() {
        return optString("name");
    }

    public String getPlaceDescription() {
        return optString("description");
    }

    public LongLat getPlaceLocation() {
        JSONObject location = optJSONObject("location");
        return new LongLat(location.optDouble("longitude"), location.optDouble("latitude"));
    }

    public String[] getPlaceCategories() {
        return (String[]) opt("categories");
    }

    public String[] getPlacePictures() {
        return (String[]) opt("pictures");
    }

    public List<WithinSiteSocialNameDrop> getSocialNameDrops() {
        List<WithinSiteSocialNameDrop> list = new ArrayList<WithinSiteSocialNameDrop>();
        try {
            JSONArray jsonArray = optJSONArray("social_name_drop");
            for (int i = 0; i < jsonArray.length(); i++) {

                list.add((WithinSiteSocialNameDrop) jsonArray.get(i));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WithinSiteSocialLink> getSocialLinks() {
        List<WithinSiteSocialLink> list = new ArrayList<WithinSiteSocialLink>();
        try {
            JSONArray jsonArray = optJSONArray("links");
            for (int i = 0; i < jsonArray.length(); i++) {

                list.add((WithinSiteSocialLink) jsonArray.get(i));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getPlaceCheckins() {
        return optInt("checkins");
    }

    public int getPlaceFans() {
        return optInt("fans");
    }

    public long getPlaceFootfall() {
        return optInt("footfall");
    }

    public int getPlaceSocialNameDropCount() {
        return optJSONArray("social_name_drop").length();
    }

    public boolean getPlaceVerified() {
        return optBoolean("verfied");
    }

    public String getPlaceServiceName() {
        return optString("service_name");
    }

    public String getPlaceServiceId() {
        return optString("service_id");
    }

    public int getAndUpdateSimplePlaceRecommendationRating(WithinSiteUser user) {

        int score = 1;

        for (String category : getPlaceCategories()) {
            score += 1000 * user.getAutoCheckinPreferences().getFrequencyForPeriodAndUnsanitisedCategory(
                    WithinSiteUserRecAutoCheckinPrefs.getTimePeriodEnum(new Date()),
                    category
            );
        } //Give a really heavy initial weighting to places that match preferences for time of day

        score = score * (1 + getPlaceSocialNameDropCount()); //more points for each friend at a place

        score = score * (1 + getPlaceCheckins() * 2);

        score = score * (1 + getPlaceFans() * 4);

        score = score * (1 + (int) getPlaceFootfall());

        if (getPlaceVerified()) {
            score *= 1.2; //20% boost if it's a verified location
        }

        //todo: add means to boost new establishments (with few checkins), those that are trending,
        // those that align with the user's (and user's friend's) facebook interests and activities

        logger.info("[Comp updatePlaceRecommendationRating()] Place: " + getPlaceName() + " Score:" + String.valueOf(score));

        try {
            put("ranking", score);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return score;

    }

    @Override
    public boolean equals(Object o) {
        try {
            WithinSitePlace placeO = (WithinSitePlace) o;
            if (this.getPlaceLocation().distanceFromPosition(placeO.getPlaceLocation()) < 50) {

                if (this.getPlaceName().toUpperCase().replaceAll("[^A-Z0-9]", "").equalsIgnoreCase(
                        placeO.getPlaceName().toUpperCase().replaceAll("[^A-Z0-9]", ""))) {
                    return true;
                }

                if (this.getSocialLinks().size() > 0 && placeO.getSocialLinks().size() > 0) {

                    for (WithinSiteSocialLink linkThis : this.getSocialLinks()) {
                        for (WithinSiteSocialLink linkO : placeO.getSocialLinks()) {
                            if (linkThis.getString("meta").contains(linkO.getString("meta"))) {
                                return true;
                            }
                        }
                    }

                }

                return false;

            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void amalgatePlace(WithinSitePlace place) {

        try {
            if (getPlaceDescription() == null || getPlaceDescription().length() == 0) {
                put("description", place.getPlaceDescription());
            }


            List<String> newCategories = new ArrayList<String>();
            for (String cat : getPlaceCategories()) {
                newCategories.add(cat);
            }
            for (String cat : place.getPlaceCategories()) {
                newCategories.add(cat);
            }
            put("categories", newCategories.toArray(new String[newCategories.size()]));

            put("checkins", getPlaceCheckins() + place.getPlaceCheckins());
            put("fans", getPlaceFans() + place.getPlaceFans());
            put("footfall", getPlaceFootfall() + place.getPlaceFootfall());

            for(WithinSiteSocialNameDrop friend : place.getSocialNameDrops()){
                append("social_name_drop", friend);
            }

            for(WithinSiteSocialLink link : place.getSocialLinks()){
                append("links", link);
            }

            List<String> newPictures = new ArrayList<String>();
            if (getPlacePictures() != null) {
                for (String pic : getPlacePictures()) {
                    newPictures.add(pic);
                }
            }
            if (place.getPlacePictures() != null) {
                for (String pic : place.getPlacePictures()) {
                    newPictures.add(pic);
                }
            }
            put("pictures", newPictures.toArray(new String[newPictures.size()]));

            put("service_name", getPlaceServiceName() + "," + place.getPlaceServiceName());
            put("service_id", getPlaceServiceId() + "," + place.getPlaceServiceId());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


    /*
    {
                                "name" : "Selly Sausage",
                                "description" : "Lot's of lovely sausages!!!",
                                "location" : {"longitude" : 1.0, "latitude" : 55.323},

                                "categories" : ["bar", "cafe", "restaurant"],
                                "checkins" : 432,
                                "fans" : 123,
                                "footfall" : 12345,
                                "social_name_drop" :
                                    [
                                        {"name" : "Jack Evans", "username" : "jev55", "service" : "facebook", "profile_picture" : "http://lolol.com/img.jpg"}
                                    ],
                                "links" :
                                    [
                                        { "name" : "website", "meta" : "http://www.sellysausage.com/" },
                                        { "name" : "facebook", "meta" : "http://www.facebook.com/selly" },
                                        { "name" : "twitter", "meta" : "http://www.twitter.com/selly" }
                                    ],
                                "pictures" :
                                    ["http://lolol.com/img.jpg", "http://poo.com/img.jpg"],

                            }
     */
