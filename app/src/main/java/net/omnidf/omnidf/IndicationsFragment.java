package net.omnidf.omnidf;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class IndicationsFragment extends Fragment {

    Context utilActivityContext = getActivity();
    RecyclerView indicationsRecyclerView;
    IndicationsAdapter indicationsAdapter;
    ArrayList<Indication> indications = new ArrayList<>();
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

        JsonArrayRequest indicationsJsonRequest = new JsonArrayRequest(Request.Method.GET, Const.URL_INDICATIONS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String trayectos;
                        int tiempo;
                        int tipoTransporte;
                        int precio;
                        String indicaciones;

                        for(int i = 0; i < response.length(); i++){
                            try {
                                trayectos = response.getJSONObject(i).getString("trayectos");
                                tiempo = response.getJSONObject(i).getInt("tiempo");
                                tipoTransporte = response.getJSONObject(i).getInt("tipoTransporte");
                                precio = response.getJSONObject(i).getInt("precio");
                                indicaciones = response.getJSONObject(i).getString("indicaciones");

                                indications.add(new Indication(trayectos, tiempo, tipoTransporte,
                                        precio, indicaciones));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }

                        indicationsAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        httpRequestQueue.add(indicationsJsonRequest);

        return inflater.inflate(R.layout.fragment_indications, container, false);
    }
}
