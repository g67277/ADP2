package com.android.nazirshuqair.lastpick.detailsfiles;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nazirshuqair.lastpick.R;

/**
 * Created by nazirshuqair on 12/16/14.
 */
public class DisplayDetailFragment extends Fragment {

    public static final String TAG = "DisplayDetailFragment.TAG";
    private static final String ARG_PHONE = "DisplayDetailFragment.ARG_PHONE";
    private static final String ARG_ADDRESS = "DisplayDetailFragment.ARG_ADDRESS";
    private static final String ARG_REVIEW = "DisplayDetailFragment.ARG_REVIEW";
    private static final String ARG_FIRST = "DisplayDetailFragment.ARG_FIRST";

    TextView phoneLabel;
    TextView addressLabel;
    TextView reviewLabel;
    TextView userNameLabel;

    String venuePhone;

    public static DisplayDetailFragment newInstance() {
        DisplayDetailFragment frag = new DisplayDetailFragment();
        return frag;
    }

    public static DisplayDetailFragment newInstance(String _phone, String _address, String _review, String _firstName){

        DisplayDetailFragment frag = new DisplayDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PHONE, _phone);
        args.putString(ARG_ADDRESS, _address);
        args.putString(ARG_REVIEW, _review);
        args.putString(ARG_FIRST, _firstName);
        frag.setArguments(args);

        return frag;

    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.details_fragment, _container, false);

        return view;
    }


    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        final Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_PHONE)){
            updateDisplay(args.getString(ARG_PHONE),
                    args.getString(ARG_ADDRESS),
                    args.getString(ARG_REVIEW),
                    args.getString(ARG_FIRST));

            phoneLabel = (TextView) getView().findViewById(R.id.phone_view);

            phoneLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!venuePhone.equals("Not Available")) {
                        String phoneNumPlain = venuePhone.replaceAll("\\D", "");
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumPlain));
                        startActivity(intent);
                    }
                }
            });
        }
    }

    public void updateDisplay(String _phone, String _address, String _review, String _firstName){

        venuePhone = _phone;
        phoneLabel = (TextView) getView().findViewById(R.id.phone_view);
        addressLabel = (TextView) getView().findViewById(R.id.address_view);
        reviewLabel = (TextView) getView().findViewById(R.id.review_view);
        userNameLabel = (TextView) getView().findViewById(R.id.user_name_view);

        phoneLabel.setText(_phone);
        addressLabel.setText(_address);
        reviewLabel.setText(_review);
        userNameLabel.setText(_firstName);
    }

}
