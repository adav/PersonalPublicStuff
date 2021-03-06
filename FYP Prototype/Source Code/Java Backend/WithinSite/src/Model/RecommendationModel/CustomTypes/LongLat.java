package Model.RecommendationModel.CustomTypes;

import com.mongodb.BasicDBObject;

import java.util.Date;

/**
 * User: adav
 */
public class LongLat extends BasicDBObject{

    public LongLat(double longitude, double latitude){
        put("longitude", longitude);
        put("latitude", latitude);
    }

    public double getLatitude() {
        return getDouble("latitude");
    }

    public double getLongitude() {
        return getDouble("longitude");
    }

    public BasicDBObject getDBObjForQuery(){
        return new BasicDBObject().append("x", getLongitude()).append("y", getLatitude());
    }

    public double distanceFromPosition(double long1, double lat1){
        return distanceBetweenPositions(long1, lat1, getLongitude(), getLatitude());
    }

    public double distanceFromPosition(LongLat newPos){
        return distanceBetweenPositions(newPos.getLongitude(), newPos.getLatitude(), getLongitude(), getLatitude());
    }

    public static double distanceBetweenPositions(double long1, double lat1,  double long2, double lat2) {

        //This is based on http://en.wikipedia.org/wiki/Haversine_formula

        //TODO verify that this works to a reasonable accuracy.

        double earthRadius = 6370990.56; //meters!

        double a = Math.pow(Math.sin(Math.toRadians(lat2-lat1) / 2), 2)
                + Math.pow(Math.sin(Math.toRadians(long2-long1) / 2), 2)
                * Math.cos(Math.toRadians(lat1))
                *  Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public static double speedBetweenPositions(double long1, double lat1, Date date1, double long2, double lat2, Date date2){
        double distanceBetween = distanceBetweenPositions(long1, lat1, long2, lat2); //distance in meters
        long timeBetween = (date2.getTime() - date1.getTime()) / 1000; //Difference in seconds
        return distanceBetween/timeBetween;
    }

    public static boolean newRecommendationsRequired(double long1, double lat1, Date date1, double long2, double lat2, Date date2){

        double speed = speedBetweenPositions( long1,  lat1,  date1,  long2,  lat2,  date2);
        double distance =  distanceBetweenPositions(long1, lat1, long2, lat2);

        //0 - 10mph =  4.4704 m/s   <- Walking / Slow Cycling <- Radius of search should be 300m
        //11 - 30mph = 13.4112 m/s  <- Driving in a town <- Radius of search should be 1000m
        //31+                       <- Fast driving <- Larger search radius 2000m
        return true; //todo for debugging
        /*if(speed < 4.5 && distance > 300){
            return true;
        } else if (speed < 13.4 && distance > 1000){
            return true;
        } else if (distance > 2000){
            return true;
        }

        return false;*/
    }
}
