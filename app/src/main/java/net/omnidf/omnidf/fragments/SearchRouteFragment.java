package net.omnidf.omnidf.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.omnidf.omnidf.R;
import net.omnidf.omnidf.rest.OmniServer;

public class SearchRouteFragment extends Fragment {
    EditText inputOrigin;
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

        initViews(fragmentSearchRouteView);

        setupGetRouteClickListener(buttonGetRoute);
        setupEmptyFieldsWatcher(inputOrigin, inputDestination);

        return fragmentSearchRouteView;
    }

    // Helper Methods

    private void initViews(View fragmentSearchRouteView) {
        inputDestination = (EditText) fragmentSearchRouteView.findViewById(R.id.inputDestination);
        inputOrigin = (EditText) fragmentSearchRouteView.findViewById(R.id.inputOrigin);
        buttonGetRoute = (Button) fragmentSearchRouteView.findViewById(R.id.buttonGetRoute);
    }

    private void setOriginDestinationArgs(Fragment fragment) {
        Bundle args = new Bundle();
        args.putString(OmniServer.ORIGIN_QUERY, inputOrigin.getText().toString());
        args.putString(OmniServer.DESTINATION_QUERY, inputDestination.getText().toString());
        fragment.setArguments(args);
    }

    private void startFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void setupGetRouteClickListener(Button buttonGetRoute) {
        buttonGetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DirectionsFragment directionsFragment = new DirectionsFragment();
                setOriginDestinationArgs(directionsFragment);
                startFragment(directionsFragment);
            }
        });
    }

    private void setupEmptyFieldsWatcher(final EditText inputOrigin, final EditText inputDestination) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                boolean isFill = inputOrigin.getText().toString().length() > 3 && inputDestination.getText().toString().length() > 3;
                buttonGetRoute.setEnabled(isFill);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        inputOrigin.addTextChangedListener(textWatcher);
        inputDestination.addTextChangedListener(textWatcher);
    }

}
