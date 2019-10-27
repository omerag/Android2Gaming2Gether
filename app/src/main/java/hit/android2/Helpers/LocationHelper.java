package hit.android2.Helpers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import ru.alexbykov.nopermission.PermissionHelper;

import static androidx.core.app.ActivityCompat.requestPermissions;

public class LocationHelper {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private Listener listener;
    private Activity activity;
    private Fragment fragment;

    private PermissionHelper permissionHelper;
    public interface Listener<Double>{
        void onSuccess(double latitude, double longitude);
    }

    public LocationHelper(Activity activity, PermissionHelper permissionHelper, Listener<Double> listener) {
        this.listener = listener;
        this.activity = activity;
        this.fragment = fragment;
        this.permissionHelper = permissionHelper;

        getLocation();
    }

    public void getLocation() {

        System.out.println("getting location...");

        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(activity);

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);
        request.setFastestInterval(500);
        request.setNumUpdates(1);
        LocationCallback callback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                System.out.println("getting location Results...");

                Location location = locationResult.getLastLocation();

                listener.onSuccess(location.getLatitude(),location.getLongitude());

            }
        };

        permissionHelper.check(Manifest.permission.ACCESS_COARSE_LOCATION)
                .onSuccess(this::onSuccess)
                .onDenied(this::onDenied)
                .onNeverAskAgain(this::onNeverAskAgain)
                .run();


      /*  if (Build.VERSION.SDK_INT >= 23) {
            if(activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                System.out.println("sending request for location");
                client.requestLocationUpdates(request, callback, null);
            }
            else {
                requestPermissions
                        (activity, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}
                        ,LOCATION_PERMISSION_REQUEST);
                Log.d("LocationHelper", "requestPermissions");
            }

        }
        else if(Build.VERSION.SDK_INT<=22)
            client.requestLocationUpdates(request,callback,null);
        else {

            System.out.println("no location permissions");
        }*/



    }

    private void onNeverAskAgain() {
        Log.d("locationHelper", "PermissionHelper - onNeverAskAgain");
        permissionHelper.startApplicationSettingsActivity();


    }

    private void onDenied() {
        Log.d("locationHelper", "PermissionHelper - onDenied");

    }

    private void onSuccess() {
        Log.d("locationHelper", "PermissionHelper - onSuccess");
    }
}
