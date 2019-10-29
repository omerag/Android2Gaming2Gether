package hit.android2.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import hit.android2.Helpers.NotificationHelper;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notificationBuilder = notificationHelper.getChannelNotification();
        notificationBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        notificationBuilder.setAutoCancel(true);
        notificationHelper.getManager().notify(1, notificationBuilder.build());
    }
}
