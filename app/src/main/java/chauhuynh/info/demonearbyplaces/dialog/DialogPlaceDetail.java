package chauhuynh.info.demonearbyplaces.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import chauhuynh.info.demonearbyplaces.R;
import chauhuynh.info.demonearbyplaces.api.CallBackApi;
import chauhuynh.info.demonearbyplaces.api.HandleApi;
import chauhuynh.info.demonearbyplaces.log.Logcat;
import chauhuynh.info.demonearbyplaces.model.Geometry;
import chauhuynh.info.demonearbyplaces.model.Photos;
import chauhuynh.info.demonearbyplaces.model.PlaceDetail;
import chauhuynh.info.demonearbyplaces.model.Result;
import chauhuynh.info.demonearbyplaces.model.Results;

/**
 * Created by Chau Huynh on 05/03/2018.
 */

public class DialogPlaceDetail {
    private static DialogPlaceDetail Instance = null;

    public static DialogPlaceDetail getInstance() {
        if (Instance == null) {
            Instance = new DialogPlaceDetail();
        }
        return Instance;
    }

    public void show(final Context context, Results results) {
        if (context != null && context instanceof Activity && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.dialog_full_transparent_background);
            dialog.setOwnerActivity((Activity) context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.item_dialog_place_detail);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();

                dialog.findViewById(R.id.dialog_input).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final TextView content = dialog.findViewById(R.id.txt_content);


                ImageView img = dialog.findViewById(R.id.img_detail);
                if (results.getPhotos() != null && results.getPhotos().length > 0) {
                    Picasso.with(context)
                            .load(getPhotoOfPlaceUrl(context, results.getPhotos()[0].getPhoto_reference(), 1000))
                            .into(img);
                }

                String p = results.getPlace_id();
                HandleApi.getInstance().getDetailPlaces(context, p, new CallBackApi() {
                    @Override
                    public void resultApi(Object object) {
                        Result result = (Result) object;
                        if (result != null) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(result.getName()).append("\n");
                            stringBuilder.append(result.getVicinity()).append("\n");
                            stringBuilder.append("Rating: ").append(result.getRating()).append("\n");

                            content.setText(stringBuilder);
                        }
                    }
                });

            }
        }
    }

    private String getPhotoOfPlaceUrl(Context context, String photo_reference, int maxwidth) {
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo");
        stringBuilder.append("?maxwidth=").append(maxwidth);
        stringBuilder.append("&photoreference=").append(photo_reference);
        stringBuilder.append("&key=").append(context.getResources().getString(R.string.map_api_key));
        Logcat.write("PHOTO-URL", stringBuilder.toString());
        return stringBuilder.toString();
    }
}
