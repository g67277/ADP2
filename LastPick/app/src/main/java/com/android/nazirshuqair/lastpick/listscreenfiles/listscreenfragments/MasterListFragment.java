package com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MapSearchFragment;
import com.android.nazirshuqair.lastpick.model.Resturant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nazirshuqair on 12/13/14.
 */
public class MasterListFragment extends Fragment {

    //Tag to identify the fragment
    public static final String TAG = "MasterListFragment.TAG";

    private static final String CNAME = "name";
    private static final String CDist = "distance";
    private static final String CPHONE = "phone";

    ListView venuesListView;
    SimpleAdapter adapter;

    public ArrayList<HashMap<String, Object>> mVenueList = new ArrayList<HashMap<String, Object>>();

    //This is to create a new instance of the fragment
    public static MasterListFragment newInstance() {
        MasterListFragment frag = new MasterListFragment();
        return frag;
    }

    public void updateDisplay (List<Resturant> _object, boolean _refresh) {


        if (mVenueList.size() > 0){
            mVenueList.clear();
        }
        for (Resturant venue : _object) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put(CNAME, venue.getName());
            map.put(CPHONE, venue.getFormattedPhone());

            mVenueList.add(map);
        }

        if (_refresh){
            adapter.notifyDataSetChanged();
            venuesListView.invalidateViews();
        }

    }

    public interface MasterClickListener {
        public void retriveData(HashMap<String, Object> item, int position);
    }

    private MasterClickListener mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof MasterClickListener) {
            mListener = (MasterClickListener)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Connecting the view
        View myFragmentView = inflater.inflate(R.layout.fragment_list, container, false);

        //Connecting the ListView
        venuesListView = (ListView) myFragmentView.findViewById(R.id.venueViewList);


        return myFragmentView;
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        // Creating an array of our keys
        String[] keys = new String[] {
                CNAME, CPHONE
        };

        // Creating an array of our list item components.
        // Indices must match the keys array.
        int[] views = new int[] {
                R.id.venuName,
                R.id.venuNum
        };

        //creating an adapter to populate the listview
        adapter = new SimpleAdapter(getActivity(), mVenueList,  R.layout.list_item, keys, views);
        venuesListView.setAdapter(adapter);

        venuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Call the displayDetails method and pass the adapter view and position
                //to populate details elements
                mListener.retriveData(mVenueList.get(position), position);
            }

        });
    }

}
