package Model.RecommendationModel.Models;

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteResults;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;

/**
 * User: adav
 */
public interface RecommendationModelInterface {

    public WithinSiteResults generateRecommendedNearbyResults (WithinSiteUser user);
    public WithinSiteResults generateUninformedNearbyResults(LongLat location);
    public void determineIfAndSendPushNotificationRecommendation (WithinSiteUser user);
    public void addReccomendationToUserList(WithinSiteUser user, WithinSitePlace recommendedPlace);
    public void updateUserPreferencesFromPlaceAtCurrentLocation (WithinSiteUser user);
    public WithinSitePlace getPlaceForServiceId (WithinSiteUser user, String serviceName, String serviceId);
}
