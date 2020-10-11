package apibuilder;

import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import reporting.logging.Logger;

import java.util.*;

import static io.restassured.RestAssured.given;

public class APIRequestResponseBuilder {

    public static final int HTTP_OK = 200;
    private static APIRequestResponseBuilder instance = null;

    public static APIRequestResponseBuilder getInstance() {
        if (instance == null)
            instance = new APIRequestResponseBuilder();
        return instance;
    }

    public Response verifyStatusCode(Response response, int statusCode) {
        Assert.assertEquals(response.statusCode(), statusCode);
        return response;
    }

    private List getImbdId() {
        Response response = given().when().header("Accept", "application/json").queryParam("type", "movie").queryParam("s", "lord of the rings").queryParam("apikey", "3140b7b9").get("http://www.omdbapi.com/");
        verifyStatusCode(response, HTTP_OK);
        String json = response.asString();
        Logger.logPass("Response with imdb ids  "+json);
        JSONObject obj = new JSONObject(json);
        List imdbIDList = new ArrayList();
        JSONArray arr = obj.getJSONArray("Search");
        for (int i = 0; i < arr.length(); i++) {
            String imdbId = arr.getJSONObject(i).getString("imdbID");
            imdbIDList.add(imdbId);
        }
        System.out.println(imdbIDList);
        return imdbIDList;
    }

    private List<IMDBMovieDetail> getTileWithRating(List imdbId) {
        ArrayList<IMDBMovieDetail> list = new ArrayList<>();
        for (int i = 0; i < imdbId.size(); i++) {
            Response response = given().when().header("Accept", "application/json").queryParam("i", imdbId.get(i)).queryParam("apikey", "3140b7b9").get("http://www.omdbapi.com/");
            verifyStatusCode(response, HTTP_OK);
            String json = response.asString();
            Logger.logPass("Response with each imdb id  "+json);
            JSONObject obj = new JSONObject(json);
            list.add(new IMDBMovieDetail(Double.valueOf(obj.getString("imdbRating")), obj.getString("Title")));
        }
        return list;
    }

    public Map<String, Double> getTopThreeMovieWithTitleAndRating() {
        List<IMDBMovieDetail> list = getTileWithRating(getImbdId());
        Collections.sort(list);
        System.out.println("After sorting");
        System.out.println();
        HashMap<String, Double> map = new HashMap();
        Logger.logPass("Below are the top three results");
        for (int i=0;i<3;i++){
            Logger.logPass(list.get(i).getTiltle()+" "+list.get(i).getImdbRating() + " = <b>Pass</b>");;
        }
        map.put(list.get(0).getTiltle(), list.get(0).getImdbRating());
        map.put(list.get(1).getTiltle(), list.get(1).getImdbRating());
        map.put(list.get(2).getTiltle(), list.get(2).getImdbRating());
        return map;
    }
}
