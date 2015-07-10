package net.omnidf.omnidf.fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.omnidf.omnidf.R;
import net.omnidf.omnidf.rest.OmniServer;

public class DirectionsFragment extends Fragment {
    RecyclerView directionsRecyclerView;
    RequestQueue httpRequestQueue;
    Context utilActivity;
    Bundle fragmentArgs;
    Location userOrigin;
    Address userDestination;

    public DirectionsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        utilActivity = activity;
        getLocationsArgs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View directionsFragmentView = inflater.inflate(R.layout.fragment_directions, container, false);

        httpRequestQueue = Volley.newRequestQueue(utilActivity);
        setupRecyclerView(directionsFragmentView);
        Request directionsRequest = new OmniServer.RequestBuilder(utilActivity)
                .fetchDirections(userOrigin, userDestination)
                .intoRecyclerView(directionsRecyclerView).build();
        httpRequestQueue.add(directionsRequest);

        return directionsFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        httpRequestQueue.stop();
    }

    //Helper Methods
    private void setupRecyclerView(View fragmentView) {
        directionsRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.directionsRecyclerView);
        directionsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(utilActivity);
        directionsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getLocationsArgs() {
        fragmentArgs = getArguments();
        userOrigin = fragmentArgs.getParcelable(OmniServer.ORIGIN_QUERY);
        userDestination = fragmentArgs.getParcelable(OmniServer.DESTINATION_QUERY);
    }

}
