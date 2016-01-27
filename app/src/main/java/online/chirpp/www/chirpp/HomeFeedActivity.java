package online.chirpp.www.chirpp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;



import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapters.PostAdapter;
import models.Post;
import models.User;
import rest.GetFeed;
import rest.GsonRequest;

public class HomeFeedActivity extends AppCompatActivity {

    public static final String feedURL = "https://www.chirpp.online/api/v1/posts/home_feed";
    public static final String SHAREDPREFFILE = "temp";
    public static final String USERIDPREF = "uid";
    public static final String TOKENPREF = "tkn";
    public static final Integer pageNo = 1;

    private SwipeRefreshLayout mFeed;
    private ListView mListView;
    private PostAdapter mAdapter;
    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_home_feed);
    }

    @Override
    public void onStart() {
        super.onStart();

        mSpinner = (ProgressBar) findViewById(R.id.feed_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        mListView = (ListView) findViewById(R.id.newsFeed);
        mListView.setVisibility(View.GONE);
        mFeed = (SwipeRefreshLayout) findViewById(R.id.feed);

        mFeed.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeed();
            }
        });

        FloatingActionButton mNewPostButton = (FloatingActionButton) findViewById(R.id.fab);

        mNewPostButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newPost();
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (mListView == null || mListView.getChildCount() == 0) ? 0 : mListView.getChildAt(0).getTop();
                mFeed.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        fetchFeed();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void showData(List<Post> posts) {
        mAdapter = new PostAdapter(this, posts);
        mListView.setAdapter(mAdapter);
        mSpinner.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
    }

    public void fetchFeed() {

        SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
        final String authToken = prefs.getString(TOKENPREF, "shrdtkn");
        final String userID = prefs.getString(USERIDPREF, "shrduid");

        GsonRequest<Post[]> getNewsFeed = new GsonRequest<Post[]>(0, feedURL, Post[].class,
                new Response.Listener<Post[]>(){
                    @Override
            public void onResponse(Post[] response) {
                List<Post> posts = Arrays.asList(response);
                showData(posts);
                mFeed.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("HomeFeedActivity", error.toString());
                mSpinner.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                returnToLogin();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + authToken + ", username=" + userID);
                return map;
            }
        };
        GetFeed.getInstance(this).addToRequestQueue(getNewsFeed);
    }

    public void returnToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void newPost() {
        Intent intent = new Intent(this, NewPostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

}
