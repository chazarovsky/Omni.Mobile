package net.omnidf.omnidf.fragments;

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

    public DirectionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View directionsFragmentView = inflater.inflate(R.layout.fragment_directions, container, false);

        httpRequestQueue = Volley.newRequestQueue(getActivity());

        directionsRecyclerView = (RecyclerView) directionsFragmentView.findViewById(R.id.directionsRecyclerView);
        directionsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        directionsRecyclerView.setLayoutManager(linearLayoutManager);

        Request directionsRequest = new OmniServer.RequestBuilder(getActivity())
                .fetchIndications("Ermita", getArguments().getString("destination")) //TODO get real user origin
                .intoRecyclerView(directionsRecyclerView).build();
        httpRequestQueue.add(directionsRequest);

        return directionsFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        httpRequestQueue.stop();
    }

}
