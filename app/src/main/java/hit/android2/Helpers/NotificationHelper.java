package hit.android2.Helpers;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import hit.android2.MainActivity;
import hit.android2.R;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";

    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.putExtra("from_notif",true);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),2,intent, PendingIntent.FLAG_CANCEL_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(getString(R.string.hello_notif))
                .setContentText(getString(R.string.msg_notif))
                .setSmallIcon(android.R.drawable.star_on)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent);

    }
}
