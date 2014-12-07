package com.android.nazirshuqair.lastpick;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.httpmanager.HttpManager;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MapSearchFragment;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MyMapFragment;
import com.android.nazirshuqair.lastpick.model.Resturant;
import com.android.nazirshuqair.lastpick.parser.JSONParser;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends Activity implements MyMapFragment.MapMaster {

    List<Resturant> venuesList;
    List<Resturant> splashCoords = new ArrayList<Resturant>();

    EditText searchEdit;
    Button randomOkBtn;
    Spinner pricePointSpin;

    MyMapFragment myMapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar abr = getActionBar();
        abr.hide();

        myMapFragment = null;

        if (savedInstanceState == null) {
            myMapFragment = new MyMapFragment();
            getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment).commit();

            //MapSearchFragment searchFragment = MapSearchFragment.newInstance();
            //getFragmentManager().beginTransaction().replace(R.id.overly_container, searchFragment, MapSearchFragment.TAG).commit();
        }



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
    public void splashGPS(double _lat, double _lng) {

        String uriBase = "https://api.foursquare.com/v2/venues/explore?client_id=SK34ZCSYWESWVO501VCNPXY5T4ZZPEJSRLVYIBB2OPABNHCY&client_secret=M45XCWUBM23SD00JFTRBKCJSRQLW2VI2CDQ2TEUY0UEKKOGN%20&v=20130815&ll=";
        String uriEnd = "&query=food&radius=7000&limit=10& venuePhotos=1";

        String fullUri = uriBase + String.valueOf(_lat) + "," + String.valueOf(_lng) + uriEnd;

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

            //Log.i("TESTING", venuesList.toString());

            if (venuesList == null){
                Toast.makeText(MainActivity.this, "Zip code is Invalid!", Toast.LENGTH_LONG).show();
            }

            updateMapMarker();
        }

        public void updateMapMarker(){

           // myMapFragment = (MyMapFragment) getFragmentManager().findFragmentByTag(MyMapFragment.TAG);

            if (myMapFragment == null){
                myMapFragment = new MyMapFragment();
                getFragmentManager().beginTransaction().replace(R.id.map_container, myMapFragment, MyMapFragment.TAG).commit();
            }

            venuesList.size();

            for (Resturant resturant :  venuesList){
                Resturant resturantCoords = new Resturant();
                resturantCoords.setLat(resturant.getLat());
                resturantCoords.setLng(resturant.getLng());
                resturantCoords.setName(resturant.getName());
                splashCoords.add(resturantCoords);
            }

            myMapFragment.updateMarkers(splashCoords);
        }

        @Override
        protected void onProgressUpdate(String... values){

        }

    }
}
