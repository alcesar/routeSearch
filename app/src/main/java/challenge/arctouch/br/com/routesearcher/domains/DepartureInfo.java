package challenge.arctouch.br.com.routesearcher.domains;

/**
 * Created by Alvaro on 08/08/2015.
 */
public class DepartureInfo {

    String id;
    String calendar;
    String time;

    //Fields to create a listView group separator
    public boolean isSeparator;
    public String separatorHeader;

    public DepartureInfo(String id, String calendar, String time){
        this.id = id;
        this.calendar = calendar;
        this.time = time;
        this.isSeparator = false;
        this.separatorHeader = null;
    }

    public DepartureInfo(boolean isSeparator, String separatorHeader){
        this.isSeparator = isSeparator;
        this.separatorHeader = separatorHeader;
    }

    public String getTime() {
        return time;
    }

    public String getCalendar() {
        return calendar;
    }

    public String getId() {
        return id;
    }
}
