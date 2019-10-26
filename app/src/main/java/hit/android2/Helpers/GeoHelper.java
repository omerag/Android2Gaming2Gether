package hit.android2.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import hit.android2.Services.GeocodeAddressIntentService;

public class GeoHelper {
    String TAG = "GeoHelper";

    //private EditText addressEdit;
    private TextView infoText;
    private String address;

    private Activity activity;

    private double latitude;
    private double longitude;
    public Listener listener;

    public interface Listener<Double>{
        void onSuccess(double latitude,double longitude);
    }



    private AddressResultReceiver mResultReceiver  = new AddressResultReceiver(null);

    public GeoHelper(Activity activity,TextView infoText, String address, Listener<Double> listener) {
        this.infoText = infoText;
        this.address = address;
        this.activity = activity;
        this.listener = listener;

        initIntent();
    }

    private void initIntent(){
        Intent intent = new Intent(activity, GeocodeAddressIntentService.class);
        intent.putExtra(GeocodeAddressIntentService.Constants.RECEIVER, mResultReceiver);
        intent.putExtra(GeocodeAddressIntentService.Constants.FETCH_TYPE_EXTRA, GeocodeAddressIntentService.Constants.USE_ADDRESS_NAME);
        intent.putExtra(GeocodeAddressIntentService.Constants.LOCATION_NAME_DATA_EXTRA, address);


       // infoText.setVisibility(View.INVISIBLE);
      //  progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "Starting Service");
        activity.startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == GeocodeAddressIntentService.Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(GeocodeAddressIntentService.Constants.RESULT_ADDRESS);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        listener.onSuccess(latitude,longitude);
                        Log.d("GeoHelper","onReciveResult\nLatitude = " + address.getLatitude()
                        + "\nLongitude = " + address.getLongitude());
                        //progressBar.setVisibility(View.GONE);
                        /*infoText.setVisibility(View.VISIBLE);
                        infoText.setText("Latitude: " + address.getLatitude() + "\n" +
                                "Longitude: " + address.getLongitude() + "\n" +
                                "Address: " + resultData.getString(GeocodeAddressIntentService.Constants.RESULT_DATA_KEY));*/
                    }
                });
            }
            else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //progressBar.setVisibility(View.GONE);
                        Log.d("GeoHelper","onReciveResult - failed");
                        //infoText.setVisibility(View.VISIBLE);
                      //  infoText.setText(resultData.getString(GeocodeAddressIntentService.Constants.RESULT_DATA_KEY));
                    }
                });
            }
        }
    }
}
