package hit.android2.Database.Managers;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import hit.android2.Database.CommentDataHolder;
import hit.android2.Database.TopicDataHolder;

public class MessegingManager {


    public static void notifyNewCommentOnTopic(Context context, TopicDataHolder topic, CommentDataHolder comment){

        //////////////////////////////////////////////

        RequestQueue mRequestQue = Volley.newRequestQueue(context);

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + topic.getTopicId());
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", topic.getTitle());
            notificationObj.put("body", "new comment from " + comment.getUserName()
            + " on " + topic.getTitle());
            //replace notification with data when went send data
            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("MUR", "onError: " + error.networkResponse);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA3uKVSnc:APA91bHTMIXxTmAa30Zaibc_P4idwYmbOKpFRnvEFBuZTE9NvMGSGVZTVQilOcZXozWWL5IM_XV6TjjH4tfr3jTn4VLNbfTWL6YTAeIfyayoNcSJjtUVaALg_SK92ZpiDe0alwnL-ms5");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /////////////////////////////////////////////

    }

    public static void notifyNewMessegInGroup(Context context,String groupId ,String groupName, String fromUser, String massage){

        //////////////////////////////////////////////

        RequestQueue mRequestQue = Volley.newRequestQueue(context);

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + groupId);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", groupName);
            notificationObj.put("body", fromUser + " say - " + massage);
            //replace notification with data when went send data
            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("MUR", "onError: " + error.networkResponse);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA3uKVSnc:APA91bHTMIXxTmAa30Zaibc_P4idwYmbOKpFRnvEFBuZTE9NvMGSGVZTVQilOcZXozWWL5IM_XV6TjjH4tfr3jTn4VLNbfTWL6YTAeIfyayoNcSJjtUVaALg_SK92ZpiDe0alwnL-ms5");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /////////////////////////////////////////////

    }

    public static void notifyNewMessegInChat(Context context,String fromUser ,String toUser, String massage){

        //////////////////////////////////////////////

        RequestQueue mRequestQue = Volley.newRequestQueue(context);

        JSONObject json = new JSONObject();
        try {
            json.put("to", "/topics/" + toUser);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", fromUser);
            notificationObj.put("body", fromUser + " say - " + massage);
            //replace notification with data when went send data
            json.put("notification", notificationObj);

            String URL = "https://fcm.googleapis.com/fcm/send";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("MUR", "onResponse: ");
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("MUR", "onError: " + error.networkResponse);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAA3uKVSnc:APA91bHTMIXxTmAa30Zaibc_P4idwYmbOKpFRnvEFBuZTE9NvMGSGVZTVQilOcZXozWWL5IM_XV6TjjH4tfr3jTn4VLNbfTWL6YTAeIfyayoNcSJjtUVaALg_SK92ZpiDe0alwnL-ms5");
                    return header;
                }
            };


            mRequestQue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /////////////////////////////////////////////

    }

    public static void subscribeToTopic(String topicId){
        FirebaseMessaging.getInstance().subscribeToTopic(topicId);
    }
    public static void unsubscribeFromTopic(String topicId){
        FirebaseMessaging.getInstance().unsubscribeFromTopic(topicId);
    }

    public static void registerReceiver(Context context, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver,new IntentFilter("message_received"));

    }

    public static void unRegisterReciver(Context context, BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }



}
