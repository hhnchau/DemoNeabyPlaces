package chauhuynh.info.demonearbyplaces.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chauhuynh.info.demonearbyplaces.R;
import chauhuynh.info.demonearbyplaces.adapter.AdapterViewPager;
import chauhuynh.info.demonearbyplaces.model.Opening_hours;
import chauhuynh.info.demonearbyplaces.model.Photos;
import chauhuynh.info.demonearbyplaces.model.Result;
import chauhuynh.info.demonearbyplaces.model.Reviews;

/**
 * Created by Chau Huynh on 3/28/2018.
 */

public class DialogPlaceMoreDetail {
    private static DialogPlaceMoreDetail Instance = null;

    public static DialogPlaceMoreDetail getInstance() {
        if (Instance == null) {
            Instance = new DialogPlaceMoreDetail();
        }
        return Instance;
    }

    private int dots_count;
    private ImageView[] dots;

    public void show(final Context context, Result result) {
        if (context != null && context instanceof Activity && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.dialog_full_transparent_background);
            dialog.setOwnerActivity((Activity) context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.item_dialog_place_more_detail);
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


                ViewPager viewPager = dialog.findViewById(R.id.item_viewPager);
                LinearLayout indicator = dialog.findViewById(R.id.indicator_item_viewpager);

                List<String> mList = new ArrayList<>();
                Photos[] photosList = result.getPhotos();
                if (photosList != null) {
                    for (Photos aPhotosList : photosList) {
                        mList.add(aPhotosList.getPhoto_reference());
                    }


                    AdapterViewPager adapterItemViewPager = new AdapterViewPager(mList);
                    viewPager.setAdapter(adapterItemViewPager);
                    dots_count = adapterItemViewPager.getCount();
                    dots = new ImageView[dots_count];
                    for (int i = 0; i < dots_count; i++) {
                        dots[i] = new ImageView(context);
                        dots[i].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dot_non_active));

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(8, 0, 8, 0);

                        indicator.addView(dots[i], params);
                    }
                    dots[0].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dot_active));


                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            for (int i = 0; i < dots_count; i++) {
                                dots[i].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dot_non_active));
                            }

                            dots[position].setImageDrawable(ContextCompat.getDrawable(context.getApplicationContext(), R.drawable.dot_active));
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                }

                TextView textView = dialog.findViewById(R.id.txt_content);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Name: ").append(result.getName()).append("\n");
                stringBuilder.append("Address: ").append(result.getFormatted_address()).append("\n");
                stringBuilder.append("Phone: ").append(result.getFormatted_phone_number()).append("\n");
                Opening_hours opening_hours = result.getOpening_hours();
                if (opening_hours != null && opening_hours.getWeekday_text() != null && opening_hours.getWeekday_text().length > 0) {
                    stringBuilder.append("Open hours: ").append(opening_hours.getWeekday_text()[0]).append("\n");
                }
                stringBuilder.append("Rating: ").append(result.getRating()).append("\n");
                stringBuilder.append("Website: ").append(result.getWebsite()).append("\n");

                Reviews[] reviews = result.getReviews();
                if (reviews != null && reviews.length > 0) {
                    stringBuilder.append("Total Reviews: ").append(String.valueOf(reviews.length)).append("\n");
                    for (int i = 0; i < reviews.length; i++) {
                        stringBuilder.append("Reviews:").append(String.valueOf(i + 1)).append("\n");
                        stringBuilder.append("     +").append("Nickname: ").append(reviews[i].getAuthor_name()).append("\n");
                        stringBuilder.append("     +").append("Rating: ").append(reviews[i].getRating()).append("\n");
                        stringBuilder.append("     +").append("Content: ").append(reviews[i].getText()).append("\n");
                    }
                }

                textView.setText(stringBuilder);
                textView.setMovementMethod(new ScrollingMovementMethod());

            }
        }
    }
}
