package Model.API.ASyncResponses.PushNotifications;

import Model.API.ASyncResponses.PushNotifications.ApplePushNotification.Apns;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import Model.RecommendationModel.CustomTypes.SupportedDevices;

/**
 * User: adav
 */
public class PushNotificationManager {

    private static Apns apnsService = new Apns();

    private static PushNotificationServiceInterface getPushServiceForUser(WithinSiteUser user){
        if(user.getDeviceType() == SupportedDevices.APPLE_IPHONE){
            return apnsService;
        }

        return null;
    }

    public static void sendRecommendationPushNotification(WithinSiteUser user, WithinSitePlace place){
        getPushServiceForUser(user).sendRecommendationPushNotification(user.getPushNotificationToken(), place);
    }

    public static void sendSystemPushNotification(WithinSiteUser user, String message){
        getPushServiceForUser(user).sendSystemPushNotification(user.getPushNotificationToken(), message);
    }
}
