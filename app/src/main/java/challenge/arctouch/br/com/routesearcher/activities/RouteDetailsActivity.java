package challenge.arctouch.br.com.routesearcher.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import challenge.arctouch.br.com.routesearcher.R;
import challenge.arctouch.br.com.routesearcher.adapters.RouteScheduleAdapter;
import challenge.arctouch.br.com.routesearcher.adapters.RouteStopsAdapter;
import challenge.arctouch.br.com.routesearcher.domains.DepartureInfo;
import challenge.arctouch.br.com.routesearcher.services.RequestsHandler;

public class RouteDetailsActivity extends ActionBarActivity {

    ArrayList<String> routeStreets;
    ArrayList<DepartureInfo> routeSchedule;

    //Utils
    static private ProgressDialog progressDialog;

    //XML Components
    Button btnBack;
    ListView lvStreets;
    ListView lvSchedules;
    TextView routeTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_details);

        routeTitle = (TextView) findViewById(R.id.tv_route_details_title);
        routeTitle.setText(getIntent().getStringExtra("routeTitle"));

        lvStreets = (ListView) findViewById(R.id.lv_route_streets);
        getRouteStopsInfo();
        lvSchedules = (ListView) findViewById(R.id.lv_route_schedules);

        btnBack = (Button) findViewById(R.id.bt_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Responsible for trigger a server request that brings the Departure Information for the
     * specified route. It handles the server response and creates a list of DepartureInfo that will
     * be injected on the listView in the UI.
     */
    private void getRouteScheduleInfo() {
        showDialog();
        JsonObject requestParameters = new JsonObject();
        JsonObject routeId = new JsonObject();
        routeId.addProperty("routeID",  getIntent().getStringExtra("routeId"));
        requestParameters.add("params", routeId);
        FutureCallback<Response<String>> callback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> respostaOb) {
                dismissDialog();
                if (respostaOb != null) {
                    if (e == null) {
                        try {
                            JSONObject result = new JSONObject(respostaOb.getResult());
                            JSONArray rows = result.getJSONArray("rows");
                            routeSchedule = new ArrayList<DepartureInfo>();
                            String scheduleType = "";
                            for(int i = 0; i < rows.length(); i++){
                                JSONObject currentSchedule = new JSONObject(rows.get(i).toString());
                                //creates a header separator do group the schedules info by calendar type
                                if(i == 0 || !currentSchedule.optString("calendar").equals(scheduleType)){
                                    scheduleType = currentSchedule.optString("calendar");
                                    DepartureInfo departureInfoSeparator = new DepartureInfo(true, scheduleType);
                                    routeSchedule.add(i, departureInfoSeparator);
                                }

                                DepartureInfo deptInfo = new DepartureInfo(currentSchedule.optString("id"),
                                        currentSchedule.optString("calendar"), currentSchedule.optString("time"));

                                routeSchedule.add(deptInfo);
                            }

                            RouteScheduleAdapter deptInfoAdapter = new RouteScheduleAdapter(RouteDetailsActivity.this, routeSchedule);
                            lvSchedules.setAdapter(deptInfoAdapter);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        };
        RequestsHandler.sendPostRequest(RouteDetailsActivity.this,
                RequestsHandler.FIND_DEPARTURES_BY_ROUTE_ID,
                requestParameters, callback);
    }

    /**
     * Responsible for trigger a server request that brings the Stops Information for the
     * specified route. It handles the server response and creates a list with the name of each
     * street within the route. Its also calls the method to put the Departure's Info togheter.
     */
    private void getRouteStopsInfo() {
        showDialog();
        JsonObject requestParameters = new JsonObject();
        JsonObject routeId = new JsonObject();
        routeId.addProperty("routeID",  getIntent().getStringExtra("routeId"));
        requestParameters.add("params", routeId);
        FutureCallback<Response<String>> callback = new FutureCallback<Response<String>>() {
            @Override
            public void onCompleted(Exception e, Response<String> respostaOb) {
                dismissDialog();
                if (respostaOb != null) {
                    if (e == null) {
                        try {
                            JSONObject result = new JSONObject(respostaOb.getResult());
                            JSONArray rows = result.getJSONArray("rows");
                            routeStreets = new ArrayList<String>();
                            for(int i = 0; i < rows.length(); i++){
                                routeStreets.add(new JSONObject(rows.get(i).toString()).optString("name"));
                            }

                            RouteStopsAdapter stopsAdapter = new RouteStopsAdapter(RouteDetailsActivity.this, routeStreets);
                            lvStreets.setAdapter(stopsAdapter);
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                getRouteScheduleInfo();
            }
        };
        RequestsHandler.sendPostRequest(RouteDetailsActivity.this,
                RequestsHandler.FIND_STOPS_BY_ROUTE_ID,
                requestParameters, callback);
    }

    /**
     * Start a dialog that 'locks' the screen while the request is processed.
     */
    private void showDialog(){
        if(progressDialog != null){
            if(!progressDialog.isShowing()){
                progressDialog = ProgressDialog.show(RouteDetailsActivity.this, " Carregando...", "Aguarde...", true, false);
            }
        }else{
            progressDialog = ProgressDialog.show( RouteDetailsActivity.this , " Carregando...", "Aguarde...", true, false);
        }
    }

    /**
     * Dismiss a dialog setting the app as 'free to go'.
     */
    private void dismissDialog(){
        if (progressDialog != null) {
            if (progressDialog.isShowing()) { //check if dialog is showing.

                //get the Context object that was used to great the dialog
                Context context = ((ContextWrapper) progressDialog.getContext()).getBaseContext();

                //if the Context used here was an activity AND it hasn't been finished or destroyed
                //then dismiss it
                if (context instanceof Activity) {
                    if (!((Activity) context).isFinishing())
                        progressDialog.dismiss();
                } else //if the Context used wasnt an Activity, then dismiss it too
                    progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }
}
