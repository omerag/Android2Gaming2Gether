package hit.android2.Helpers;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    private Listener listener;
    private Context context;

    public interface Listener<Double>{
        void onSuccess(double latitude, double longitude);
    }

    public LocationHelper(Context context, Listener<Double> listener) {
        this.listener = listener;
        this.context = context;

        getLocation();
    }

    public void getLocation() {

        System.out.println("getting location...");

        final FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

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


        if (Build.VERSION.SDK_INT >= 23 && context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            System.out.println("sending request for location");
            client.requestLocationUpdates(request, callback, null);
        }
        else if(Build.VERSION.SDK_INT<=22)
            client.requestLocationUpdates(request,callback,null);
        else {

            System.out.println("no location permissions");
        }

    }
}
