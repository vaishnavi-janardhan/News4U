package com.vaishjanardhan.news4u;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>,
        SwipeRefreshLayout.OnRefreshListener {

    public static final String LOG_TAG = MainActivity.class.getName();

    //private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/" +
    //        "search?&show-tags=contributor&api-key=7c9ff97e-bafe-4a5a-a702-85e92de79564";
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/" +
            "search";

    /**
     * Constant value for the news loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWS_LOADER_ID = 1;

    TextView emptyTextView;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;

    /**
     * Adapter for the list of news articles
     */
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.DarkTheme);

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        String theme = sharedPreferences.getString(
                getString(R.string.settings_theme_key),
                getString(R.string.settings_theme_default)
        );

        if (theme.equals(getString(R.string.settings_theme_dark_value)))
            setTheme(R.style.DarkTheme);
        else if (theme.equals(getString(R.string.settings_theme_light_value)))
            setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Find a reference to the {@link ListView} in the layout
        final ListView newsListView = findViewById(R.id.list);

        emptyTextView = findViewById(R.id.empty_text_view);

        progressBar = findViewById(R.id.progress_bar);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        // Create a new {@link ArrayAdapter} of news articles
        adapter = new NewsAdapter(this, new ArrayList<News>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsListView.setAdapter(adapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network

        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            progressBar.setVisibility(View.GONE);
            // Update empty state with no connection error message
            emptyTextView.setText(R.string.no_internet_connection);
        }
        newsListView.setEmptyView(emptyTextView);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current news item that was clicked on
                News currentNews = adapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                assert currentNews != null;
                Uri newsUri = Uri.parse(currentNews.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    // onCreateLoader instantiates and returns a new Loader for the given ID
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        String category = sharedPreferences.getString(
                getString(R.string.settings_category_key),
                getString(R.string.settings_category_default)
        );

        String noOfItems = sharedPreferences.getString(
                getString(R.string.settings_number_key),
                getString(R.string.settings_number_default)
        );

        Log.i(LOG_TAG, "category = " + category);

        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `show-tags=contributor`
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "7c9ff97e-bafe-4a5a-a702-85e92de79564");
        if (!category.equals(""))
            uriBuilder.appendQueryParameter("section", category);
        uriBuilder.appendQueryParameter("page-size", noOfItems);

        // Return the completed uri
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Clear the adapter of previous news data
        adapter.clear();

        Log.e(LOG_TAG, "OnLoadFinished");

        // If there is a valid list of {@link News}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }

        progressBar.setVisibility(View.GONE);

        swipeRefreshLayout.setRefreshing(false);

        emptyTextView.setText(R.string.empty_state);

    }


    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
    }

    @Override
    // This method initializes the contents of the activity's options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu specified in XML
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_refresh) {
            swipeRefreshLayout.setRefreshing(true);
            adapter.clear();
            getLoaderManager()
                    .restartLoader(NEWS_LOADER_ID, null, this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        Log.e(LOG_TAG, "onRefresh");
        adapter.clear();
        getLoaderManager()
                .restartLoader(NEWS_LOADER_ID, null, this);
    }
}