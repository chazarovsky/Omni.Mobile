package net.omnidf.omnidf.rest;

import android.content.Context;
import android.net.Uri;
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
    public static final String SEARCH_DIRECTION_URL = "http://omnimobi.appspot.com/search.json";

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

        public RequestBuilder fetchDirections(String userOrigin, String userDestination){
            this.urlRequest = urlDirections(userOrigin, userDestination);
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
                        Toast.makeText(context, R.string.http_req_parse_error, Toast.LENGTH_LONG).show();
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
            if(jsonObject == null) return null;
            FeatureCollection geoJson = (FeatureCollection) GeoJSON.parse(jsonObject);
            return geoJson.getFeatures();
        }

        private void setupRecyclerView(List<Feature> directions){
            if (directions == null) Toast.makeText(context, R.string.http_req_parse_error, Toast.LENGTH_SHORT).show();
            else {
            DirectionsAdapter directionsAdapter = new DirectionsAdapter(directions);
            recyclerView.setAdapter(directionsAdapter);}
        }
        
        private String urlDirections(String origin, String destination){
            return Uri.parse(SEARCH_DIRECTION_URL).buildUpon()
                    .appendQueryParameter("origin", origin)
                    .appendQueryParameter("destination", destination)
                    .build().toString();
        }
    }
}
