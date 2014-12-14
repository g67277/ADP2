package com.android.nazirshuqair.lastpick.listscreenfiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.FeaturedFragment;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.MasterListFragment;
import com.android.nazirshuqair.lastpick.model.Resturant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by nazirshuqair on 12/13/14.
 */
public class RandomDeletionActivity extends Activity implements MasterListFragment.MasterClickListener, FeaturedFragment.RestoreClickListener{

    private List<Resturant> resturantsList;
    private List<Resturant> restoredList;
    ScheduledExecutorService executor;

    MasterListFragment listFragment;

    MenuItem startDeletion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listscreen);

        getActionBar().setDisplayHomeAsUpEnabled(true);



        Intent i = getIntent(); // RETRIEVE OUR INTENT
        resturantsList = i.getParcelableArrayListExtra("venues"); // GET PARCELABLE VENUES
        restoredList = new LinkedList<Resturant>();
        for (Resturant resturant: resturantsList){
            restoredList.add(resturant);
        }
        listFragment = null;
        if(savedInstanceState == null) {
            listFragment = MasterListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, listFragment, MasterListFragment.TAG).commit();
        }
        listFragment.updateDisplay(resturantsList, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent intent = getIntent();

        if (intent.getBooleanExtra("edit", true)) {
            startDeletion = menu.add("Pick for Me!");
            startDeletion.setShowAsAction(1);

            startDeletion.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    startDeletion();

                    return false;
                }
            });

        }

        return super.onCreateOptionsMenu(menu);

    }

    public void startDeletion(){

        Runnable helloRunnable = new Runnable() {
            public void run() {
                Log.i("TESTING", "ONE OEIFJOEFJEIOWFJ EOWFIJEWOFI JEWOFJEWOFI JEOWFIJ WOFJ");

                if (resturantsList.size() > 1) {
                    randomDeletion();
                }else {
                    executor.shutdown();
                    displayFeatured();
                }
            }
        };

        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 3, TimeUnit.SECONDS);
    }

    public void randomDeletion(){
        Random r = new Random();
        int i1 = r.nextInt(resturantsList.size() - 0) + 0;
        resturantsList.remove(i1);
        listFragment.updateDisplay(resturantsList, true);
    }

    public void displayFeatured(){
        startDeletion.setTitle("");
        String name = resturantsList.get(0).getName();
        String phone = resturantsList.get(0).getFormattedPhone();
        String address = resturantsList.get(0).getFormattedAddress();
        FeaturedFragment frag = FeaturedFragment.newInstance(name, phone, address);
        getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, frag, FeaturedFragment.TAG).commit();
    }

    @Override
    public void retriveData(HashMap<String, Object> item, int position) {

    }


    @Override
    public void restore() {
        resturantsList.clear();
        for (Resturant resturant: restoredList){
            resturantsList.add(resturant);
        }
        getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, listFragment, MasterListFragment.TAG).commit();
        listFragment.updateDisplay(resturantsList, true);
        startDeletion.setTitle("Pick for Me!");

    }

}
