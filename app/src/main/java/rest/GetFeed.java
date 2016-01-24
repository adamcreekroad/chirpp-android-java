package rest;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by RCI Studios on 1/24/2016.
 */
public class GetFeed {
    private static GetFeed mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private GetFeed(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized GetFeed getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GetFeed(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}
