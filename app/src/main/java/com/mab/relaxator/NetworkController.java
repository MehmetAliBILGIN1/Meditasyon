package com.mab.relaxator;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkController {
    private static Context mCtx;
    private static NetworkController mIsntance;
    private RequestQueue mRequestQueue;

    private NetworkController(Context context) {
        mCtx = context;
        this.mRequestQueue = getRequestQueue();
    }

    public static synchronized NetworkController getInstance(MainActivity conntext) {
        NetworkController networkController;
        synchronized (NetworkController.class) {
            if (mIsntance == null) {
                mIsntance = new NetworkController(conntext);
            }

            networkController = mIsntance;
        }
        return networkController;

    }

     RequestQueue getRequestQueue() {
        if (this.mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQueue().add(req);
    }
}
