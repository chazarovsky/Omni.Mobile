package net.omnidf.omnidf.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.omnidf.omnidf.R;

public class RouteCriteriaFragment extends Fragment {

    public interface GetRoutesListener {
        void onGetRoute();
    }

    RelativeLayout relativeCheap;
    RelativeLayout relativeFast;
    GetRoutesListener getRouteCallback;

    public RouteCriteriaFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getRouteCallback = (GetRoutesListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View routeCriteriaFragmentView = inflater
                .inflate(R.layout.fragment_route_criteria, container, false);

        initViews(routeCriteriaFragmentView);
        setUpCriteriasClicks();

        return routeCriteriaFragmentView;
    }

    private void initViews(View fragment) {
        relativeCheap = (RelativeLayout) fragment.findViewById(R.id.relativeCheap);
        relativeFast = (RelativeLayout) fragment.findViewById(R.id.relativeFast);
    }

    private void setUpCriteriasClicks() {
        View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setBackgroundColor(Color.argb(84, 189, 189, 189));
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        getRouteCallback.onGetRoute();
                        break;
                    }
                }
                return false;
            }
        };
        relativeCheap.setOnTouchListener(onTouchListener);
        relativeFast.setOnTouchListener(onTouchListener);
    }
}
