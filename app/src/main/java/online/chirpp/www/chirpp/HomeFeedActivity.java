package online.chirpp.www.chirpp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.Arrays;
import java.util.List;

import adapters.PostAdapter;
import models.Post;
import rest.GetFeed;
import rest.GsonRequest;

public class HomeFeedActivity extends AppCompatActivity {

    public static final String feedURL = "https://www.chirpp.online/api/v1/posts";

    private ListView mListView;
    private PostAdapter mAdapter;
    private ProgressBar mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_feed);
    }

    @Override
    public void onStart() {
        super.onStart();

        mSpinner = (ProgressBar)findViewById(R.id.feed_spinner);
        mSpinner.setVisibility(View.VISIBLE);
        mListView = (ListView) findViewById(R.id.newsFeed);
        mListView.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://www.chirpp.online/api/v1/posts";

        GsonRequest<Post[]> getNewsFeed = new GsonRequest<Post[]>(url, Post[].class,
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
            });

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
