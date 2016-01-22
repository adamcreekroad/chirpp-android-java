package rest;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;

import java.io.IOException;

import models.User;
import models.Post;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Ashiq Uz Zoha on 9/13/15.
 * Dhrubok Infotech Services Ltd.
 * ashiq.ayon@gmail.com
 */
public class RestClient {

    private static ChirppApiInterface chirppApiInterface ;
    private static String baseUrl = "https://www.chirpp.online" ;

    public static ChirppApiInterface getClient() {
        if (chirppApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient();
            okClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response response = chain.proceed(chain.request());
                    return response;
                }
            });

            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            chirppApiInterface = client.create(ChirppApiInterface.class);
        }
        return chirppApiInterface ;
    }

    public interface ChirppApiInterface {

        @POST("/api/v1/sessions")
        Call<User> createSession(@Query("user[username]") String username, @Query("user[password]") String password);

        @GET("/api/v1/posts")
        Call<Post> getAllPosts();

    }

}