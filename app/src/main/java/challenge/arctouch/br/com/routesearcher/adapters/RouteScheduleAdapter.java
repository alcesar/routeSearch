package challenge.arctouch.br.com.routesearcher.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import challenge.arctouch.br.com.routesearcher.R;
import challenge.arctouch.br.com.routesearcher.domains.DepartureInfo;

/**
 * Adapter resposible for the exhibition of the Route Departure Infos on the
 * Route Details Screen. It keeps the control on the kind of view that will be
 * displayed (Separators for weekdays or weekends, or regular rows with the time).
 *
 * Created by Alvaro on 08/08/2015.
 */
public class RouteScheduleAdapter extends BaseAdapter  {

    private Context context;
    private ArrayList<DepartureInfo> departuresList;

    // Flag for Separators
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 0;
    // Flag for Regular rows
    private static final int ITEM_VIEW_TYPE_REGULAR = 1;

    private static final int ITEM_VIEW_TYPE_COUNT = 2;

    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isSection = departuresList.get(position).isSeparator;
        if (isSection) {
            return ITEM_VIEW_TYPE_SEPARATOR;
        }
        else {
            return ITEM_VIEW_TYPE_REGULAR;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != ITEM_VIEW_TYPE_SEPARATOR;
    }

    public RouteScheduleAdapter(Context context, ArrayList<DepartureInfo> departuresList){
        this.context = context;
        this.departuresList = departuresList;
    }

    public int getCount() {
        return departuresList.size();
    }

    public Object getItem(int position){
        return departuresList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @SuppressLint("ViewHolder")
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v;
        int itemViewType = getItemViewType(position);
        //inflates the correct item view based on the view type required (Separator or Regular Row)
        if (convertView == null) {
            if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
                // If its a section ?
                v = inflater.inflate(R.layout.list_section_divider, null);
            }
            else {
                // Regular row
                v = inflater.inflate(R.layout.departure_info_list_item, null);
            }
        }
        else {
            v = convertView;
        }

        //Setup the view with correct data
        DepartureInfo deptInfo = departuresList.get(position);
        if(itemViewType == ITEM_VIEW_TYPE_SEPARATOR){
            TextView separatorView = (TextView) v.findViewById(R.id.tv_list_header_separator_title);
            separatorView.setText(deptInfo.separatorHeader);
        } else {
            TextView separatorView = (TextView) v.findViewById(R.id.tv_departure_info_time);
            separatorView.setText(deptInfo.getTime());
        }
        return v;
    }

}
