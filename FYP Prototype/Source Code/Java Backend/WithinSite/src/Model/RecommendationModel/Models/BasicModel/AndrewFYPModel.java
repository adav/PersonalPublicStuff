package Model.RecommendationModel.Models.BasicModel;

import Model.API.ASyncResponses.PushNotifications.PushNotificationManager;
import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteResults;
import Model.RecommendationModel.CustomTypes.POJOs.User.RecommendationPreferences.WithinSiteUserRecAutoCheckinPrefs;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import Model.RecommendationModel.Models.BasicModel.Tasks.PlaceRater;
import Model.RecommendationModel.Models.RecommendationModelInterface;
import Model.RecommendationModel.ModuleInterface;
import Model.RecommendationModel.ServiceModules.SocialNetworks.FacebookModule;
import Model.RecommendationModel.ServiceModules.SocialNetworks.FoursquareModule;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: adav
 */
public class AndrewFYPModel implements RecommendationModelInterface {

    protected static final Logger logger = Logger.getLogger(AndrewFYPModel.class);

    //private ThreadPoolExecutor threadPoolExecutor;

    public AndrewFYPModel() {
        //threadPoolExecutor = new ThreadPoolExecutor(5, 100, 5000l, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

    }

    @Override
    public WithinSiteResults generateRecommendedNearbyResults(WithinSiteUser user) {
//        FutureTask<WithinSiteResults> task = new FutureTask<WithinSiteResults>(new FindNearby(user));
//        threadPoolExecutor.execute(task);
//        try {
//            return task.get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        Date startClock = new Date();

        FacebookModule facebookModule = new FacebookModule();
        WithinSiteResults basicResults = new WithinSiteResults();
        basicResults.addResult(facebookModule.getNearestInformedInterestingPlaces(user));

        Date queriedFacebook = new Date();

        FoursquareModule foursquareModule = new FoursquareModule();
        basicResults.addResult(foursquareModule.getNearestUninformedInterestingPlaces(user.getMostRecentLocation().getLongitude(), user.getMostRecentLocation().getLatitude()));
        //todo implement informed results for foursquare^

        Date queriedFoursquare = new Date();

        WithinSiteResults informedResults = new WithinSiteResults();
        informedResults.addResult(PlaceRater.createRecommendationsResultsForUser(user, basicResults));

        Date generatedResults = new Date();

        logger.info("STATS: Facebook Query: " + ((queriedFacebook.getTime()-startClock.getTime())/1000) +
            "Foursquare Query: "+ ((queriedFoursquare.getTime()-queriedFacebook.getTime())/1000) +
            "Generated Results: " +((generatedResults.getTime()-queriedFoursquare.getTime())/1000) +
            "***Total Time: " + ((generatedResults.getTime()-startClock.getTime())/1000));
        return informedResults;
    }

    @Override
    public WithinSiteResults generateUninformedNearbyResults(LongLat location) {
//        FutureTask<WithinSiteResults> task = new FutureTask<WithinSiteResults>(new FindNearby(location));
//        threadPoolExecutor.execute(task);
//        try {
//            return task.get();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
        FoursquareModule foursquareModule = new FoursquareModule();

        WithinSiteResults results = new WithinSiteResults();
        results.addResult(foursquareModule.getNearestUninformedInterestingPlaces(location.getLongitude(), location.getLatitude()));

        return results;
    }

    @Override
    public void determineIfAndSendPushNotificationRecommendation(WithinSiteUser user) {

        FoursquareModule foursquareModule = new FoursquareModule();
        FacebookModule facebookModule = new FacebookModule();
        WithinSiteResults results = new WithinSiteResults();

        results.addResult(foursquareModule.getNearestUninformedInterestingPlaces(user.getMostRecentLocation().getLongitude(), user.getMostRecentLocation().getLatitude()));
        results.addResult(facebookModule.getNearestInformedInterestingPlaces(user));

        //todo Add in here publicservice modules and the ilk

        try {
            WithinSitePlace recommendationForUser = PlaceRater.findRecommendationForUser(user, results);
            if(true){ // todo Final check before sending the user a message!
                PushNotificationManager.sendRecommendationPushNotification(user,recommendationForUser);
                if(!user.placeHasPreviouslyBeenRecommended(recommendationForUser.getPlaceServiceId())){
                    addReccomendationToUserList(user, recommendationForUser);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //PushNotificationManager.sendSystemPushNotification(user, "Reached determineIfAndSendPushNotificationRecommendation() in Model " + (new Date()).toString());
    }

    @Override
    public void addReccomendationToUserList(WithinSiteUser user, WithinSitePlace recommendedPlace) {
        DB db = Model.Util.getDB();

        try {
            db.requestStart();
            db.requestEnsureConnection();

            Date dateRecommended = new Date();

            DBCollection userColl = db.getCollection("withinsite.users");

            BasicDBObject userQuery = new BasicDBObject()
                    .append("email", user.getEmail());

            BasicDBObject push = new BasicDBObject().append("$push", new BasicDBObject()
                    .append("previously_recommended", new BasicDBObject()
                            .append("service_id", recommendedPlace.getPlaceServiceId())
                            .append("service_name", recommendedPlace.getPlaceServiceName())
                            .append("date_recommended", dateRecommended)
                    ));
            //this appends a new recommendation to the array previously_recommended (also creates the field should it not exist in the user document)

            userColl.update(userQuery, push);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.requestDone();
        }
    }

    @Override
    public void updateUserPreferencesFromPlaceAtCurrentLocation(WithinSiteUser user) {

        FoursquareModule foursquareModule = new FoursquareModule();
        if (foursquareModule.canGetNearestPlaceForLocatingUser()) {
            WithinSitePlace frequentedPlace = foursquareModule.getNearestPlace(user.getMostRecentLocation());

            if (frequentedPlace != null) {

                DB db = Model.Util.getDB();

                try {
                    db.requestStart();
                    db.requestEnsureConnection();

                    Date dateCheckin = new Date();

                    DBCollection userColl = db.getCollection("withinsite.users");

                    BasicDBObject userQuery = new BasicDBObject()
                            .append("email", user.getEmail());

                    BasicDBObject push = new BasicDBObject().append("$push", new BasicDBObject()
                            .append("auto_checkins", new BasicDBObject()
                                    .append("name", frequentedPlace.getPlaceName())
                                    .append("location", frequentedPlace.getPlaceLocation().getDBObjForQuery())
                                    .append("categories", frequentedPlace.getPlaceCategories())
                                    .append("service", "foursquare") //TODO programmatic service in place
                                    .append("service_id", frequentedPlace.getPlaceServiceId())
                            ));
                    //this appends a new location to the array locations (also creates the field should it not exist in the user document)

                    userColl.update(userQuery, push);

                    for (String cat : frequentedPlace.getPlaceCategories()) {

                        String catSantised = WithinSiteUserRecAutoCheckinPrefs.sanitiseCategoryForPreferences(cat);

                        BasicDBObject addPref = new BasicDBObject().append("$inc", new BasicDBObject()
                                .append("auto_checkin_prefs." + WithinSiteUserRecAutoCheckinPrefs.getTimePeriod(dateCheckin) + "." + catSantised, 1)
                        );

                        userColl.update(userQuery, addPref);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.requestDone();
                }
            }
        }
    }

    @Override
    public WithinSitePlace getPlaceForServiceId(WithinSiteUser user, String serviceName, String serviceId) {

        if(serviceName.contains(",")){
            String[] serviceNames = serviceName.split(",");
            String[] serviceIds = serviceId.split(",");

            List<WithinSitePlace> placesToAlmagamate = new ArrayList<WithinSitePlace>();

            for(int i = 0; i < serviceNames.length; i++){
                placesToAlmagamate.add(processServiceId(user, serviceNames[i], serviceIds[i]));
            }

            WithinSitePlace place = null;

            for(WithinSitePlace placeInList : placesToAlmagamate){
                if(place == null){
                    place = placeInList;
                } else {
                    place.amalgatePlace(placeInList);
                }
            }

            return place;
        } else {
            return processServiceId(user, serviceName, serviceId);
        }
    }

    private WithinSitePlace processServiceId(WithinSiteUser user, String serviceName, String serviceId){
        ModuleInterface module = null;

        if(serviceName.equalsIgnoreCase("facebook")){
            module = new FacebookModule();
        } else if (serviceName.equalsIgnoreCase("foursquare")){
            module = new FoursquareModule();
        } else {
            return null;
        }

        return module.getPlaceForServiceId(serviceId, user);

    }


    /* JAVA ORACLE EXAMPLE FOR REFERENCE:
    private ThreadPoolExecutor tpe;


  public SingleThreadAccess() {
    tpe = new ThreadPoolExecutor(1, 1, 50000L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<Runnable>());
  }

  public void invokeLater(Runnable r) {
    tpe.execute(r);
  }

  public void invokeAneWait(Runnable r) throws InterruptedException, ExecutionException {
    FutureTask task = new FutureTask(r, null);
    tpe.execute(task);
    task.get();
  }

  public void shutdown() {
    tpe.shutdown();
  }*/
}
