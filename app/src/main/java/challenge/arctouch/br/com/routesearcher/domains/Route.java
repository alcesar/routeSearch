package challenge.arctouch.br.com.routesearcher.domains;

import org.json.JSONObject;

/**
 * Created by Alvaro on 06/08/2015.
 */
public class Route {

    String id;
    String shortName;
    String longName;
    String lastModifiedDate;
    String agencyId;

    public Route(JSONObject jRoute){
        this.id = jRoute.optString("id", null);
        this.shortName =  jRoute.optString("shortName", null);
        this.longName =  jRoute.optString("longName", null);
        this.lastModifiedDate =  jRoute.optString("lastModifiedDate", null);
        this.agencyId =  jRoute.optString("agencyId", null);
    }

    public String getAgencyId() {
        return agencyId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public String getLongName() {
        return longName;
    }

    public String getId() {
        return id;
    }
}
