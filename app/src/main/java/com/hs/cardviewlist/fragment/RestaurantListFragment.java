package com.hs.cardviewlist.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.hs.cardviewlist.MainActivity;
import com.hs.cardviewlist.NetworkUtils;
import com.hs.cardviewlist.R;
import com.hs.cardviewlist.adapter.RestaurantListAdapter;
import com.hs.cardviewlist.model.Restaurant;
import com.hs.cardviewlist.network.RequestHandler;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hannan on 06/08/15.
 */
public class RestaurantListFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public List<Restaurant> restaurants;
    RestaurantListAdapter restaurantListAdapter;
    ProgressDialog pd;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public RestaurantListFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        RecyclerView restaurantListView = (RecyclerView) rootView.findViewById(R.id.rv_restaurant_list);

        restaurants = new ArrayList<>();
        restaurantListAdapter = new RestaurantListAdapter(getActivity(), restaurants);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        restaurantListView.setLayoutManager(layoutManager);
        restaurantListView.setItemAnimator(new DefaultItemAnimator());
        restaurantListView.setAdapter(restaurantListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.sr_swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.getInstance(getActivity()).isConnected()) {
                    Log.e("", "Connected");
                    syncRestaurants();
                } else {
                    Toast.makeText(getActivity(), "No Internet Connection! Please try later!", Toast.LENGTH_SHORT).show();
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
            if(Restaurant.getAll().size() == 0) {
                pd = ProgressDialog.show(getActivity(), "Please Wait...", "Fetching near by restaurants!");
                syncRestaurants();
            }
        }

        this.restaurants.clear();
        this.restaurants.addAll(Restaurant.getAll());
        restaurantListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if(mLastLocation!=null) {
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("RESTAURANT_APP", Context.MODE_PRIVATE).edit();
            editor.putString("LATITUDE", mLastLocation.getLatitude() + "");
            editor.putString("LONGITUDE", mLastLocation.getLongitude() + "");
            editor.apply();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("RESTAURANT APP", "Connection to get location is suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("RESTAURANT APP", "Failed to get location");
    }

    private void syncRestaurants(){
        new RequestHandler(((MainActivity) getActivity()).getSpiceManager())
                .getRestaurantsAround(new RestaurantListRequestListener());
        mSwipeRefreshLayout.setRefreshing(true);

    }

    class RestaurantListRequestListener implements RequestListener<List<Restaurant>>{

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            pd.dismiss();
            Toast.makeText(getActivity(),"Error occurred While fetching restaurant list!",Toast.LENGTH_SHORT).show();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            if(pd!=null && pd.isShowing())
                pd.dismiss();
        }

        @Override
        public void onRequestSuccess(List<Restaurant> restaurantsList) {

            restaurants.clear();
            restaurants.addAll(restaurantsList);
            restaurantListAdapter.notifyDataSetChanged();

            if(pd!=null && pd.isShowing())
                pd.dismiss();
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

}
