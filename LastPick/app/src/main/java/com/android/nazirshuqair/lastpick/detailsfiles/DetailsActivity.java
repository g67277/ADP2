package com.android.nazirshuqair.lastpick.detailsfiles;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.android.nazirshuqair.lastpick.R;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MapSearchFragment;
import com.android.nazirshuqair.lastpick.mainscreenfragments.MyMapFragment;

/**
 * Created by nazirshuqair on 12/16/14.
 */
public class DetailsActivity extends Activity implements MapOverlayDetailFragment.BackClickListener {

    DisplayDetailFragment displayDetailFragment;
    MapOverlayDetailFragment mapOverlayDetailFragment;
    MapDetailFragment mapDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        ActionBar abr = getActionBar();
        abr.hide();

        Intent intent = getIntent();

        if (savedInstanceState == null) {

            mapDetailFragment = MapDetailFragment.newInstance(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0), intent.getStringExtra("name"));
            getFragmentManager().beginTransaction().replace(R.id.detailContainer1, mapDetailFragment, MapDetailFragment.TAG).commit();

            displayDetailFragment = DisplayDetailFragment.newInstance(intent.getStringExtra("phone"), intent.getStringExtra("address"), intent.getStringExtra("review"), intent.getStringExtra("username"));
            getFragmentManager().beginTransaction().replace(R.id.detailContainer2, displayDetailFragment, DisplayDetailFragment.TAG).commit();

            mapOverlayDetailFragment = MapOverlayDetailFragment.newInstance(intent.getStringExtra("name"), intent.getStringExtra("distance"), intent.getStringExtra("rating"));
            getFragmentManager().beginTransaction().replace(R.id.map_container_overlay, mapOverlayDetailFragment, MapOverlayDetailFragment.TAG).commit();

            //MapSearchFragment frag = MapSearchFragment.newInstance();
            //getFragmentManager().beginTransaction().replace(R.id.overly_container, frag, MapSearchFragment.TAG).commit();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void backToList() {
        finish();
    }
}
