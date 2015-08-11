package com.hs.cardviewlist.network;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.retrofit.RetrofitObjectPersisterFactory;
import com.octo.android.robospice.retrofit.RetrofitSpiceService;

import java.io.File;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by Hannan on 06/08/15.
 */
public class CustomSpiceService extends RetrofitSpiceService {

    private RequestInterceptor requestInterceptor;

    @Override
    public void onCreate() {
        super.onCreate();
        setUpCustomGson();
    }

    @Override
    protected String getServerUrl() {
        //return YOUR SERVICE URL;
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new RetrofitObjectPersisterFactory(application, getConverter(), getCacheFolder()));
        return cacheManager;
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder restAdapter = super.createRestAdapterBuilder();
        restAdapter.setRequestInterceptor(getRequestInterceptor());
        restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
        return restAdapter;
    }

    public File getCacheFolder() {
        return null;
    }

    private RequestInterceptor getRequestInterceptor() {
        if (requestInterceptor == null) {
            requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade requestFacade) {
                    requestFacade.addHeader("Accept", "application/json");
                    requestFacade.addHeader("Content-Type", "application/json");
                }
            };
        }
        return requestInterceptor;
    }


    protected Converter createConverter() {
        return setUpCustomGson();
    }

    private Converter setUpCustomGson() {
        Gson gson = new GsonBuilder()
                .create();
        return new GsonConverter(gson);
    }

}
