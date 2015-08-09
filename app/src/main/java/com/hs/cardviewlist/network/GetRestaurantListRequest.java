package com.hs.cardviewlist.network;

import com.google.gson.JsonObject;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by Hannan on 06/08/15.
 */
public class GetRestaurantListRequest extends RetrofitSpiceRequest<JsonObject,RestaurantListAPI> {

    public GetRestaurantListRequest(){
        super(JsonObject.class,RestaurantListAPI.class);
    }
    @Override
    public JsonObject loadDataFromNetwork() throws Exception {
        return getService().getAllRestaurantsAround();
    }
}
