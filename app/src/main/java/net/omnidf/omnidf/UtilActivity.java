package net.omnidf.omnidf;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.omnidf.omnidf.R;
import net.omnidf.omnidf.fragments.DirectionsFragment;
import net.omnidf.omnidf.fragments.SearchRouteFragment;
import net.omnidf.omnidf.geolocation.Constants;
import net.omnidf.omnidf.geolocation.FetchLocationIntentService;
import net.omnidf.omnidf.rest.OmniServer;


public class UtilActivity extends AppCompatActivity implements
        SearchRouteFragment.searchRouteListeners,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "main-activity";
    protected GoogleApiClient googleApiClient;
    protected Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }

        if (savedInstanceState == null) {
            startFragment(new SearchRouteFragment(), false);
        }

        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onGetRoute(Location userOrigin, Address userDestination) {

    }

    @Override
    public void onGetRoute(Address userDestination) {
        DirectionsFragment directionsFragment = new DirectionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(OmniServer.ORIGIN_QUERY, lastLocation);
        args.putParcelable(OmniServer.DESTINATION_QUERY, userDestination);
        directionsFragment.setArguments(args);
        startFragment(directionsFragment, true);
    }

    @Override
    public void startFetchLocationService(SearchRouteFragment.GeocodedResultReciver geocodedResultReceiver,
                                          String adressData) {
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, geocodedResultReceiver);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, adressData);
        startService(intent);
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(int stringResourceId) {
        Toast.makeText(this, stringResourceId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnected(Bundle bundle) {

        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation != null) {
            if (!Geocoder.isPresent()) {
                showToast(R.string.service_not_available);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    // Helper Methods

    private void startFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}
