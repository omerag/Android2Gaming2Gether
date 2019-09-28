package hit.android2.gaintbomb.api;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hit.android2.gaintbomb.game.GameAdapter;
import hit.android2.gaintbomb.game.GameItem;

public class DataLoader {

    private String API_KEY;

    private GameItem gameItem = null;

    private Context context;

    private GameAdapter gameAdapter;

    private List<GameItem> gameItemList = null;

    public DataLoader(String API_KEY, Context context, List<GameItem> gameItemList, GameAdapter gameAdapter) {
        this.API_KEY = API_KEY;
        this.context = context;
        this.gameItemList = gameItemList;
        this.gameAdapter = gameAdapter;

        System.out.println("DataLoader created");

    }

    public void searchGameRequest(final String gameName){

        System.out.println("searchGameRequest called");


        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(createStringForRequest(gameName), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                System.out.println("searchGameRequest  onResponse called");


                try {
                    JSONObject rootObject = new JSONObject(response);
                    createGameItemList(rootObject,gameItemList);
                    gameAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    private String createStringForRequest(String gameName){

        System.out.println("createStringForRequest called");

        String baseUrl = "https://www.giantbomb.com/api/search";

        return baseUrl + "?api_key=" + API_KEY + "&format=json&query=" + gameName + "&resources=game";

    }



    void createGameItem(JSONObject rootObject){

        try {
            JSONObject resultObject = rootObject.getJSONObject("result");

            JSONObject imageObject = resultObject.getJSONObject("image");

            String gameId = resultObject.getString("guide");
            String gameName = resultObject.getString("name");
            String gameImageUrl = imageObject.getString("thumb_url");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createGameItemList(JSONObject rootObject, List<GameItem> gameItemList){

        try {
            JSONArray resultArray =rootObject.getJSONArray("results");

            for (int i = 0; i < resultArray.length(); i++){

                JSONObject jsonObject = resultArray.getJSONObject(i);
                JSONObject imageObject = jsonObject.getJSONObject("image");

                String gameId = jsonObject.getString("guid");
                String gameName = jsonObject.getString("name");
                String gameImageUrl = imageObject.getString("thumb_url");

                GameItem gameItem = new GameItem(gameId,gameName,gameImageUrl);
                gameItemList.add(gameItem);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}