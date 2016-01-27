package online.chirpp.www.chirpp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Post;
import rest.GetFeed;
import rest.GsonRequest;

public class NewPostActivity extends AppCompatActivity {

    public static final String postURL = "https://www.chirpp.online/api/v1/posts";
    public static final String SHAREDPREFFILE = "temp";
    public static final String USERIDPREF = "uid";
    public static final String TOKENPREF = "tkn";

    private EditText messageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_new_post);

        messageView = (EditText) findViewById(R.id.new_post_message);
        Button newPostBtn = (Button) findViewById(R.id.submit_post);

        newPostBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                submitPost();
            }
        });
    }

    private void submitPost() {

        Log.d("NewPostActivity", "attempting post submit");

        SharedPreferences prefs = getSharedPreferences(SHAREDPREFFILE, Context.MODE_PRIVATE);
        final String authToken = prefs.getString(TOKENPREF, "shrdtkn");
        final String userID = prefs.getString(USERIDPREF, "shrduid");
        final String message = messageView.getText().toString();

        messageView.setError(null);

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(message)) {
            messageView.setError(getString(R.string.error_field_required));
            focusView = messageView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            GsonRequest<Post> newPost = new GsonRequest<Post>(1, postURL, Post.class,
                    new Response.Listener<Post>() {
                        @Override
                        public void onResponse(Post response) {
                            Post post = response;
                            goToFeed();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("NewPostActivity", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + authToken + ", username=" + userID);
                    return map;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("post[message]", message);
                    return params;
                }
            };
            GetFeed.getInstance(this).addToRequestQueue(newPost);
        }
    }

    public void goToFeed() {
        Intent intent = new Intent(this, HomeFeedActivity.class);
        startActivity(intent);
    }
}
