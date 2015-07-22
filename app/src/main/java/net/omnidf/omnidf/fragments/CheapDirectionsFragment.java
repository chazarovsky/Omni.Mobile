package net.omnidf.omnidf.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.omnidf.omnidf.R;

public class CheapDirectionsFragment extends Fragment {

    public CheapDirectionsFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View directionsFragmentView = inflater.inflate(R.layout.fragment_cheap_directions, container, false);

        return directionsFragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
