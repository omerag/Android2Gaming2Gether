package hit.android2.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {

    int timeToUpdate;

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("AlarmReceiver called");

        timeToUpdate = intent.getIntExtra("time_to_update",0);




        myStartService(context);
    }

    private void myStartService(Context context){

        Intent intent = new Intent(context,BackroundUpdatesService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }
        else {
            context.startService(intent);
        }

    }
}
