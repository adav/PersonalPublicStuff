package Model.API.Requests;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: adav
 */
public class ServerStatusServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/json");
        try {
            //TODO perform some tests to check various aspects of the server's functionality and return a more instructive status
            out.print(new JSONObject().put("status", "ok").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.close();
    }
}
