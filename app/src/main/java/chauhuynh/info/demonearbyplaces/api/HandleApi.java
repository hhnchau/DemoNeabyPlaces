package chauhuynh.info.demonearbyplaces.api;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import chauhuynh.info.demonearbyplaces.R;
import chauhuynh.info.demonearbyplaces.log.Logcat;
import chauhuynh.info.demonearbyplaces.model.Geometry;
import chauhuynh.info.demonearbyplaces.model.PlaceDetail;
import chauhuynh.info.demonearbyplaces.model.Places;
import chauhuynh.info.demonearbyplaces.model.Predictions;
import chauhuynh.info.demonearbyplaces.model.Result;
import chauhuynh.info.demonearbyplaces.model.Results;
import chauhuynh.info.demonearbyplaces.model.Suggest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Chau Huynh on 05/03/2018.
 */

public class HandleApi {
    private static HandleApi Instance = null;

    public static HandleApi getInstance() {
        if (Instance == null) {
            Instance = new HandleApi();
        }
        return Instance;
    }

    public void getSuggestSearch(final Context context, String keyword, final CallBackListApi callBackListApi) {
        ControllerApi.serviceApi.getSuggestSearch(getSuggestSearchUrl(context, keyword)).enqueue(new Callback<Suggest>() {
            @Override
            public void onResponse(Call<Suggest> call, Response<Suggest> response) {
                if (response.isSuccessful()) {
                    Suggest suggest = response.body();
                    if (suggest != null) {
                        if (suggest.getStatus().equals("OK")) {
                            List<Predictions> predictionsList = suggest.getPredictions();
                            List<Object> list = new ArrayList<>();
                            list.addAll(predictionsList);
                            callBackListApi.resultApi(list);
                        } else {
                            Toast.makeText(context, suggest.getStatus(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Suggest> call, Throwable t) {
                Logcat.write("", "");
            }
        });
    }

    public void getNearbyPlaces(Context context, double latitude, double longitude, String type, final CallBackListApi callBackListApi) {
        ControllerApi.serviceApi.getNearbyPlaces(getNearbyPlacesUrl(context, latitude, longitude, type)).enqueue(new Callback<Places>() {
            @Override
            public void onResponse(Call<Places> call, Response<Places> response) {
                if (response.isSuccessful()) {
                    Places places = response.body();
                    if (places != null) {
                        List<Results> resultsList = places.getResults();
                        List<Object> list = new ArrayList<>();
                        list.addAll(resultsList);
                        callBackListApi.resultApi(list);
                    }
                }
            }

            @Override
            public void onFailure(Call<Places> call, Throwable t) {

            }
        });
    }

    public void getDetailPlaces(Context context, final String place_id, final CallBackApi callBackApi) {
        ControllerApi.serviceApi.getDetailPlace(getDetailPlacesUrl(context, place_id)).enqueue(new Callback<PlaceDetail>() {
            @Override
            public void onResponse(Call<PlaceDetail> call, Response<PlaceDetail> response) {
                if (response.isSuccessful()) {
                    PlaceDetail placeDetail = response.body();
                    if (placeDetail != null) {
                        Result result = placeDetail.getResult();
                        if (result != null) {

                            callBackApi.resultApi(result);

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<PlaceDetail> call, Throwable t) {

            }
        });
    }

    private String getSuggestSearchUrl(Context context, String keyword) {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?");
        stringBuilder.append("input=").append(keyword);
        stringBuilder.append("&types=geocode");
        stringBuilder.append("&sensor=false");
        stringBuilder.append("&key=").append(context.getResources().getString(R.string.map_api_key));
        stringBuilder.append("&components=country:vn");
        Logcat.write("URL-SUGGEST-SEARCH", stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String getNearbyPlacesUrl(Context context, double latitude, double longitude, String type) {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        stringBuilder.append("location=").append(latitude).append(",").append(longitude);
        stringBuilder.append("&radius=" + 1000);
        stringBuilder.append("&type=").append(type);
        stringBuilder.append("&sensor=true");
        stringBuilder.append("&key=").append(context.getResources().getString(R.string.map_api_key));

        Logcat.write("SUGGEST-URL", stringBuilder.toString());

        return stringBuilder.toString();
    }

    private String getDetailPlacesUrl(Context context, String place_id) {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
        stringBuilder.append("?placeid=").append(place_id);
        stringBuilder.append("&key=").append(context.getResources().getString(R.string.map_api_key));
        Logcat.write("DETAIL-URL", stringBuilder.toString());
        return stringBuilder.toString();
    }
}
