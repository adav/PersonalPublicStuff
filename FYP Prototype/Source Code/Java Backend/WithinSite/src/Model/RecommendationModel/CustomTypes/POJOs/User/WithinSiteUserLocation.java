package Model.RecommendationModel.CustomTypes.POJOs.User;

import Model.RecommendationModel.CustomTypes.LongLat;
import com.mongodb.BasicDBObject;

import java.util.Date;

/**
 * User: adav
 */
public class WithinSiteUserLocation extends BasicDBObject{

    //{     "longitude" : "-1.932409",     "latitude" : "52.446661",     "accuracy" : "50.000000",     "updated" : ISODate("2013-02-21T03:05:40.905Z") }

    public WithinSiteUserLocation(){}

    public WithinSiteUserLocation(BasicDBObject basic){
        super(basic.toMap());
    }

    public WithinSiteUserLocation(double longitude, double latitude, double accuracy, Date updated){
        put("longitude", longitude);
        put("latitude", latitude);
        put("accuracy", accuracy);
        put("updated", updated);
    }

    public WithinSiteUserLocation(LongLat longLat, double accuracy, Date updated){
        put("longitude", longLat.getLongitude());
        put("latitude", longLat.getLatitude());
        put("accuracy", accuracy);
        put("updated", updated);
    }

    public double getLongitude(){
        return getDouble("longitude");
    }

    public double getLatitude(){
        return getDouble("latitude");
    }

    public double getAccuracy(){
        return getDouble("accuracy");
    }

    public LongLat getLatLong(){
        return new LongLat(getLatitude(), getLongitude());
    }

    public Date getUpdated(){
        return getDate("updated");
    }
}
