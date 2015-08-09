package com.hs.cardviewlist.network;

import com.google.gson.JsonObject;

import retrofit.http.GET;

/**
 * Created by Hannan on 06/08/15.
 */
public interface RestaurantListAPI {
    @GET("/task_data.txt")
    JsonObject getAllRestaurantsAround();
}
