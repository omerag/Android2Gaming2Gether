package hit.android2.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentText("alarm time is up wake up!!!!").setContentTitle("Alram clock!")
                .setSmallIcon(android.R.drawable.star_on);

        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT>=26) {
            NotificationChannel channel = new NotificationChannel("id_1", "Alarm channel", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
            builder.setChannelId("id_1");
        }




        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_ALL;
        notification.flags |= Notification.FLAG_INSISTENT;

        manager.notify(1,notification);
    }
}