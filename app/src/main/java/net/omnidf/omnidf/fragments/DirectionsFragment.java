/* package net.omnidf.omnidf.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import net.omnidf.omnidf.R;
import net.omnidf.omnidf.rest.OmniServer;

public class DirectionsFragment extends Fragment {
    HttpQueueListener httpQueueCallback;
    RecyclerView directionsRecyclerView;
    Context RoutesSearchActivity;
    Bundle fragmentArgs;
    //temporal DEMO-MODE
    //Location userOrigin;
    //Address userDestination;
    String userOrigin;
    String userDestination;

    public interface HttpQueueListener {
        void addToHttpQueue(Request request);
    }

    public DirectionsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        RoutesSearchActivity = activity;
        httpQueueCallback = (HttpQueueListener) activity;
        //temporal DEMO-MODE
        //getLocationsArgs();
        getStringArgs();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View directionsFragmentView = inflater.inflate(R.layout.fragment_directions, container, false);

        setupRecyclerView(directionsFragmentView);
        Request directionsRequest = new OmniServer.RequestBuilder(RoutesSearchActivity)
                .fetchDirections(userOrigin, userDestination)
                .intoRecyclerView(directionsRecyclerView).build();
        httpQueueCallback.addToHttpQueue(directionsRequest);

        return directionsFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //Helper Methods
    private void setupRecyclerView(View fragmentView) {
        directionsRecyclerView = (RecyclerView) fragmentView.findViewById(R.id.directionsRecyclerView);
        directionsRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(RoutesSearchActivity);
        directionsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void getLocationsArgs() {
        fragmentArgs = getArguments();
        userOrigin = fragmentArgs.getParcelable(OmniServer.ORIGIN_QUERY);
        userDestination = fragmentArgs.getParcelable(OmniServer.DESTINATION_QUERY);
    }

    //temporal DEMO-MODE
    private void getStringArgs() {
        fragmentArgs = getArguments();
        userOrigin = fragmentArgs.getString(OmniServer.ORIGIN_QUERY);
        userDestination = fragmentArgs.getString(OmniServer.DESTINATION_QUERY);
    }

} */
