package chauhuynh.info.demonearbyplaces.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chauhuynh.info.demonearbyplaces.R;
import chauhuynh.info.demonearbyplaces.model.GoogleSuggestSearch;
import chauhuynh.info.demonearbyplaces.model.Predictions;

/**
 * Created by appro on 02/03/2018.
 */

public class AdapterSuggestSearch extends BaseAdapter {
    private Context myContext;
    private List<Predictions> list;

    public AdapterSuggestSearch(Context myContext, List<Predictions> list) {
        this.myContext = myContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder {
        private TextView txtName, txtAddress;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = view;
        ViewHolder viewHolder = new ViewHolder();

        if (rowView == null && inflater != null) {
            rowView = inflater.inflate(R.layout.item_suggest_search, null);
            //Init
            viewHolder.txtName = rowView.findViewById(R.id.tv_name);
            //viewHolder.txtAddress = rowView.findViewById(R.id.tv_address);


            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) rowView.getTag();
        }

        viewHolder.txtName.setText(list.get(i).getDescription());
        //viewHolder.txtAddress.setText(list.get(i).getId());

        return rowView;
    }
}
