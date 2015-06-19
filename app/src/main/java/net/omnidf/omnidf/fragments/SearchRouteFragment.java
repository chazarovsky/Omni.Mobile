package net.omnidf.omnidf.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.omnidf.omnidf.R;

public class SearchRouteFragment extends Fragment {
    EditText inputDestination;
    Button buttonGetRoute;

    public SearchRouteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentSearchRouteView = inflater.inflate(R.layout.fragment_search_route, container, false);

        inputDestination = (EditText) fragmentSearchRouteView.findViewById(R.id.inputDestination);
        buttonGetRoute = (Button) fragmentSearchRouteView.findViewById(R.id.buttonGetRoute);

        buttonGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("destination", inputDestination.getText().toString());
                DirectionsFragment directionsFragment = new DirectionsFragment();
                directionsFragment.setArguments(args);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, directionsFragment)
                        .commit();
            }
        });

        return fragmentSearchRouteView;
    }

}
