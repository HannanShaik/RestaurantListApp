package com.hs.cardviewlist.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hs.cardviewlist.R;
import com.hs.cardviewlist.model.Category;
import com.hs.cardviewlist.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Hannan on 09/08/15.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ListViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    List<Restaurant> restaurants = Collections.emptyList();
    private Restaurant restaurant;

    public RestaurantListAdapter(Context context,List<Restaurant> restaurants){
        inflater = LayoutInflater.from(context);
        this.restaurants = restaurants;
        this.context = context;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_list_item, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        restaurant = restaurants.get(position);
        context = holder.restaurantLogo.getContext();

        holder.restaurantName.setText(restaurant.BrandName);
        holder.restaurantOffers.setText(restaurant.NumCoupons + " Offers");
        String categories = "";
        for(Category category:restaurant.Categories){
            String BULLET_SYMBOL = "\u2022";
            categories += " " + BULLET_SYMBOL + " " + category.Name;
        }
        holder.restaurantCategories.setText(categories);

        Picasso.with(context)
                .load(restaurant.LogoURL)
                .into(holder.restaurantLogo);

        holder.restaurantDistance.setText(getDistanceToRestaurant() + "  " + getAreaName());

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        ImageView restaurantLogo;
        TextView restaurantName, restaurantOffers, restaurantCategories, restaurantDistance;

        public ListViewHolder(View itemView) {
            super(itemView);
            restaurantLogo = (ImageView) itemView.findViewById(R.id.iv_restaurant_logo);
            restaurantName = (TextView) itemView.findViewById(R.id.tv_restaurant_name);
            restaurantOffers = (TextView) itemView.findViewById(R.id.tv_restaurant_offers);
            restaurantCategories = (TextView) itemView.findViewById(R.id.tv_restaurant_categories);
            restaurantDistance = (TextView) itemView.findViewById(R.id.tv_restaurant_distance);

        }


    }

    public String getDistanceToRestaurant(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("RESTAURANT_APP", Context.MODE_PRIVATE);
        Location currentLocation = new Location("USER Location");
        String latitude = sharedPreferences.getString("LATITUDE",null);
        String longitude = sharedPreferences.getString("LONGITUDE",null);

        if(latitude!=null && longitude!=null) {
            currentLocation.setLatitude(Double.parseDouble(latitude));
            currentLocation.setLongitude(Double.parseDouble(longitude));

            float distanceToRestaurant = 0f;
            try {
                Location restaurantLocation = new Location(restaurant.BrandName);
                restaurantLocation.setLatitude(Double.parseDouble(restaurant.Latitude));
                restaurantLocation.setLongitude(Double.parseDouble(restaurant.Longitude));
                distanceToRestaurant = currentLocation.distanceTo(restaurantLocation);
            } catch (Exception e) {
                //handle number formatting exception
            }

            int distanceInKm = (int)(distanceToRestaurant/1000);
            return  distanceInKm + " km";
        }
        return "0 km";
    }

    public String getAreaName(){
        Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(Double.parseDouble(restaurant.Latitude), Double.parseDouble(restaurant.Longitude), 1);

            if(addresses!=null)
                return addresses.get(0).getSubLocality();
        } catch (Exception e){
            return "";
        }
        return "";
    }
}