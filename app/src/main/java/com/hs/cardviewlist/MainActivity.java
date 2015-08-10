package com.hs.cardviewlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hs.cardviewlist.fragment.RestaurantListFragment;
import com.hs.cardviewlist.network.CustomSpiceService;
import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private SpiceManager spiceManager;

    @Override
    protected void onStart() {
        super.onStart();
        getSpiceManager().start(this);
    }

    @Override
    protected void onStop() {
        getSpiceManager().shouldStop();
        super.onStop();
    }

    public SpiceManager getSpiceManager() {
        if (spiceManager == null) {
            spiceManager = new SpiceManager(CustomSpiceService.class);
        }
        return spiceManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showGPSAlertPopup();
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new RestaurantListFragment(), "IN YOUR CITY");
        adapter.addFragment(new RestaurantListFragment(), "NEAR BY");
        adapter.addFragment(new RestaurantListFragment(), "BEST OFFERS");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1,true);
        viewPager.setEnabled(false);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public void showGPSAlertPopup(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

}
