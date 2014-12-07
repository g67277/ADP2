package com.android.nazirshuqair.lastpick.mainscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.R;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by nazirshuqair on 12/6/14.
 */


public class MapSearchFragment extends Fragment {

    public static final String TAG = "MapSearchFragment.TAG";


    EditText searchEdit;
    Button randomOkBtn;

    public MapSearchFragment(){

    }

    public static MapSearchFragment newInstance(){
        MapSearchFragment frag = new MapSearchFragment();
        return frag;
    }

    public interface SearchMasterClickListener{
        public void pushData(String _searchTerm, String _pricePoint);
    }

    private SearchMasterClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof SearchMasterClickListener) {
            mListener = (SearchMasterClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.search_fragment, container, false);

        searchEdit = (EditText) myFragmentView.findViewById(R.id.search_edit);
        randomOkBtn = (Button) myFragmentView.findViewById(R.id.random_ok_btn);

        randomOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.pushData(String.valueOf(searchEdit.getText()), " ");
            }
        });

        return myFragmentView;
    }
}
