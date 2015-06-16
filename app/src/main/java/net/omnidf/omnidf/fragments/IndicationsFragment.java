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
import net.omnidf.omnidf.adapters.IndicationsAdapter;
import net.omnidf.omnidf.R;

import org.json.JSONObject;
import java.util.List;

public class IndicationsFragment extends Fragment {
    RecyclerView indicationsRecyclerView;
    IndicationsAdapter indicationsAdapter;
    List<Feature> indications;
    RequestQueue httpRequestQueue;

    public IndicationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indications, container, false);

        httpRequestQueue = Volley.newRequestQueue(getActivity());

        indicationsRecyclerView = (RecyclerView) view.findViewById(R.id.indicationsRecyclerView);
        indicationsRecyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        indicationsRecyclerView.setLayoutManager(linearLayoutManager);

        JsonObjectRequest indicationsJsonRequest = new JsonObjectRequest(Request.Method.GET, getUrlRequest(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonresponse = null;
                            jsonresponse = response.optJSONObject("content")
                                    .optJSONObject("route_nodes");

                        FeatureCollection geoJson = (FeatureCollection) GeoJSON.parse(jsonresponse);
                        indications = geoJson.getFeatures();

                        IndicationsAdapter indicationsAdapter = new IndicationsAdapter(indications);
                        indicationsRecyclerView.setAdapter(indicationsAdapter);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("Volley Request ERROR", error.toString());
            }
        });

        httpRequestQueue.add(indicationsJsonRequest);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        httpRequestQueue.stop();
    }

    public String getUrlRequest(){
        return Const.URL_INDICATIONS + getArguments().getString("destination", "Insurgentes");
    }
}
