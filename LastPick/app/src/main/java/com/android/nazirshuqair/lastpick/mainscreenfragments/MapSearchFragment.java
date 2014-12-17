package com.android.nazirshuqair.lastpick.mainscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.R;
import com.squareup.picasso.Picasso;

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

    EditText searchEdit;
    Button randomOkBtn;
    Spinner pricePointSpin;
    Button clearText;

    String[] mostSearched;


    public interface SearchMasterClickListener {
        public void pushData(String _searchTerm, String _pricePoint);
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

        searchEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    clearText.setAlpha(100);
                    clearText.setClickable(true);
                    randomOkBtn.setText("OK");
                    pricePointSpin.performClick();
                } else {
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
                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "You selected: " + label, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        randomOkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (priceSelected > 0 && searchEdit.getText().length() > 0) {
                    mListener.pushData(String.valueOf(searchEdit.getText()), String.valueOf(priceSelected));
                } else if (searchEdit.getText().length() > 0) {
                    mListener.pushData(String.valueOf(searchEdit.getText()), "");
                } else if (priceSelected > 0){
                    mListener.pushData(randomSearch(), String.valueOf(priceSelected));
                } else {
                    mListener.pushData(randomSearch(), "");
                }
                searchEdit.clearFocus();
            }
        });

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdit.setText("");
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

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

    }

}