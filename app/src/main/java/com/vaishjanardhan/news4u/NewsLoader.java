package com.vaishjanardhan.news4u;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

public class NewsLoader extends AsyncTaskLoader{
    /** Tag for log messages */
    private static final String LOG_TAG = NewsLoader.class.getName();
    /** Query URL */
    private String url;

    NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null) {
            Log.e(LOG_TAG,"No URLs found");
            return null;
        }

        //Perform network request, parse the url and extract a list of responses
        return QueryUtils.fetchNewsData(url);
    }

}
