/*package net.omnidf.omnidf.fragments;

import android.app.Activity;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.omnidf.omnidf.R;
import net.omnidf.omnidf.geolocation.Constants;

public class RouteSearchFragment extends Fragment {
    private GeocodedResultReceiver geocodedResultReceiver;
    EditText inputOrigin;
    EditText inputDestination;
    Button buttonGetRoute;
    protected RouteSearchListeners routeSearchCallback;

    public RouteSearchFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        routeSearchCallback = (RouteSearchListeners) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentSearchRouteView = inflater.
                inflate(R.layout.fragment_route_search, container, false);

        geocodedResultReceiver = new GeocodedResultReceiver(new Handler());

        initViews(fragmentSearchRouteView);
        setupEmptyFieldsWatcher();

        buttonGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routeSearchCallback.startFetchLocationService(geocodedResultReceiver,
                        inputDestination.getText().toString());
            }
        });

        return fragmentSearchRouteView;
    }

    // Helper Methods

    private void initViews(View fragmentSearchRouteView) {
        inputDestination = (EditText) fragmentSearchRouteView.findViewById(R.id.inputDestination);
        inputOrigin = (EditText) fragmentSearchRouteView.findViewById(R.id.inputOrigin);
        buttonGetRoute = (Button) fragmentSearchRouteView.findViewById(R.id.fabGetRoute);
    }

    private void setupEmptyFieldsWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                boolean isFill = inputOrigin.getText().toString().length() > 3
                        && inputDestination.getText().toString().length() > 3;
                buttonGetRoute.setEnabled(isFill);
                buttonGetRoute.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        inputOrigin.addTextChangedListener(textWatcher);
        inputDestination.addTextChangedListener(textWatcher);
    }

    public class GeocodedResultReceiver extends ResultReceiver {
        public GeocodedResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.FAILURE_RESULT) {
                String result = resultData.getString(Constants.RESULT_DATA_KEY);
                routeSearchCallback.showToast(result);
            } else {
                Address result = resultData.getParcelable(Constants.RESULT_DATA_KEY);
                routeSearchCallback.onGetRoute(result);
            }
        }
    }
} */
