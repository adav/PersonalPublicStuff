package Model.RecommendationModel.ServiceModules.SocialNetworks;

import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteServiceModuleResult;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteSocialLink;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteSocialNameDrop;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUserLocation;
import Model.RecommendationModel.ModuleInterface;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONObject;

import java.util.ArrayList;

/**
 * User: adav
 */
public class FacebookModule implements ModuleInterface {

    final String WITHINSITERESULT_IDENTIFICATION = "facebook";

    final String API_KEY = "325368314245378";
    final String APP_SECRET = "841ebb55fe21f04531d4cb94aa8b10d4";
    final String APP_NAMESPACE = "withinsite";

    @Override
    public WithinSiteServiceModuleResult getNearestUninformedInterestingPlaces(double x, double y) {
        return null; //Not implemented as this is not Facebook's forté.
    }

    @Override
    public WithinSiteServiceModuleResult getNearestInformedInterestingPlaces(WithinSiteUser user) {

        WithinSiteUserLocation userLocation = user.getMostRecentLocation();

        WithinSiteServiceModuleResult returnCollection = new WithinSiteServiceModuleResult(WITHINSITERESULT_IDENTIFICATION);

        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(API_KEY, APP_SECRET);
        facebook.setOAuthAccessToken(new AccessToken(user.getFbAccessToken(), null));

        String query = "select page_id, name, description, location, categories, type, checkins, fan_count, were_here_count, website, page_url " +
                "from page where page_id in " +
                "(select page_id from location_post where author_uid IN" +
                "(SELECT uid2 FROM friend WHERE uid1=me())" +
                "and distance(latitude, longitude, '" + userLocation.getLatitude() + "','" + userLocation.getLongitude() + "') < 400)";

        try {
            JSONArray placesTaggedByFriends = facebook.executeFQL(query);

            for (int i = 0; i < placesTaggedByFriends.length(); i++) {
                JSONObject place = placesTaggedByFriends.getJSONObject(i);

                //Build Social Name Drop List
                ArrayList<WithinSiteSocialNameDrop> socialNameDrops = new ArrayList<WithinSiteSocialNameDrop>();
                JSONArray friendsAtPlace = facebook.executeFQL(
                        "select name, username, pic_square_with_logo " +
                                "from user where uid in " +
                                "(select tagged_uids from location_post where page_id = " + place.getString("page_id") + ")" +
                                "");//and uid in (SELECT uid2 FROM friend WHERE uid1 = me())

                for(int j = 0; j < friendsAtPlace.length(); j++){
                    JSONObject friend = friendsAtPlace.getJSONObject(j);
                    socialNameDrops.add(new WithinSiteSocialNameDrop(
                            friend.getString("name"),
                            friend.getString("username"),
                            "facebook",
                            WithinSiteSocialNameDrop.Relation.FRIEND,
                            friend.getString("pic_square_with_logo")
                    ));

                }

                //Build Categories List
                ArrayList<String> categories = new ArrayList<String>();
                for(int k = 0; k < place.getJSONArray("categories").length(); k++){
                    JSONObject cat = place.getJSONArray("categories").getJSONObject(k);
                    categories.add(cat.getString("name"));
                }

                //Build Social Links List
                ArrayList<WithinSiteSocialLink> socialLinks = new ArrayList<WithinSiteSocialLink>();
                socialLinks.add(new WithinSiteSocialLink("url", place.getString("website")));
                socialLinks.add(new WithinSiteSocialLink("facebook", place.getString("page_url")));

                returnCollection.addPlace(new WithinSitePlace(
                        place.getString("name"),
                        place.getString("description"),
                        place.getJSONObject("location").getDouble("longitude"),
                        place.getJSONObject("location").getDouble("latitude"),
                        categories.toArray(new String[categories.size()]),
                        place.getInt("checkins"),
                        place.getInt("fan_count"),
                        place.getLong("were_here_count"),
                        socialNameDrops,
                        socialLinks,
                        null,
                        "facebook",
                        place.getString("page_id"),
                        false
                ));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnCollection;
    }

    @Override
    public boolean canGetNearestPlaceForLocatingUser() {
        return false;
    }

    @Override
    public WithinSitePlace getPlaceForServiceId(String serviceId, WithinSiteUser user) {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(API_KEY, APP_SECRET);
        facebook.setOAuthAccessToken(new AccessToken(user.getFbAccessToken(), null));

        String query = "select page_id, name, description, location, categories, type, checkins, fan_count, were_here_count, website, page_url " +
                "from page where page_id = " + serviceId;

        try{
            JSONArray responseForId = facebook.executeFQL(query);
            JSONObject place = responseForId.getJSONObject(0);

            //Build Social Name Drop List
            ArrayList<WithinSiteSocialNameDrop> socialNameDrops = new ArrayList<WithinSiteSocialNameDrop>();
            JSONArray friendsAtPlace = facebook.executeFQL(
                    "select name, username, pic_square_with_logo " +
                            "from user where uid in " +
                            "(select tagged_uids from location_post where page_id = " + place.getString("page_id") + ")");

            for(int j = 0; j < friendsAtPlace.length(); j++){
                JSONObject friend = friendsAtPlace.getJSONObject(j);
                socialNameDrops.add(new WithinSiteSocialNameDrop(
                        friend.getString("name"),
                        friend.getString("username"),
                        "facebook",
                        WithinSiteSocialNameDrop.Relation.FRIEND,
                        friend.getString("pic_square_with_logo")
                ));

            }

            //Build Categories List
            ArrayList<String> categories = new ArrayList<String>();
            for(int k = 0; k < place.getJSONArray("categories").length(); k++){
                JSONObject cat = place.getJSONArray("categories").getJSONObject(k);
                categories.add(cat.getString("name"));
            }

            //Build Social Links List
            ArrayList<WithinSiteSocialLink> socialLinks = new ArrayList<WithinSiteSocialLink>();
            socialLinks.add(new WithinSiteSocialLink("url", place.getString("website")));
            socialLinks.add(new WithinSiteSocialLink("facebook", place.getString("page_url")));

            return new WithinSitePlace(
                    place.getString("name"),
                    place.getString("description"),
                    place.getJSONObject("location").getDouble("longitude"),
                    place.getJSONObject("location").getDouble("latitude"),
                    categories.toArray(new String[categories.size()]),
                    place.getInt("checkins"),
                    place.getInt("fan_count"),
                    place.getLong("were_here_count"),
                    socialNameDrops,
                    socialLinks,
                    null,
                    "facebook",
                    place.getString("page_id"),
                    false
            );

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public WithinSitePlace getNearestPlace(WithinSiteUserLocation userLocation) {
        return null;
    }

    public void postMessageToWall(String message, String token) {
        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId(API_KEY, APP_SECRET);
        facebook.setOAuthAccessToken(new AccessToken(token, null));

        try {
            facebook.postStatusMessage(message);
        } catch (FacebookException e) {
            e.printStackTrace();
        }
    }
}
                        /*select page_id, name, description, type, checkins, fan_count, were_here_count, website, page_url from page where page_id in (select page_id from location_post where author_uid IN
                (SELECT uid2 FROM friend WHERE uid1=me()) and distance(latitude, longitude, '52.445888','-1.933197') < 400) */


/*
select name, username, pic_square_with_logo from user where uid in (select tagged_uids from location_post where page_id = <page_ID>)
 */
