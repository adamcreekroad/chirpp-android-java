package online.chirpp.www.chirpp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import models.Post;
import models.User;
import rest.RestClient;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class HomeFeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProgressDialog dialog = ProgressDialog.show(this, "", "loading...");
        RestClient.ChirppApiInterface service = RestClient.getClient();
        Call<Post> call = service.getAllPosts();
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Response<Post> response) {
                dialog.dismiss();
                Log.d("MainActivity", "Status Code = " + response.code());
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    Post result = response.body();
                    Log.d("MainActivity", "response = " + new Gson().toJson(result));
                } else {
                    // response received but request not successful (like 400,401,403 etc)
                    //Handle errors

                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });
    }
}
