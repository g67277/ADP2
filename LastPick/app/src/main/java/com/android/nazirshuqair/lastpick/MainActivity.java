package com.android.nazirshuqair.lastpick;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.detailsfiles.DetailsActivity;
import com.android.nazirshuqair.lastpick.httpmanager.HttpManager;
import com.android.nazirshuqair.lastpick.listscreenfiles.RandomDeletionActivity;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MapSearchFragment;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MyMapFragment;
import com.android.nazirshuqair.lastpick.model.Resturant;
import com.android.nazirshuqair.lastpick.parser.JSONParser;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nazirshuqair on 12/04/14.
 */

public class MainActivity extends Activity implements MyMapFragment.MapMaster, MapSearchFragment.SearchMasterClickListener {

    private static final int REQUEST_ENABLE_GPS = 0x02001;

    List<Resturant> venuesList;
    List<Resturant> splashCoords = new ArrayList<Resturant>();


    boolean skipMapping = false;
    boolean mainPics = false;
    boolean randomSearch = false;
    boolean initialConnection = false;

    String pricePoint;
    String userInput;
    int cVenue = 0;
    int bearing = 0;

    MyMapFragment myMapFragment;
    MapSearchFragment frag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar abr = getActionBar();
        abr.hide();

        myMapFragment = null;

        if (savedInstanceState == null) {
            frag = MapSearchFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.overly_container, frag, MapSearchFragment.TAG).commit();

            if (isOnline()) {
                myMapFragment = new MyMapFragment();
                getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment).commit();
            }
        }

    }

    public void noNetError(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Checking Connectivity
    protected boolean isOnline(){

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void apiCall(double _lat, double _lng, String _term, String _price) {

        userInput = _term;
        String uriBase = "https://api.foursquare.com/v2/venues/explore?client_id=SK34ZCSYWESWVO501VCNPXY5T4ZZPEJSRLVYIBB2OPABNHCY&client_secret=M45XCWUBM23SD00JFTRBKCJSRQLW2VI2CDQ2TEUY0UEKKOGN%20&v=20130815&ll=";
        String uriQuery = "&query=";
        String uriPrice = "&price=";
        String uriEnd = "&radius=7000&limit=10&venuePhotos=1";
        String fullUri;
        if (_price.isEmpty()){
            fullUri = uriBase + String.valueOf(_lat) + "," + String.valueOf(_lng) + uriQuery + _term + uriEnd;
        }else {
            fullUri = uriBase + String.valueOf(_lat) + "," + String.valueOf(_lng) + uriQuery + _term + uriPrice + _price + uriEnd;
        }

        Log.i("TESTING", fullUri);

        if (isOnline()){
            requestData(fullUri);

        }else {
            Toast.makeText(this, "No network", Toast.LENGTH_LONG).show();
        }

    }

    private void requestData(String uri){

        MyTask myTask = new MyTask();
        myTask.execute(uri);

    }

    @Override
    public void pushData(String _searchTerm, String _pricePoint, boolean _random) {
        LatLng requestedCoords = new LatLng(0,0);
        requestedCoords = myMapFragment.enableGps();

        randomSearch = _random;
        pricePoint = _pricePoint;

        apiCall(requestedCoords.latitude, requestedCoords.longitude, _searchTerm, _pricePoint);

        skipMapping = true;
    }

    private class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute(){
            //updateDisplay
            //pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params){

            String content = HttpManager.getData(params[0]);

            return content;
        }

        @Override
        protected void onPostExecute(String result){

            //pb.setVisibility(View.INVISIBLE);

            Log.i("TESTING", result);

            if (result == null){
                Toast.makeText(MainActivity.this, "Can't Connect", Toast.LENGTH_LONG).show();
                return;
            }
            venuesList = JSONParser.parseFeed(result);

            if (venuesList != null) {
                if (venuesList.size() != 0 && !skipMapping) {
                    //updateMapMarker();
                    timerHandler.postDelayed(timerRunnable, 0);
                    mainPics = true;
                } else if (venuesList.size() != 0 && skipMapping) {
                    populateList();
                    mainPics = false;
                } else {
                    if (randomSearch){
                        pushData(frag.randomSearch(), pricePoint, true);
                    }else {
                        Toast.makeText(MainActivity.this, "No matching restaurants found", Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                if (randomSearch){
                    pushData(frag.randomSearch(), pricePoint, true);
                }else {
                    Toast.makeText(MainActivity.this, "No matching restaurants found", Toast.LENGTH_LONG).show();
                }
            }

        }

        /* updated might use later
        public void updateMapMarker(){


            if (myMapFragment == null){
                myMapFragment = new MyMapFragment();
                getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment, MyMapFragment.TAG).commit();
            }

            venuesList.size();
            if (splashCoords.size() > 0){
                splashCoords.clear();
            }

            for (Resturant resturant :  venuesList){
                Resturant resturantCoords = new Resturant();
                resturantCoords.setLat(resturant.getLat());
                resturantCoords.setLng(resturant.getLng());
                resturantCoords.setName(resturant.getName());
                splashCoords.add(resturantCoords);
            }

            //myMapFragment.updateMarkers(splashCoords);
        }*/

        public void populateList(){
            timerHandler.removeCallbacks(timerRunnable);
            frag.pauseLabelUpdate();
            Intent intent = new Intent(MainActivity.this, RandomDeletionActivity.class);

            // INITIALIZE NEW ARRAYLIST AND POPULATE
            ArrayList<Resturant> overlays = new ArrayList<Resturant>();
            for (Resturant c : venuesList) {
                overlays.add(new Resturant(c.getName(), c.getFormattedPhone(), c.getFormattedAddress(), c.getCurrency(),
                        c.getText(), c.getFirstName(), c.getUrl(), c.getRating(), c.getLat(), c.getLng(), c.getDistance(), c.getImgUrl()));
            }

            // EMBED INTO INTENT
            intent.putParcelableArrayListExtra("venues", overlays);
            intent.putExtra("userInput", userInput);
            startActivity(intent);
        }

        @Override
        protected void onProgressUpdate(String... values){

        }
    }

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            if (bearing > 360){
                bearing = 0;
            }

            if (cVenue < venuesList.size() ) {
                animateMap();
                bearing = bearing + 15;
                cVenue++;
            }else {
                bearing = 0;
                cVenue = 0;
                animateMap();
            }
            timerHandler.postDelayed(this, 8000);
        }
    };

    public void animateMap(){

        if (myMapFragment == null){
            myMapFragment = new MyMapFragment();
            getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment, MyMapFragment.TAG).commit();
        }

        Resturant resturant = new Resturant();
        resturant = venuesList.get(cVenue);

        myMapFragment.animateMap(new LatLng(resturant.getLat(),resturant.getLng()), resturant.getName(), bearing);
        frag.updateMarkerDetails(resturant.getName(), String.valueOf(resturant.getRating()), resturant.getFormattedPhone(),
                resturant.getDistance(), resturant.getImgUrl());

    }

    @Override
    public void pauseAnimation(boolean _paused) {
        if (isOnline()) {
            if (_paused) {
                timerHandler.removeCallbacks(timerRunnable);
            } else {
                timerHandler.postDelayed(timerRunnable, 0);
            }
        }
    }

    @Override
    public void displayDetails(int _position) {

        timerHandler.removeCallbacks(timerRunnable);
        frag.pauseLabelUpdate();

        if (!isOnline()){
            frag.showNetError(1);
            myMapFragment = new MyMapFragment();
            getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment).commit();
            initialConnection = true;
        }else if (initialConnection){
            myMapFragment = new MyMapFragment();
            getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment).commit();
            initialConnection = false;
        }else if (!myMapFragment.gpsStatus()){
            myMapFragment.enableGps();
        }else {
            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            Resturant resturant = venuesList.get(cVenue - 1 + _position);

            intent.putExtra("name", resturant.getName());
            intent.putExtra("distance", String.valueOf(Math.round(resturant.getDistance() * 1000) / 1000));
            intent.putExtra("phone", resturant.getFormattedPhone());
            intent.putExtra("address", resturant.getFormattedAddress());
            intent.putExtra("review", resturant.getText());
            intent.putExtra("username", resturant.getFirstName());
            intent.putExtra("rating", String.valueOf(resturant.getRating()));
            intent.putExtra("lat", resturant.getLat());
            intent.putExtra("lng", resturant.getLng());

            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timerHandler.removeCallbacks(timerRunnable);
    }
}
