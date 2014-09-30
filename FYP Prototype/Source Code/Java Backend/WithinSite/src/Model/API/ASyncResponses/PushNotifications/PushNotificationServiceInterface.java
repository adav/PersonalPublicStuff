package Model.API.ASyncResponses.PushNotifications;

import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;

/**
 * User: adav
 */
public interface PushNotificationServiceInterface {
    public void sendRecommendationPushNotification(String token, WithinSitePlace place);
    public void sendSystemPushNotification(String token, String message);
}
