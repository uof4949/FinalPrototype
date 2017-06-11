package kr.ac.gachon.finalprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.skp.Tmap.TMapPOIItem;

import java.util.ArrayList;

/**
 * Created by kalios on 2017-06-11.
 */

public class LocationsAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<TMapPOIItem> POIdata; // POI 목록을 담을 배열.
    private ArrayList<LocationItem> data; // location 목록을 담을 배열.
    private int layout;

    public LocationsAdapter (Context context, int layout, ArrayList<TMapPOIItem> POIdata, ArrayList<LocationItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.POIdata = POIdata;
        this.data = data;
        this.layout = layout;
    }
    @Override
    public int getCount() { // 리스트 안 Item의 개수를 센다.
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getLocName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(layout, parent, false);
        }

        LocationItem item = data.get(position);

        // 이름 연동
        TextView textLocName = (TextView) convertView.findViewById(R.id.TextLocName);
        textLocName.setText(item.getLocName().toString());

        // 주소 연동
        TextView textLocAddress = (TextView) convertView.findViewById(R.id.TextLocAddress);
        textLocAddress.setText(item.getLocAddress().toString());

        return convertView;
    }

    public void filter(String searchText) {

    }

}
