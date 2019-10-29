package hit.android2.Helpers;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;



import hit.android2.MainActivity;
import hit.android2.R;


public class NotificationTopicDispatch {

    private Context context;
    private String channelId = "topic channel";
    private String channelName = "topic";

    private final int NOTIF_REQUEST_CODE = 1;


    public NotificationTopicDispatch(Context context) {

        System.out.println("notif topic dispatch created!");


        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,channelId);

   /*//     final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_topic_layout);





        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,NOTIF_REQUEST_CODE,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.notification_topic_layout,pendingIntent);

        builder.setSmallIcon(R.drawable.ic_small_news)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(remoteViews);

        manager.notify(1,builder.build());

        System.out.println("notif topic send???");*/
    }




}
