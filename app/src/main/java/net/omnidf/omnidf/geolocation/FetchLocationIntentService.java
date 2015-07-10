package net.omnidf.omnidf.geolocation;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import net.omnidf.omnidf.R;

import java.io.IOException;
import java.util.List;

public class FetchLocationIntentService extends IntentService {
    private static final String TAG = "fetch-location-intent-service";
    protected ResultReceiver locationReceiver;

    public FetchLocationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        this.locationReceiver = intent.getParcelableExtra(Constants.RECEIVER);
        String address = intent.getStringExtra(Constants.ADDRESS_DATA_EXTRA);

        Geocoder geocoder = new Geocoder(this);

        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(address, 1,
                    Constants.SEARCH_LIMIT_LOWER_LEFT_LATITUDE,
                    Constants.SEARCH_LIMIT_LOWER_LEFT_LONGITUDE,
                    Constants.SEARCH_LIMIT_UPPER_RIGHT_LATITUDE,
                    Constants.SEARCH_LIMIT_UPPER_RIGHT_LONGITUDE);

        } catch (IOException ioException) {
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = getString(R.string.invalid_lat_long_used);
            Log.e(TAG, errorMessage + ". " +
                    "Address = " + address);
        }

        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
        } else {
            Address location = addresses.get(0);
            Log.i(TAG, getString(R.string.address_found));
            deliverResultToReceiver(Constants.SUCCESS_RESULT, location);
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        locationReceiver.send(resultCode, bundle);
    }

    private void deliverResultToReceiver(int resultCode, Address location) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RESULT_DATA_KEY, location);
        locationReceiver.send(resultCode, bundle);
    }
}
