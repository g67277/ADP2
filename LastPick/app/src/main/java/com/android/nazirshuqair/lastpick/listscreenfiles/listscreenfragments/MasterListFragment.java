package com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.model.Resturant;
import com.android.nazirshuqair.lastpick.textViewHelper.AutoResizeTextView;
import com.squareup.picasso.Picasso;

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
    private static final String CIMG = "img";

    ListView venuesListView;
    SimpleAdapter adapter;

    //public ArrayList<HashMap<String, Object>> mVenueList = new ArrayList<HashMap<String, Object>>();
    public List<Resturant> mVenuesList = new ArrayList<Resturant>();

    //This is to create a new instance of the fragment
    public static MasterListFragment newInstance() {
        MasterListFragment frag = new MasterListFragment();
        return frag;
    }

    public void updateDisplay (List<Resturant> _object, boolean _refresh) {


        if (mVenuesList.size() > 0){
            mVenuesList.clear();
        }

        for (int i = 0; i < _object.size(); i++){
            mVenuesList.add(_object.get(i));
        }

        if (_refresh){
            venuesListView.invalidate();
            venuesListView.invalidateViews();
        }

    }

    public interface MasterClickListener {
        public void pushItemSelected(int _position);
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
                CNAME, CPHONE, CDist, CIMG
        };

        // Creating an array of our list item components.
        // Indices must match the keys array.
        int[] views = new int[] {
                R.id.venuName,
                R.id.venuNum,
                R.id.distance_label,
                R.id.venue_photo
        };

        venuesListView.setAdapter(new dataListAdapter(mVenuesList));

        venuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.pushItemSelected(position);
            }
        });
        /*
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
        */
    }

    class dataListAdapter extends BaseAdapter {

        List<Resturant> mList = new ArrayList<Resturant>();

        dataListAdapter() {
            mList = null;
        }

        public dataListAdapter(List<Resturant> _list) {
            mList = _list;

        }

        public int getCount() {
            return mList.size();
        }

        public Object getItem(int arg0) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View row;
            row = inflater.inflate(R.layout.list_item, parent, false);
            AutoResizeTextView vName, vPhone;
            TextView vDistance;
            ImageView vImg;
            Resturant resturant = new Resturant();

            resturant = mList.get(position);

            vName = (AutoResizeTextView) row.findViewById(R.id.venuName);
            vPhone = (AutoResizeTextView) row.findViewById(R.id.venuNum);
            vDistance = (TextView) row.findViewById(R.id.distance_label);
            vImg=(ImageView)row.findViewById(R.id.venue_photo);

            String imgURl = resturant.getImgUrl();
            double distance = Math.round(resturant.getDistance() * 100) / 100;
            vName.setText(resturant.getName());
            vName.resizeText();
            vPhone.setText(resturant.getFormattedPhone());
            vPhone.resizeText();
            vDistance.setText(distance + " mi");
            Picasso.with(getActivity()).load(imgURl).into(vImg);

            return (row);
        }
    }

}
