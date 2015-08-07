package challenge.archtouch.br.com.routesearcher.services;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Alvaro on 06/08/2015.
 * This class is responsible for handling all the requests made by this app,
 * as a way to centralize the web services request's flow.
 */
public class RequestsHandler {

    static final public int CONNECTION_TIMEOUT = 10000;
    static final public String FIND_ROUTE_BY_STOP_NAME = "https://api.appglu.com/v1/queries/findRoutesByStopName/run";
    static final public String FIND_STOPS_BY_ROUTE_ID = "https://api.appglu.com/v1/queries/findStopsByRouteId/run";
    static final public String FIND_DEPARTURES_BY_ROUTE_ID = "https://api.appglu.com/v1/queries/findDeparturesByRouteId/run";

    static final String BASIC_AUTH = "Basic " + Base64.encodeToString("WKD4N7YMA1uiM8V:DtdTtzMLQlA0hk2C1Yi5pLyVIlAQ68".getBytes(), Base64.NO_WRAP);

    /**
     * Send a GET request to the web service, using the provided 'callback' action to handle the service response.
     * @param context - context associated with the current requester;
     * @param url - address to the desired service, including authentication parameters;
     * @param callback - planned treatment to the service's response.
     */
    public static  void sendGetRequest(final Context context, String url, FutureCallback callback){
        Ion.with(context).load("GET", url)
                .setHeader("Content-Type", "application/json")
                .setHeader("X-AppGlu-Environment", "staging")
                .setHeader("charset", "utf-8")
                .setHeader("Authorization", BASIC_AUTH)
                .setTimeout(CONNECTION_TIMEOUT)
                .asString()
                .withResponse()
                .setCallback(callback);
    }

    /**
     * Send a POST request to the web service, using the provided 'callback' action to handle the service response.
     * @param context - context associated with the current requester;
     * @param url - address to the desired service, including authentication parameters;
     * @param json - object containing all the dynamic parameters necessary to the request composition;
     * @param callback - planned treatment to the service's response.
     */
    public static void sendPostRequest(final Context context, String url, final JsonObject json, FutureCallback callback) {
        Ion.with(context).load("POST", url)
                .setHeader("Content-Type", "application/json")
                .setHeader("X-AppGlu-Environment", "staging")
                .setHeader("charset", "utf-8")
                .setHeader("Authorization", BASIC_AUTH)
                .setTimeout(CONNECTION_TIMEOUT)
                .setJsonObjectBody(json)
                .asString()
                .withResponse()
                .setCallback(callback);
    }
}
