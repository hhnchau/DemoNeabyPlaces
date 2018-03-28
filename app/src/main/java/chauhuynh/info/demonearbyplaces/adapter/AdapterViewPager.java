package chauhuynh.info.demonearbyplaces.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import chauhuynh.info.demonearbyplaces.R;
import chauhuynh.info.demonearbyplaces.log.Logcat;


/**
 * Created by Chau Huynh on 2/21/2018.
 */

public class AdapterViewPager extends PagerAdapter {
    private List<String> mList;

    public AdapterViewPager(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager, container, false);

        ImageView img = view.findViewById(R.id.img);

        Picasso.with(container.getContext())
                .load(getPhotoOfPlaceUrl(container.getContext(), mList.get(position), 1000))
                .into(img);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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
