package net.omnidf.omnidf;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.omnidf.omnidf.fragments.DirectionsFragment;
import net.omnidf.omnidf.geolocation.Constants;
import net.omnidf.omnidf.geolocation.FetchLocationIntentService;
import net.omnidf.omnidf.rest.OmniServer;


public class RoutesSearchActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DirectionsFragment.HttpQueueListener {

    private GeocodedResultReceiver geocodedResultReceiver;

    protected static final String TAG = "routes-search-activity";
    protected GoogleApiClient googleApiClient;
    protected Location lastLocation;

    RequestQueue httpRequestQueue;
    View rootView;
    EditText inputOrigin;
    EditText inputDestination;
    FloatingActionButton fabGetRoute;
    Intent homeScreenActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_search);
        homeScreenActivityIntent = getIntent();
        httpRequestQueue = Volley.newRequestQueue(this);
        geocodedResultReceiver = new GeocodedResultReceiver(new Handler());
        setUpToolbar();
        initViews();
        setUpGetRouteFab();
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        httpRequestQueue.start();
        googleApiClient.connect();
        if (homeScreenActivityIntent != null) {
            processIntentData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        httpRequestQueue.stop();
    }

    // GoogleApiClient Callbacks

    @Override
    public void onConnected(Bundle bundle) {
        tryToEnsureOriginCanBeObtained();
    }

    @Override
    public void onConnectionSuspended(int i) {
        showSnackbar(R.string.connection_suspended);
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        showSnackbar(R.string.service_not_available);
    }

    // Helper Methods

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
    }

    private void initViews() {
        rootView = findViewById(R.id.coordinator_main);
        inputDestination = (EditText) findViewById(R.id.inputDestination);
        inputOrigin = (EditText) findViewById(R.id.inputOrigin);
        fabGetRoute = (FloatingActionButton) findViewById(R.id.fabGetRoute);
    }

    private void setUpGetRouteFab() {
        fabGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Temporal DEMO-MODE
                // startFetchLocationService(geocodedResultReceiver,
                //inputDestination.getText().toString());
                onGetRoute(inputOrigin.getText().toString(),
                        inputDestination.getText().toString());
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void processIntentData() {
        String origin = homeScreenActivityIntent.getStringExtra(OmniServer.ORIGIN_QUERY);
        String destination = homeScreenActivityIntent.getStringExtra(OmniServer.DESTINATION_QUERY);
        inputOrigin.setText(origin);
        inputDestination.setText(destination);
        onGetRoute(origin, destination);
    }

    private void startFragment(Fragment fragment, boolean shouldAddToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        if (shouldAddToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showSnackbar(String text) {
        Snackbar.make(rootView, text, Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackbar(int stringResourceId) {
        Snackbar.make(rootView, stringResourceId, Snackbar.LENGTH_SHORT).show();
    }

    private void tryToEnsureOriginCanBeObtained() {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (lastLocation == null) {
            if (!Geocoder.isPresent()) {
                showSnackbar(R.string.service_not_available);
                return;
            }

            // ToDo: myOriginEditText.setVisibility(View.VISIBLE);
        }
    }

    private void startFetchLocationService(GeocodedResultReceiver geocodedResultReceiver,
                                           String addressData) {
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, geocodedResultReceiver);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, addressData);
        startService(intent);
    }

    private void onGetRoute(Address userDestination) {
        DirectionsFragment directionsFragment = new DirectionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(OmniServer.ORIGIN_QUERY, lastLocation);
        args.putParcelable(OmniServer.DESTINATION_QUERY, userDestination);
        directionsFragment.setArguments(args);
        startFragment(directionsFragment, false);
    }

    private void onGetRoute(String userOrigin, String userDestination) {
        DirectionsFragment directionsFragment = new DirectionsFragment();
        Bundle args = new Bundle();
        args.putString(OmniServer.ORIGIN_QUERY, userOrigin);
        args.putString(OmniServer.DESTINATION_QUERY, userDestination);
        directionsFragment.setArguments(args);
        startFragment(directionsFragment, false);
    }


    @Override
    public void addToQueue(Request request) {
        httpRequestQueue.add(request);
    }

    public class GeocodedResultReceiver extends ResultReceiver {
        public GeocodedResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.FAILURE_RESULT) {
                String result = resultData.getString(Constants.RESULT_DATA_KEY);
                showSnackbar(result);
            } else {
                Address result = resultData.getParcelable(Constants.RESULT_DATA_KEY);
                onGetRoute(result);
            }
        }
    }
}
