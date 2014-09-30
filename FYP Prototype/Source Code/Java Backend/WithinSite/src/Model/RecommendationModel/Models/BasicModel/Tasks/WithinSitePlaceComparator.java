package Model.RecommendationModel.Models.BasicModel.Tasks;

import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import org.apache.log4j.Logger;

import java.util.Comparator;

/**
 * User: adav
 */
public class WithinSitePlaceComparator implements Comparator<WithinSitePlace> {

    private WithinSiteUser user;

    protected static final Logger logger = Logger.getLogger(PlaceRater.class);

    public WithinSitePlaceComparator(WithinSiteUser user){
        this.user = user;
    }

    @Override
    public int compare(WithinSitePlace t, WithinSitePlace t1) {
        //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
        try {

            logger.info("[Comp compare()] Place: "+t.getPlaceName() +  " Place1: "+t1.getPlaceName());

            //!!!!!!!!!!!!!!!!!!!!!
            //Reverse sort (so higher rate places come first!)
            if (t.getAndUpdateSimplePlaceRecommendationRating(user) < t1.getAndUpdateSimplePlaceRecommendationRating(user)) {
                return 1;
            }  else {
                return -1;//Can't be 0 as then it will be deleted.
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}
