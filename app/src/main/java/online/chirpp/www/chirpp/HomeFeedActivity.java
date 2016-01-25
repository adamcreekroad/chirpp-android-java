package online.chirpp.www.chirpp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

        mSpinner = (ProgressBar)findViewById(R.id.feed_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        mListView = (ListView) findViewById(R.id.newsFeed);
        mListView.setVisibility(View.GONE);

        Intent intent = getIntent();
        final String authToken = intent.getStringExtra(LoginActivity.TOKENPREF);
        final String userID = intent.getStringExtra(LoginActivity.USERIDPREF);

        RequestQueue queue = Volley.newRequestQueue(this);

        GsonRequest<Post[]> getNewsFeed = new GsonRequest<Post[]>(0, feedURL, Post[].class,
            new Response.Listener<Post[]>(){
                @Override
                public void onResponse(Post[] response) {
                    List<Post> posts = Arrays.asList(response);

                    showData(posts);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("HomeFeedActivity", error.toString());
                    mSpinner.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
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
}
