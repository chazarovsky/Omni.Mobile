package net.omnidf.omnidf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;

import net.omnidf.omnidf.Const;
import net.omnidf.omnidf.adapters.DirectionsAdapter;
import net.omnidf.omnidf.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class DirectionsFragment extends Fragment {
    RecyclerView DirectionsRecyclerView;
    RequestQueue httpRequestQueue;

    public DirectionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflatedDirectionsFragment = inflater.inflate(R.layout.fragment_directions, container, false);

        httpRequestQueue = Volley.newRequestQueue(getActivity());

        DirectionsRecyclerView = (RecyclerView) inflatedDirectionsFragment.findViewById(R.id.DirectionsRecyclerView);
        DirectionsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DirectionsRecyclerView.setLayoutManager(linearLayoutManager);

        httpRequestQueue.add(setupDirectionsRecyclerViewByGeoJsonRequest());

        return inflatedDirectionsFragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        httpRequestQueue.stop();
    }

    // Helper Methods

    private String getUrlRequest(){
        return Const.DIRECTIONS_URL + getArguments().getString("destination", "Insurgentes");
    }

    private JsonObjectRequest setupDirectionsRecyclerViewByGeoJsonRequest(){
        return new JsonObjectRequest(Request.Method.GET, getUrlRequest(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonDoc = null;
                        try {
                            jsonDoc = response.getJSONObject("content")
                                    .getJSONObject("route_nodes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        FeatureCollection geoJson = (FeatureCollection) GeoJSON.parse(jsonDoc);
                        List<Feature> Directions = geoJson.getFeatures();

                        DirectionsAdapter directionsAdapter = new DirectionsAdapter(Directions);
                        DirectionsRecyclerView.setAdapter(directionsAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("Volley Request ERROR", error.toString());
            }
        });
    }
}
