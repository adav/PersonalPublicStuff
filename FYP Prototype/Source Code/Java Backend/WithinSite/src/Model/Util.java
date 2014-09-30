package Model;

/**
 * User: adav
 */

import Model.RecommendationModel.CustomTypes.LongLat;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUser;
import Model.RecommendationModel.CustomTypes.POJOs.User.WithinSiteUserLocation;
import Model.RecommendationModel.Models.RecommendationModel;
import Model.RecommendationModel.Models.RecommendationModelInterface;
import com.mongodb.*;

import javax.servlet.ServletOutputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

public class Util {

    public static DB getDB(){
        try{
            //Mongo mongo = new Mongo("dbteach2.cs.bham.ac.uk");
            MongoClient mongoClient = new MongoClient("localhost");                        //dbteach2.cs.bham.ac.uk
            DB db = mongoClient.getDB("axd967");
            boolean auth = db.authenticate("axd967", "mymriEdUd".toCharArray());
            return db;
        } catch (UnknownHostException ex){
            throw new ExceptionInInitializerError(ex);
        }
    }

}