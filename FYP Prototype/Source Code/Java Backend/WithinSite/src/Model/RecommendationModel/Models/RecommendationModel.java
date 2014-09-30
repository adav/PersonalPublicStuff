package Model.RecommendationModel.Models;

import Model.RecommendationModel.Models.BasicModel.AndrewFYPModel;

/**
 * User: adav
 */
public class RecommendationModel {

    //Change this class to switch models

    private static AndrewFYPModel ourInstance = new AndrewFYPModel();

    public static AndrewFYPModel getModel() {
        return ourInstance;
    }

}
