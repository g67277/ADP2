package com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.nazirshuqair.lastpick.R;

/**
 * Created by nazirshuqair on 12/13/14.
 */
public class FeaturedFragment extends Fragment {

    public static final String TAG = "FeaturedFragment.TAG";
    private static final String ARG_NAME = "ViewFragment.ARG_NAME";
    private static final String ARG_PHONE = "ViewFragment.ARG_PHONE";
    private static final String ARG_ADDRESS = "ViewFragment.ARG_ADDRESS";

    ImageView fImage;
    TextView fName;
    TextView fPhone;
    TextView fAddress;
    Button restore;

    public interface RestoreClickListener{
        public void restore();
    }

    private RestoreClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof RestoreClickListener) {
            mListener = (RestoreClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    public static FeaturedFragment newInstance(String _name, String _phone, String _address) {
        FeaturedFragment frag = new FeaturedFragment();

        Bundle args = new Bundle();
        args.putString(ARG_NAME, _name);
        args.putString(ARG_PHONE, _phone);
        args.putString(ARG_ADDRESS, _address);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.featured_fragment, _container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_NAME)) {
            updateDisplay(args.getString(ARG_NAME),
                    args.getString(ARG_PHONE),
                    args.getString(ARG_ADDRESS));

            restore = (Button) getView().findViewById(R.id.restore);


            restore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.restore();
                }
            });
        }
    }

    public void updateDisplay(String _name, String _phone, String _address){

        fName = (TextView) getView().findViewById(R.id.featuredAddress);
        fImage = (ImageView) getView().findViewById(R.id.featuredImage);
        fName = (TextView) getView().findViewById(R.id.featuredName);
        fPhone = (TextView) getView().findViewById(R.id.featuredPhone);
        fAddress = (TextView) getView().findViewById(R.id.featuredAddress);

        fName.setText(_name);
        fPhone.setText(_phone);
        fAddress.setText(_address);
    }


}
