package com.android.nazirshuqair.lastpick.mainscreenfragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import com.android.nazirshuqair.lastpick.model.Resturant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazirshuqair on 12/6/14.
 */
public class MyMapFragment extends MapFragment implements GoogleMap.OnInfoWindowClickListener, LocationListener {

    public static final String TAG = "MyMapFragment.TAG";
    private static final int REQUEST_ENABLE_GPS = 0x02001;

    GoogleMap mMap;
    LocationManager locManager;
    double cLatitude;
    double cLongitude;
    double lastMarkerLat;
    double lastMarkerLng;
    boolean gpsEnabled;

    public List<Resturant> coordinatesList = new ArrayList<Resturant>();

    public interface MapMaster{

        public void apiCall(double _lat, double _lng, String _term, String _price);
    }

    MapMaster mListener;

    @Override
    public void onAttach(Activity _activity) {
        super.onAttach(_activity);

        if(_activity instanceof MapMaster) {
            mListener = (MapMaster)_activity;
        } else {
            throw new IllegalArgumentException("Containing activity must implement OnButtonClickListener interface");
        }
    }

    public void updateMarkers(List<Resturant> _splashMarkers){

        coordinatesList = _splashMarkers;
        mMap.clear();

        if (mMap != null){
            for (Resturant resturant : _splashMarkers){
                lastMarkerLat = resturant.getLat();
                lastMarkerLng = resturant.getLng();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(lastMarkerLat, lastMarkerLng))
                        .title(resturant.getName()));

            }
        }

        if (gpsEnabled) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cLatitude, cLongitude), 12));
        }else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastMarkerLat, lastMarkerLng), 12));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        locManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        mMap = getMap();
        enableGps();

        mListener.apiCall(cLatitude, cLongitude, "food", "");

        mMap.setInfoWindowAdapter(new MarkerAdapter());
        mMap.setOnInfoWindowClickListener(this);

        /*if (gpsEnabled) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cLatitude, cLongitude), 16));
        }else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(picLat, picLong), 16));
        }*/

        locManager.removeUpdates(this);
    }



    public LatLng enableGps(){

        LatLng requestedCoords = new LatLng(0,0);
        LatLng nullCoords = new LatLng(0,0);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null){
                cLatitude = loc.getLatitude();
                cLongitude = loc.getLongitude();
                requestedCoords = new LatLng(cLatitude, cLongitude);
            }
            gpsEnabled = true;
        }else {
            gpsEnabled = false;
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Unavailable")
                    .setMessage("Please enabled GPS in the system settings")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(settingsIntent, REQUEST_ENABLE_GPS);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        if (requestedCoords != nullCoords){
            return requestedCoords;
        }
        return null;
    }

    private class MarkerAdapter implements GoogleMap.InfoWindowAdapter {

        TextView mText;

        public MarkerAdapter() {
            mText = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        cLatitude = location.getLatitude();
        cLongitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
