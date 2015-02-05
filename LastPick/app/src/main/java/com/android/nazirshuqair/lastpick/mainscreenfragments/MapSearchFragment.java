package com.android.nazirshuqair.lastpick.mainscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.textViewHelper.AutoResizeTextView;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by nazirshuqair on 12/6/14.
 */


public class MapSearchFragment extends Fragment {


    public static final String TAG = "MapSearchFragment.TAG";

    int priceSelected;
    boolean animationPaused = false;
    String venuePhone;

    EditText searchEdit;
    Spinner pricePointSpin;
    Button clearText, pauseAnimation, randomOkBtn;
    RelativeLayout markerDetails;
    TextView vRating, vDistance ;
    AutoResizeTextView vName, vPhone;
    ImageView vPic;
    ProgressBar mainProgress;
    String[] mostSearched;


    public interface SearchMasterClickListener {
        public void pushData(String _searchTerm, String _pricePoint, boolean _random);
        public void pauseAnimation(boolean _paused);
        public void displayDetails(int _position);
    }

    private SearchMasterClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if (_activity instanceof SearchMasterClickListener) {
            mListener = (SearchMasterClickListener) _activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }


    public static MapSearchFragment newInstance() {
        MapSearchFragment frag = new MapSearchFragment();

        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        View view = _inflater.inflate(R.layout.search_fragment, _container, false);

        mostSearched = getResources().getStringArray(R.array.most_searched_lunch);

        searchEdit = (EditText) view.findViewById(R.id.search_edit);
        randomOkBtn = (Button) view.findViewById(R.id.random_ok_btn);
        pricePointSpin = (Spinner) view.findViewById(R.id.price_point);
        clearText = (Button) view.findViewById(R.id.clearText);
        pauseAnimation = (Button) view.findViewById(R.id.pause_animation);
        markerDetails = (RelativeLayout) view.findViewById(R.id.main_info_direct);
        vName = (AutoResizeTextView) view.findViewById(R.id.main_v_name);
        vRating = (TextView) view.findViewById(R.id.main_v_rating);
        vDistance = (TextView) view.findViewById(R.id.main_v_distance);
        vPhone = (AutoResizeTextView) view.findViewById(R.id.main_v_phone);
        vPic = (ImageView) view.findViewById(R.id.main_v_pic);
        mainProgress = (ProgressBar) view.findViewById(R.id.main_progress_bar);

        vPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMap();
                if (!venuePhone.equals("Not Available")) {
                    String phoneNumPlain = venuePhone.replaceAll("\\D", "");
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumPlain));
                    startActivity(intent);
                }
            }
        });

        markerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.displayDetails(0);
            }
        });

        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    pauseMap();
                    clearText.setAlpha(100);
                    clearText.setClickable(true);
                    randomOkBtn.setText("OK");
                    pricePointSpin.performClick();
                } else {
                    playMap();
                    clearText.setAlpha(0);
                    clearText.setClickable(false);
                    randomOkBtn.setText("Random");
                }
            }
        });

        pricePointSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();
                priceSelected = label.length();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        randomOkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (priceSelected > 0 && searchEdit.getText().length() > 0) {
                    mListener.pushData(String.valueOf(searchEdit.getText()), String.valueOf(priceSelected), false);
                } else if (searchEdit.getText().length() > 0) {
                    mListener.pushData(String.valueOf(searchEdit.getText()), "", false);
                } else if (priceSelected > 0){
                    mListener.pushData(randomSearch(), String.valueOf(priceSelected), true);
                } else {
                    mListener.pushData(randomSearch(), "", true);
                }
                searchEdit.clearFocus();
            }
        });

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
                pricePointSpin.setSelection(0);
            }
        });

        pauseAnimation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!animationPaused){
                    pauseMap();
                }else {
                    playMap();
                }
            }
        });

        return view;
    }

    public String randomSearch(){
        Random r = new Random();
        int i1 = r.nextInt(mostSearched.length - 0) + 0;
        String result = mostSearched[i1];
        return result;
    }

    public void pauseLabelUpdate(){
        pauseMap();
    }

    public void pauseMap(){
        animationPaused = true;
        mListener.pauseAnimation(animationPaused);
        pauseAnimation.setBackgroundResource(R.drawable.play);
    }

    public void playMap(){
        animationPaused = false;
        mListener.pauseAnimation(animationPaused);
        pauseAnimation.setBackgroundResource(R.drawable.pause);
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

    }

    public void updateMarkerDetails(String _name, String _rating, String _phone, double _distance, String _imgUrl){
        vName.resetTextSize();
        vPhone.resetTextSize();

        venuePhone = _phone;
        vName.setText(_name);
        vName.resizeText();
        vRating.setText(_rating);
        vPhone.setText(_phone);
        vPhone.resizeText();
        vDistance.setText(String.valueOf(Math.round(_distance * 100.0) / 100.0) + " mi");
        Picasso.with(getActivity()).load(_imgUrl).into(vPic);
    }


    public void showNetError(int _errorCode){

        if (_errorCode == 1){
            vName.setText("Please make sure you're connected...");
            vPhone.setText("Tap here to retry");
            vRating.setAlpha(0);
        }else {
            vRating.setAlpha(100);
        }
    }

    public void showProgress(){
        mainProgress.setVisibility(View.VISIBLE);
    }

    public void hideProgress(){
        mainProgress.setVisibility(View.INVISIBLE);
    }

}