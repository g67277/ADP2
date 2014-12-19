package com.android.nazirshuqair.lastpick.listscreenfiles;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.detailsfiles.DetailsActivity;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.FeaturedFragment;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.MasterListFragment;
import com.android.nazirshuqair.lastpick.listscreenfiles.listscreenfragments.SettingsFragment;
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
public class RandomDeletionActivity extends Activity implements MasterListFragment.MasterClickListener,
        FeaturedFragment.RestoreClickListener, SettingsFragment.PrefClickListener{

    private List<Resturant> resturantsList;
    private List<Resturant> restoredList;

    SharedPreferences defaultPrefs;


    MasterListFragment listFragment;

    boolean startStop = false;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            if (resturantsList.size() > 2) {
                randomDeletion();
            }else {
                timerHandler.removeCallbacks(timerRunnable);
                startStop = false;
                displayFeatured();
                return;
            }

            timerHandler.postDelayed(this, defaultPrefs.getInt("seconds", 2000));
        }
    };

    MenuItem startDeletion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listscreen);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#c8198cff")));

        defaultPrefs = PreferenceManager.getDefaultSharedPreferences(this);


        Intent i = getIntent(); // RETRIEVE OUR INTENT
        setTitle(i.getStringExtra("userInput"));
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

            MenuItem resetForm = menu.add("Setting");
            resetForm.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.activity_listscreen, new SettingsFragment());
                    ft.commit();

                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

    }

    public void startDeletion(){

        if (startStop) {
            timerHandler.removeCallbacks(timerRunnable);
            startStop = false;
            startDeletion.setTitle("Pick for Me!");
            listFragment.resumeScrolling();
        } else {
            timerHandler.postDelayed(timerRunnable, 0);
            startStop = true;
            startDeletion.setTitle("Pause");
            listFragment.stopScrolling();
        }
    }


    public void randomDeletion(){
        Random r = new Random();
        int i1 = r.nextInt(resturantsList.size() - 0) + 0;
        Resturant resturant = new Resturant();
        resturant = resturantsList.get(i1);
        Toast.makeText(this, resturant.getName() + " has been deleted!", Toast.LENGTH_SHORT).show();
        resturantsList.remove(i1);
        listFragment.updateDisplay(resturantsList, true);
    }

    public void displayFeatured(){
        startDeletion.setTitle("");
        String name = resturantsList.get(0).getName();
        String phone = resturantsList.get(0).getFormattedPhone();
        String address = resturantsList.get(0).getFormattedAddress();
        String imgUrl = resturantsList.get(0).getImgUrl();
        FeaturedFragment frag = FeaturedFragment.newInstance(name, phone, address, imgUrl);
        getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, frag, FeaturedFragment.TAG).commit();
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

    @Override
    public void toDetails() {

        timerHandler.removeCallbacks(timerRunnable);
        startStop = false;
        startDeletion.setTitle("Pick for Me!");

        Intent intent = new Intent(RandomDeletionActivity.this, DetailsActivity.class);
        Resturant resturant = resturantsList.get(0);

        intent.putExtra("name", resturant.getName());
        intent.putExtra("distance", String.valueOf(Math.round(resturant.getDistance() * 100.0) / 100.0));
        intent.putExtra("phone", resturant.getFormattedPhone());
        intent.putExtra("address", resturant.getFormattedAddress());
        intent.putExtra("review", resturant.getText());
        intent.putExtra("username", resturant.getFirstName());
        intent.putExtra("rating", String.valueOf(resturant.getRating()));
        intent.putExtra("lat", resturant.getLat());
        intent.putExtra("lng", resturant.getLng());

        startActivity(intent);
    }

    @Override
    protected void onDestroy() {

        timerHandler.removeCallbacks(timerRunnable);

        super.onDestroy();
    }

    @Override
    public void pushItemSelected(int _position) {

        timerHandler.removeCallbacks(timerRunnable);
        startStop = false;
        startDeletion.setTitle("Pick for Me!");

        Intent intent = new Intent(RandomDeletionActivity.this, DetailsActivity.class);
        Resturant resturant = resturantsList.get(_position);

        intent.putExtra("name", resturant.getName());
        intent.putExtra("distance", String.valueOf(Math.round(resturant.getDistance() * 100.0) / 100.0));
        intent.putExtra("phone", resturant.getFormattedPhone());
        intent.putExtra("address", resturant.getFormattedAddress());
        intent.putExtra("review", resturant.getText());
        intent.putExtra("username", resturant.getFirstName());
        intent.putExtra("rating", String.valueOf(resturant.getRating()));
        intent.putExtra("lat", resturant.getLat());
        intent.putExtra("lng", resturant.getLng());

        startActivity(intent);

    }

    @Override
    public void secondsDelay(int seconds) {

        SharedPreferences.Editor edit = defaultPrefs.edit();
        edit.putInt("seconds", seconds);
        edit.apply();

            listFragment = MasterListFragment.newInstance();
            getFragmentManager().beginTransaction().replace(R.id.activity_listscreen, listFragment, MasterListFragment.TAG).commit();
            listFragment.updateDisplay(resturantsList, false);

    }
}
