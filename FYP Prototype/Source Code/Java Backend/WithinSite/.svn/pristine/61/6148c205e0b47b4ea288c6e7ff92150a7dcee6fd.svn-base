package Model.RecommendationModel.Models.BasicModel.Tasks;

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteResults;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;

import java.util.concurrent.Callable;

/**
 * User: adav
 */
public class FindNearby implements Callable<WithinSiteResults> {

    private WithinSiteUser user;
    private LongLat location;

    public FindNearby(LongLat location){
        this.location = location;
    }

    public FindNearby(WithinSiteUser user){
        this.user = user;
    }

    @Override
    public WithinSiteResults call() throws Exception {
        WithinSiteResults results = new WithinSiteResults();

        if(user != null) {

        } else if(location != null) {

        }

        return null;
    }
}
