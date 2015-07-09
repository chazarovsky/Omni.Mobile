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

import net.omnidf.omnidf.fragments.DirectionsFragment;
import net.omnidf.omnidf.fragments.RouteSearchFragment;
import net.omnidf.omnidf.geolocation.Constants;
import net.omnidf.omnidf.geolocation.FetchLocationIntentService;
import net.omnidf.omnidf.rest.OmniServer;


public class RoutesSearchActivity extends AppCompatActivity implements
        RouteSearchFragment.RouteSearchListeners,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    protected static final String TAG = "routes-search-activity";
    protected GoogleApiClient googleApiClient;
    protected Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_util);

        setUpToolbar();

        if (savedInstanceState == null) {
            startFragment(new RouteSearchFragment(), false);
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
        // ToDo: implement method when field in case of the user doesn't have gps is integrated
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
    public void startFetchLocationService(RouteSearchFragment.GeocodedResultReceiver geocodedResultReceiver,
                                          String addressData) {
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, geocodedResultReceiver);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, addressData);
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
        tryToEnsureOriginCanBeObtained();
    }

    @Override
    public void onConnectionSuspended(int i) {
        showToast(R.string.connection_suspended);
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        showToast(R.string.service_not_available);
    }

    // Helper Methods

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
    }

    private void startFragment(Fragment fragment, boolean isAddedToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        if (isAddedToBackStack)
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

    private void tryToEnsureOriginCanBeObtained() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation == null) {
            if (!Geocoder.isPresent()) {
                showToast(R.string.service_not_available);
                return;
            }

            // ToDo: myOriginEditText.setVisibility(View.VISIBLE);
        }
    }
}
