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
import android.widget.Toast;

import java.util.ArrayList;

import challenge.arctouch.br.com.routesearcher.R;
import challenge.arctouch.br.com.routesearcher.activities.RouteDetailsActivity;
import challenge.arctouch.br.com.routesearcher.domains.Route;

/**
 * Adapter responsible for the exhibition of the result of the search by
 * stop name on the Route Search Screen.
 * It creates a link for each item to the respective Route Details.
 * Created by Alvaro on 06/08/2015.
 */
public class RouteListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Route> routes;

    public RouteListAdapter(Context context, ArrayList<Route> routes){
        this.context = context;
        this.routes = routes;
    }

    public int getCount() {
        return routes.size();
    }

    public Object getItem(int position){
        return routes.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.route_list_item, null);

        Route route = routes.get(position);

        TextView tvRouteFullName = (TextView) v.findViewById(R.id.tv_route_full_name);
        final String routeTitle = route.getShortName()+" - "+route.getLongName();
        tvRouteFullName.setText(routeTitle);
        final String routeId = route.getId();
        LinearLayout rootLinearLayout = (LinearLayout) v.findViewById(R.id.ll_route_list_item_root);
        rootLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RouteDetailsActivity.class);
                intent.putExtra("routeTitle", routeTitle);
                intent.putExtra("routeId", routeId);
                context.startActivity(intent);
                //Toast.makeText(context, "click na rota, deve levar para os detalhes", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }
}
