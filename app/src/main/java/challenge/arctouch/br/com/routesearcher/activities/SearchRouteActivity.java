package challenge.arctouch.br.com.routesearcher.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import challenge.arctouch.br.com.routesearcher.R;
import challenge.arctouch.br.com.routesearcher.adapters.RouteListAdapter;
import challenge.arctouch.br.com.routesearcher.domains.Route;
import challenge.arctouch.br.com.routesearcher.services.RequestsHandler;


public class SearchRouteActivity extends ActionBarActivity {

    //XML Components
    EditText edtSearchInfo;
    Button btnSearch;
    ListView lvRoutesResult;
    RelativeLayout rlNoResultsFound;

    ArrayList<Route> routeList;
    RouteListAdapter routeListAdapter;

    //Utils
    static private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_route);

        //Set the xml references to the components
        rlNoResultsFound = (RelativeLayout) findViewById(R.id.rl_without_routes);
        edtSearchInfo = (EditText) findViewById(R.id.edt_search_info);
        lvRoutesResult = (ListView) findViewById(R.id.lv_routes_result);
        btnSearch = (Button) findViewById(R.id.bt_search);

        //set listener to search button
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
                JsonObject requestParameters = new JsonObject();
                JsonObject stopName = new JsonObject();
                stopName.addProperty("stopName",  "%"+edtSearchInfo.getText().toString()+"%");
                requestParameters.add("params", stopName);
                FutureCallback<Response<String>> callback = new FutureCallback<Response<String>>() {
                    @Override
                    public void onCompleted(Exception e, Response<String> respostaOb) {
                        dismissDialog();
                        if (respostaOb != null) {
                            if (e == null) {
                                try {
                                    JSONObject result = new JSONObject(respostaOb.getResult());
                                    JSONArray rows = result.getJSONArray("rows");
                                    routeList = new ArrayList<Route>();
                                    for(int index = 0; index < rows.length(); index++){
                                        Route route = new Route(new JSONObject(rows.get(index).toString()));
                                        routeList.add(route);
                                    }
                                    if(routeList != null && routeList.size() > 0) {
                                        rlNoResultsFound.setVisibility(View.GONE);
                                        lvRoutesResult.setVisibility(View.VISIBLE);
                                        routeListAdapter = new RouteListAdapter(SearchRouteActivity.this, routeList);
                                        lvRoutesResult.setAdapter(routeListAdapter);
                                    } else {
                                        rlNoResultsFound.setVisibility(View.VISIBLE);
                                        lvRoutesResult.setVisibility(View.GONE);
                                    }
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }
                };
                RequestsHandler.sendPostRequest(SearchRouteActivity.this,
                        RequestsHandler.FIND_ROUTE_BY_STOP_NAME,
                        requestParameters, callback);
            }
        });
    }

    /**
     * Start a dialog that 'locks' the screen while the request is processed.
     */
    private void showDialog(){
        if(progressDialog != null){
            if(!progressDialog.isShowing()){
                progressDialog = ProgressDialog.show(SearchRouteActivity.this, " Carregando...", "Aguarde...", true, false);
            }
        }else{
            progressDialog = ProgressDialog.show( SearchRouteActivity.this , " Carregando...", "Aguarde...", true, false);
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
