package net.omnidf.omnidf.rest;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.FeatureCollection;
import com.cocoahero.android.geojson.GeoJSON;

import net.omnidf.omnidf.R;
import net.omnidf.omnidf.adapters.DirectionsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OmniServer {
    public static final String GET_DIRECTIONS = "http://omnimobi.appspot.com/search.json?origin=%s&destination=%s";

    public static class RequestBuilder{
        private String urlRequest;
        private RecyclerView recyclerView;
        private Context context;
        private int method;
        private Response.Listener<JSONObject> listener;
        private Response.ErrorListener errorListener;

        public RequestBuilder(Context context) {
            this.context = context;
        }

        public RequestBuilder fetchIndications(String userOrigin, String userDestination ){
            this.urlRequest = String.format(GET_DIRECTIONS, userOrigin, userDestination);
            this.method = Request.Method.GET;

            this.listener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    JSONObject jsonDoc = null;
                    try {
                        jsonDoc = response.getJSONObject("content")
                                .getJSONObject("route_nodes");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, R.string.http_req_nodirections, Toast.LENGTH_LONG).show();
                    }
                    setupRecyclerView(parseGeoJsonFeatures(jsonDoc));
                }
            };

            this.errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.wtf("Volley Request ERROR", error.toString());
                    Toast.makeText(context, R.string.http_req_error, Toast.LENGTH_LONG).show();
                }
            };

            return this;
        }

        public RequestBuilder intoRecyclerView(RecyclerView recyclerView){
            this.recyclerView = recyclerView;
            return this;
        }

        public JsonObjectRequest build(){
            return new JsonObjectRequest(method, urlRequest,
                    listener, errorListener);
        }

        // Helper methods

        private List<Feature> parseGeoJsonFeatures(JSONObject jsonObject){
            FeatureCollection geoJson = (FeatureCollection) GeoJSON.parse(jsonObject);
            return geoJson.getFeatures();
        }

        private void setupRecyclerView(List<Feature> directions){
            DirectionsAdapter directionsAdapter = new DirectionsAdapter(directions);
            recyclerView.setAdapter(directionsAdapter);
        }
    }
}
