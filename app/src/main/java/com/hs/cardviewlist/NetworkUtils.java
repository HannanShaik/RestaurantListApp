package com.hs.cardviewlist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * @author mario
 */
public class NetworkUtils {
    private Context context;
    private static NetworkUtils sharedInstance;

    private NetworkUtils() {
    }

    public static synchronized NetworkUtils getInstance(Context context) {
        if (sharedInstance == null) {
            sharedInstance = new NetworkUtils();
        }
        sharedInstance.context = context;
        return sharedInstance;
    }

    public Boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager)
                this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
