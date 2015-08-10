package com.hs.cardviewlist.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hs.cardviewlist.model.Category;
import com.hs.cardviewlist.model.Restaurant;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hannan on 06/08/15.
 */
public class RequestHandler {

    private final SpiceManager spiceManager;

    public RequestHandler(SpiceManager spiceManager){
        this.spiceManager = spiceManager;
    }

    public void getRestaurantsAround(final RequestListener<List<Restaurant>> requestListener){
        GetRestaurantListRequest getRestaurantListRequest = new GetRestaurantListRequest();
        spiceManager.execute(getRestaurantListRequest, "RESTAURANT_LIST", DurationInMillis.ALWAYS_RETURNED, new RequestListener<JsonObject>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                requestListener.onRequestFailure(spiceException);
            }

            @Override
            public void onRequestSuccess(JsonObject jsonObject) {
                List<Restaurant> restaurants = new ArrayList<>();
                Gson gson = new GsonBuilder().create();
                if (jsonObject.getAsJsonObject("status").get("rcode").getAsInt() == 200) {
                    //Remove old entries
                    Restaurant.truncate();

                    JsonObject dataJson = jsonObject.getAsJsonObject("data");
                    for (Map.Entry<String, JsonElement> entry : dataJson.entrySet()) {
                        JsonObject restaurantJson = dataJson.getAsJsonObject(entry.getKey());

                        Restaurant restaurant = gson.fromJson(restaurantJson, Restaurant.class);
                        restaurant.categoriesList = gson.toJson(restaurant.Categories);
                        restaurant.save();

                        restaurants.add(restaurant);
                    }
                    requestListener.onRequestSuccess(restaurants);
                }
            }
        });
    }
}
