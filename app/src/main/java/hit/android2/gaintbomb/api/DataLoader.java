package hit.android2.gaintbomb.api;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hit.android2.Adapters.GameAdapter;
import hit.android2.Database.Model.GameData;

public class DataLoader {

    private String API_KEY;

    private GameData gameData = null;

    private Context context;

   // private GameAdapter gameAdapter;

    //private List<GameData> gameDataList = null;

    public DataLoader(String API_KEY, Context context) {
        this.API_KEY = API_KEY;
        this.context = context;
      //  this.gameDataList = gameDataList;
      //  this.gameAdapter = gameAdapter;

        System.out.println("DataLoader created");



    }

    public interface Listener{

        void onSuccess(String string);
    }

    public void searchGameRequest(final String gameName, final List<GameData> gameDataList, final GameAdapter gameAdapter, Listener listener){

        System.out.println("searchGameRequest called");


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(createStringForSearchRequest(gameName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("searchGameRequest  onResponse called");


                try {
                    JSONObject rootObject = new JSONObject(response);
                    createGameItemList(rootObject, gameDataList);
                    gameAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listener.onSuccess(null);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("searchGameRequest  onErrorResponse called");


                error.printStackTrace();

            }
        });

        queue.add(request);

        //queue.start();

    }

    public void getCharactersByGameRequest(final String gameName, final Listener listener){

        System.out.println("getCharacterImagesByGame called");


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(createStringForGameRequest(gameName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("getCharacterImagesByGame  onResponse called");


                try {
                    JSONObject rootObject = new JSONObject(response);
                    createGameItem(rootObject,listener);
                    //gameAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("getCharacterImagesByGame  onErrorResponse called");


                error.printStackTrace();

            }
        });

        queue.add(request);

        //queue.start();

    }

    public void getCharactersRequest(final String id, final Listener listener){

        System.out.println("getCharacterImagesByGame called");


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(createStringForCharacterRequest(id), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("getCharacterImagesByGame  onResponse called");


                try {
                    JSONObject rootObject = new JSONObject(response);
                    JSONObject resultsObject = rootObject.getJSONObject("results");
                    JSONObject imageObject = resultsObject.getJSONObject("image");
                  //  String thumb_url = imageObject.getString("thumb_url");
                    String thumb_url = imageObject.getString("screen_url");

                    listener.onSuccess(thumb_url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("getCharacterImagesByGame  onErrorResponse called");


                error.printStackTrace();

            }
        });

        queue.add(request);

        //queue.start();

    }



    private String createStringForSearchRequest(String gameName){

        System.out.println("createStringForSearchRequest called");

        String baseUrl = "https://www.giantbomb.com/api/search";

        return baseUrl + "?api_key=" + API_KEY + "&format=json&query=" + gameName + "&resources=game";

    }

    private String createStringForGameRequest(String gameGuid){

        System.out.println("createStringForGameRequest called");

        String baseUrl = "https://www.giantbomb.com/api/game/";

        return baseUrl + gameGuid + "/?api_key=" + API_KEY + "&format=json&";

    }

    private String createStringForCharacterRequest(String characterGuid){

        System.out.println("createStringForCharacterRequest called");

        String baseUrl = "https://www.giantbomb.com/api/character/";

        return baseUrl + characterGuid + "/?api_key=" + API_KEY + "&format=json&";

    }



    void createGameItem(JSONObject rootObject,Listener listener){

        try {
            JSONObject resultObject = rootObject.getJSONObject("results");

            JSONObject imageObject = resultObject.getJSONObject("image");

            JSONArray charactersArray = resultObject.getJSONArray("characters");


         //   String gameId = resultObject.getString("guide");
          //  String gameName = resultObject.getString("name");
          //  String gameImageUrl = imageObject.getString("thumb_url");

            System.out.println();
            for(int i = 0; i < charactersArray.length(); i++){
                JSONObject characterObject = charactersArray.getJSONObject(i);
                String charcarterGuid = "3005-" + characterObject.getString("id");
                getCharactersRequest(charcarterGuid,listener);            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createGameItemList(JSONObject rootObject, List<GameData> gameDataList){

        try {
            JSONArray resultArray =rootObject.getJSONArray("results");

            for (int i = 0; i < resultArray.length(); i++){

                JSONObject jsonObject = resultArray.getJSONObject(i);
                JSONObject imageObject = jsonObject.getJSONObject("image");

                String gameId = jsonObject.getString("guid");
                String gameName = jsonObject.getString("name");
               // String gameImageUrl = imageObject.getString("thumb_url");
                String gameImageUrl = imageObject.getString("small_url");
                Log.d("DataLoader","gameImageUrl = " + gameImageUrl);

                GameData gameData = new GameData(gameId,gameName,gameImageUrl);
                gameDataList.add(gameData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
