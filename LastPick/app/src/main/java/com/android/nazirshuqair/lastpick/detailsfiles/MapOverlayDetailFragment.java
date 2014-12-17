package com.android.nazirshuqair.lastpick.detailsfiles;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.nazirshuqair.lastpick.R;

/**
 * Created by nazirshuqair on 12/16/14.
 */
public class MapOverlayDetailFragment extends Fragment {

    public static final String TAG = "MapOverlayDetailFragment.TAG";
    private static final String ARG_NAME = "MapOverlayDetailFragment.ARG_NAME";
    private static final String ARG_DISTANCE = "MapOverlayDetailFragment.ARG_DISTANCE";
    private static final String ARG_RATING = "MapOverlayDetailFragment.ARG_RATING";


    Button backBtn;
    TextView venueNameLabel;
    TextView distanceLabel;
    TextView ratingLabel;

    public interface BackClickListener{
        public void backToList();
    }

    private BackClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof BackClickListener) {
            mListener = (BackClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    public static MapOverlayDetailFragment newInstance() {
        MapOverlayDetailFragment frag = new MapOverlayDetailFragment();
        return frag;
    }

    public static MapOverlayDetailFragment newInstance(String _name, String _distance, String _rating){

        MapOverlayDetailFragment frag = new MapOverlayDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, _name);
        args.putString(ARG_DISTANCE, _distance);
        args.putString(ARG_RATING, _rating);
        frag.setArguments(args);

        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.mapoverlay_fragment, _container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NAME)){
            updateDisplay(args.getString(ARG_NAME),
                    args.getString(ARG_DISTANCE),
                    args.getString(ARG_RATING));
        }

        backBtn = (Button) getView().findViewById(R.id.backToList);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.backToList();
            }
        });

    }

    public void updateDisplay(String _name, String _distance,  String _rating){

        venueNameLabel = (TextView) getView().findViewById(R.id.venue_Name_overlay);
        distanceLabel = (TextView) getView().findViewById(R.id.distance_detail);
        ratingLabel = (TextView) getView().findViewById(R.id.rating_label);

        venueNameLabel.setText(_name);
        distanceLabel.setText(_distance + " mi");
        ratingLabel.setText(_rating);
    }




}
