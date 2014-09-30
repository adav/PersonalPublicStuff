package Model.API.ASyncResponses;

import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteSocialNameDrop;

/**
 * User: adav
 */
public class HumanFriendlyMessages {
    public static String composeFriendlyPushMessage(WithinSitePlace place) {
        String message = String.format("An interesting %s is nearby. %s has been found based on your interests and proximity.",
                place.getPlaceCategories()[0],
                place.getPlaceName()); //TODO replace interesting with relevant word as to why it's recommended

        //Find friend to name drop:

        if (place.getPlaceSocialNameDropCount() > 0) {
            int randomIndex = (int) (Math.random() * (place.getPlaceSocialNameDropCount()));
            WithinSiteSocialNameDrop friend = place.getSocialNameDrops().get(randomIndex);

            if (place.getPlaceSocialNameDropCount() > 1) {
                message += String.format(" %s, and %d other friends, have visited here.",
                        friend.getSocialName(),
                        place.getPlaceSocialNameDropCount() - 1);
            } else {
                message += String.format(" %s, has visited here.",
                        friend.getSocialName());
            }
        }

        return message.substring(0,Math.min(255, message.length())); //Limit length, just in case. (the APNS service has a length limit)
    }
}
