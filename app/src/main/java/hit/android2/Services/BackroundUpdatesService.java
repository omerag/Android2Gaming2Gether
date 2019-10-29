package hit.android2.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;


public class BackroundUpdatesService extends Service {

    private Context context;
    private boolean isUpdateTimeSet = false;
    private int timeUpdateInSeconds = 0;
    private String topic = null;
    private boolean firstNotify = false;

    private boolean isWeatherUpdatable = false;
    private boolean isNewsUpdatable = false;

    private String RSS_link;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        System.out.println("backroundUpdateService onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        System.out.println("backroundUpdateService onStartCommand");

        context = getBaseContext();

        firstNotify = intent.getBooleanExtra("firstNotify",false);

        loadPreferences();

        if(!firstNotify){
            enterForground();
        }



        dispatchUpdate(firstNotify);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        System.out.println("backroundUpdateService onDestroy");

    }

    public void dispatchUpdate(boolean firstNotify){

        System.out.println("dispatchUpdate: second = " + timeUpdateInSeconds);

        if(!isWeatherUpdatable && !isNewsUpdatable){
            System.out.println("no topic and no weather were selected");
            stopSelf();
            return;
        }

/*        if(firstNotify && (seconds == timeUpdateInSeconds || seconds == 0)){
            System.out.println("no time or no change");
            return;
        }*/

        if(timeUpdateInSeconds == 0){
            System.out.println("time is 0 so no update for you");
            stopSelf();
            return;

        }
        else{
            //timeUpdateInSeconds = seconds;
            System.out.println("to die or not die?");

            stopSelf();

            System.out.println("not today");
        }


        System.out.println("All right lets go");

        if(!firstNotify){
            loadUpdates(RSS_link, isNewsUpdatable, isWeatherUpdatable);
        }

        AlarmManager manager = (AlarmManager)context.getSystemService(ALARM_SERVICE);


        Intent intent = new Intent(context,AlarmReceiver.class);


       // intent.putExtra("topic",topic);
       // intent.putExtra("topic_link",RSS_link);
       // intent.putExtra("isNewsUpdatable", isNewsUpdatable);
        intent.putExtra("isWeatherUpdatable", isWeatherUpdatable);
       // intent.putExtra("time_to_update",timeUpdateInSeconds);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeUpdateInSeconds*1000,pendingIntent);



    }


    private void loadUpdates(String topicLink, boolean isNewsUpdatable,boolean isWeatherUpdatable){




    }

    void enterForground(){

        String channelName = "loading updates";
        String channelId = "loading_updates";



        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }

        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(android.R.drawable.star_on)
                .setContentTitle("Loading Updates...")
                .setPriority(Notification.PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE);

        if(Build.VERSION.SDK_INT >= 26) {
            builder.setChannelId(channelId);
        }



        Notification notification = builder.build();

        notification.defaults = 0;

        startForeground(3,notification);

    }

    private void loadPreferences(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        topic = preferences.getString("list_preferences_topic","All");
        timeUpdateInSeconds = Integer.parseInt(preferences.getString("list_preferences_times","0"));
        isNewsUpdatable = preferences.getBoolean("preference_news_checkbox",false);
        isWeatherUpdatable = preferences.getBoolean("preference_weather_checkbox",false);
    }



}
