package challenge.arctouch.br.com.routesearcher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import challenge.arctouch.br.com.routesearcher.R;
import challenge.arctouch.br.com.routesearcher.activities.RouteDetailsActivity;
import challenge.arctouch.br.com.routesearcher.domains.Route;

/**
 * Adapter responsible for the exhibition of the list of streets within a selected route.
 * Created by Alvaro on 08/08/2015.
 */
public class RouteStopsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> stopsList;

    public RouteStopsAdapter(Context context, ArrayList<String> stops){
        this.context = context;
        this.stopsList = stops;
    }

    public int getCount() {
        return stopsList.size();
    }

    public Object getItem(int position){
        return stopsList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.stops_info_list_item, null);

        String stopName = stopsList.get(position);

        TextView tvRouteFullName = (TextView) v.findViewById(R.id.tv_stops_info_name);
        tvRouteFullName.setText(stopName);

        return v;
    }


}
