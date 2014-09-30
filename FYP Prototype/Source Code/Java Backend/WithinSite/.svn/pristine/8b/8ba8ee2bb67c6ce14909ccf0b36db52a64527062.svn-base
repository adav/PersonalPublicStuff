package Model.RecommendationModel.Models.BasicModel.Tasks;

import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSitePlace;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteResults;
import Model.RecommendationModel.CustomTypes.POJOs.Results.WithinSiteServiceModuleResult;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: adav
 */
public class PlaceRater {

    protected static final Logger logger = Logger.getLogger(PlaceRater.class);

    private static SortedSet<WithinSitePlace> orderPlaceResultsForUser (WithinSiteUser user, WithinSiteResults results){

        //logger.info("[PlaceRater] Results param contains " + results.getAllPlacesUnmodified().size() + " places.");

        SortedSet<WithinSitePlace> sortedPlaces = new TreeSet<WithinSitePlace>(new WithinSitePlaceComparator(user));
        /*
        for(WithinSitePlace place : results.getAllPlacesUnmodified()){
            sortedPlaces.add(place); //this also sorts them using the comparator ^
        }
        */
        sortedPlaces.addAll(removeDuplicatePlaces(results.getAllPlacesUnmodified()));

        //logger.info("[PlaceRater] Results sorted contains " + sortedPlaces.size() + " places.");

        return sortedPlaces;

    }

    private static List<WithinSitePlace> removeDuplicatePlaces (List<WithinSitePlace> original){
        List<WithinSitePlace> noDupes = new ArrayList<WithinSitePlace>();

        for (WithinSitePlace place : original) {
            if (!noDupes.contains(place)) {
                noDupes.add(place);
            } else {
                WithinSitePlace existing = noDupes.get(noDupes.indexOf(place));
                existing.amalgatePlace(place);
            }
        }

        return noDupes;


    }

    public static WithinSitePlace findRecommendationForUser (WithinSiteUser user, WithinSiteResults results){

        SortedSet<WithinSitePlace> sortedPlaces = orderPlaceResultsForUser(user, results);
        //todo not quite, needs to see if the place has been recommended before
        return sortedPlaces.first();
    }

    public static WithinSiteServiceModuleResult createRecommendationsResultsForUser (WithinSiteUser user, WithinSiteResults results){

        //return new WithinSiteServiceModuleResult("withinsite_dumb", results.getAllPlacesUnmodified());
        logger.info("[PlaceRater.createRecommendationsResultsForUser()] Results param contains " + results.getAllPlacesUnmodified().size() + " places.");

        SortedSet<WithinSitePlace> sortedPlaces = orderPlaceResultsForUser(user, results);

        //Limit to 15 otherwise connection takes too long:
        if(sortedPlaces.size() > 15){
            sortedPlaces = sortedPlaces.headSet(sortedPlaces.toArray(new WithinSitePlace[sortedPlaces.size()])[14]);
        }

        logger.info("[PlaceRater.createRecommendationsResultsForUser()] returning sorted set contains " + sortedPlaces.size() + " places.");
        return new WithinSiteServiceModuleResult("withinsite", sortedPlaces);
    }
}
