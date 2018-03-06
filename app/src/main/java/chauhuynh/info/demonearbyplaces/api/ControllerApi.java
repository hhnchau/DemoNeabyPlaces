package chauhuynh.info.demonearbyplaces.api;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import chauhuynh.info.demonearbyplaces.log.Logcat;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chau Huynh on 02/03/02017.
 */

public class ControllerApi extends Application {
    public Retrofit retrofit;
    public static ServiceApi serviceApi;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(new RequestInterceptor(this));
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();

        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl(UrlParams.url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            serviceApi = retrofit.create(ServiceApi.class);
        } catch (Exception e) {
            e.printStackTrace();
            if (retrofit == null) {
                Logcat.write("retrofit: ", "Cannot connect to server");

            } else {
                Logcat.write("retrofit: ", "Successfully connect to server");
            }
        }

    }
}
