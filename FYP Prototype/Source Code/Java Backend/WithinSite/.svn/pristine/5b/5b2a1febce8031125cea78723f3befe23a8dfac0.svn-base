package Sandbox;

import Model.RecommendationModel.ServiceModules.SocialNetworks.FacebookModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: adav
 */
public class TestFacebookServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FacebookModule facebookModule = new FacebookModule();
        facebookModule.postMessageToWall(request.getParameter("message"), request.getParameter("token"));


    }
}
