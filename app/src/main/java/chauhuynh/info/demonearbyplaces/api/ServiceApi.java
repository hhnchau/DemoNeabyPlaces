package chauhuynh.info.demonearbyplaces.api;

import chauhuynh.info.demonearbyplaces.model.PlaceDetail;
import chauhuynh.info.demonearbyplaces.model.Places;
import chauhuynh.info.demonearbyplaces.model.Suggest;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


/**
 * Created by Chau Huynh on 3/2/2018.
 */

public interface ServiceApi {

    @GET
    Call<Places> getNearbyPlaces(@Url String url);

    @GET
    Call<PlaceDetail> getDetailPlace(@Url String url);

    @GET
    Call<Suggest> getSuggestSearch(@Url String url);

}
