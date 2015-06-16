package net.omnidf.omnidf.fragments;

import android.content.Context;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;
import com.cocoahero.android.geojson.GeoJSONObject;

import net.omnidf.omnidf.Const;
import net.omnidf.omnidf.pojos.Features;
import net.omnidf.omnidf.adapters.IndicationsAdapter;
import net.omnidf.omnidf.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IndicationsFragment extends Fragment {

    Context utilActivityContext = getActivity();
    RecyclerView indicationsRecyclerView;
    IndicationsAdapter indicationsAdapter;
    ArrayList<GeoJSONObject> indications = new ArrayList<>();
    RequestQueue httpRequestQueue;

    public IndicationsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        httpRequestQueue = Volley.newRequestQueue(utilActivityContext);

        indicationsRecyclerView = (RecyclerView) container.findViewById(R.id.indicationsRecyclerView);
        indicationsRecyclerView.setHasFixedSize(true);

        indicationsAdapter = new IndicationsAdapter(indications);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(utilActivityContext);

        indicationsRecyclerView.setLayoutManager(linearLayoutManager);
        indicationsRecyclerView.setAdapter(indicationsAdapter);

        JsonObjectRequest indicationsJsonRequest = new JsonObjectRequest(Request.Method.GET, Const.URL_INDICATIONS,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONObject jsonresponse = null;
                        try {
                            jsonresponse = response.getJSONObject("content")
                                    .getJSONObject("route_nodes");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        GeoJSONObject geoJSON = GeoJSON.parse(jsonresponse);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.wtf("Volley Request ERROR", error.toString());
            }
        });

        httpRequestQueue.add(indicationsJsonRequest);

        return inflater.inflate(R.layout.fragment_indications, container, false);
    }
}
