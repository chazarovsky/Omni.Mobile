package net.omnidf.omnidf;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import net.omnidf.omnidf.fragments.CheapDirectionsFragment;
import net.omnidf.omnidf.fragments.FastDirectionsFragment;
import net.omnidf.omnidf.fragments.FragmentPagerRoutes;
import net.omnidf.omnidf.geolocation.Constants;
import net.omnidf.omnidf.geolocation.FetchLocationIntentService;
import net.omnidf.omnidf.rest.OmniServer;


public class RoutesSearchActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

//    private GeocodedResultReceiver geocodedResultReceiver;

    protected static final String TAG = "routes-search-activity";
//    protected GoogleApiClient googleApiClient;
//    protected Location lastLocation;

//    RequestQueue httpRequestQueue;
    View rootView;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView imagePencil;
    ImageView imageMetro;
    ImageView imageBus;
    ImageView imageWalk;
    ImageView imageTaxi;
    ImageView imageBike;
    RelativeLayout relativeProgessBar;
    TextView textProgressBar;
    String userOrigin;
    String userDestination;
    Intent homeScreenActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes_search);
        homeScreenActivityIntent = getIntent();
        //httpRequestQueue = Volley.newRequestQueue(this);
        //geocodedResultReceiver = new GeocodedResultReceiver(new Handler());
        setUpToolbar();
        initViews();
        setUpTabLayout();
        setUpPencilImageClickListener();
        setClickListenersTransportImages();
        //buildGoogleApiClient();
        beginDummyProgessBar();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        httpRequestQueue.start();
//        googleApiClient.connect();
        if (homeScreenActivityIntent != null) {
            processIntentData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (googleApiClient.isConnected()) {
//            googleApiClient.disconnect();
//        }
//        httpRequestQueue.stop();
    }

    // GoogleApiClient Callbacks

    @Override
    public void onConnected(Bundle bundle) {
//        tryToEnsureOriginCanBeObtained();
    }

    @Override
    public void onConnectionSuspended(int i) {
        showSnackbar(R.string.connection_suspended);
//        googleApiClient.connect();
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

    private void setTitleToolbar(String origin, String destination){
        TextView titleToolbar = (TextView) findViewById(R.id.titleToolbar);
        titleToolbar.setText(origin + " - " + destination);
    }

    private void initViews() {
        rootView = findViewById(R.id.coordinator_main);
        relativeProgessBar = (RelativeLayout) findViewById(R.id.relativeProgessBar);
        textProgressBar = (TextView) findViewById(R.id.textProgressBar);
        imagePencil = (ImageView) findViewById(R.id.imagePencil);
        imageMetro = (ImageView) findViewById(R.id.imageMetro);
        imageBus = (ImageView) findViewById(R.id.imageBus);
        imageWalk = (ImageView) findViewById(R.id.imageWalk);
        imageTaxi = (ImageView) findViewById(R.id.imageTaxi);
        imageBike = (ImageView) findViewById(R.id.imageBike);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.fragmentContainer);
    }

    private void setUpTabLayout(){
        FragmentPagerRoutes fragmentPagerRoutes = new FragmentPagerRoutes(getSupportFragmentManager());
        fragmentPagerRoutes.addFragment(new CheapDirectionsFragment(), "");
        fragmentPagerRoutes.addFragment(new FastDirectionsFragment(), "");
        viewPager.setAdapter(fragmentPagerRoutes);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_cheap_blue);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_fast_white);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tab.setIcon(R.drawable.ic_cheap_blue);
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_fast_blue);
                        viewPager.setCurrentItem(1);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tab.setIcon(R.drawable.ic_cheap_white);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_fast_white);
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        tab.setIcon(R.drawable.ic_cheap_blue);
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.ic_fast_blue);
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
    }

    private void setUpPencilImageClickListener(){
        imagePencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setClickListenersTransportImages(){
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = (ImageView) findViewById(v.getId());
                if (imageView.getTag() == true){
                    imageView.setColorFilter(Color.argb(1, 145, 145, 145));
                    imageView.setTag(false);
                } else {
                    imageView.setColorFilter(Color.argb(255, 255, 255, 255));
                    imageView.setTag(true);
                }
            }
        };

        imageMetro.setTag(false);
        imageMetro.setOnClickListener(clickListener);
        imageBus.setTag(false);
        imageBus.setOnClickListener(clickListener);
        imageWalk.setTag(false);
        imageWalk.setOnClickListener(clickListener);
        imageTaxi.setTag(false);
        imageTaxi.setOnClickListener(clickListener);
        imageBike.setTag(false);
        imageBike.setOnClickListener(clickListener);
    }

//    protected synchronized void buildGoogleApiClient() {
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//    }

    private void beginDummyProgessBar(){
        new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished < 3000)
                    textProgressBar.setText("Calculando rutas...");
            }

            @Override
            public void onFinish() {
                relativeProgessBar.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    private void processIntentData() {
        userOrigin = homeScreenActivityIntent.getStringExtra(OmniServer.ORIGIN_QUERY);
        userDestination = homeScreenActivityIntent.getStringExtra(OmniServer.DESTINATION_QUERY);
        setTitleToolbar(userOrigin, userDestination);
    }

    private void startFragment(Fragment fragment, boolean shouldAddToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.fragmentContainer, fragment);
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

//    private void tryToEnsureOriginCanBeObtained() {
//        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//
//        if (lastLocation == null) {
//            if (!Geocoder.isPresent()) {
//                showSnackbar(R.string.service_not_available);
//                return;
//            }
//        }
//    }

    private void startFetchLocationService(GeocodedResultReceiver geocodedResultReceiver,
                                           String addressData) {
        Intent intent = new Intent(this, FetchLocationIntentService.class);
        intent.putExtra(Constants.RECEIVER, geocodedResultReceiver);
        intent.putExtra(Constants.ADDRESS_DATA_EXTRA, addressData);
        startService(intent);
    }

    /* private void onGetRoute(Address userDestination) {
        DirectionsFragment directionsFragment = new DirectionsFragment();
        Bundle args = new Bundle();
        args.putParcelable(OmniServer.ORIGIN_QUERY, lastLocation);
        args.putParcelable(OmniServer.DESTINATION_QUERY, userDestination);
        directionsFragment.setArguments(args);
        startFragment(directionsFragment, true);
    } */

    /* private void onGetRoute(String userOrigin, String userDestination) {
        DirectionsFragment directionsFragment = new DirectionsFragment();
        Bundle args = new Bundle();
        args.putString(OmniServer.ORIGIN_QUERY, userOrigin);
        args.putString(OmniServer.DESTINATION_QUERY, userDestination);
        directionsFragment.setArguments(args);
        startFragment(directionsFragment, true);
    } */


    /* @Override
    public void addToHttpQueue(Request request) {
        httpRequestQueue.add(request);
    } */


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
                //onGetRoute(result);
            }
        }
    }
}
