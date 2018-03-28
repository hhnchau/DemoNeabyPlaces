package chauhuynh.info.demonearbyplaces.model;

/**
 * Created by Chau Huynh on 3/28/2018.
 */

public class Opening_hours {

    private Periods[] periods;

    private String open_now;

    private String[] weekday_text;

    public String getOpen_now() {
        return open_now;
    }

    public void setOpen_now(String open_now) {
        this.open_now = open_now;
    }

    public String[] getWeekday_text() {
        return weekday_text;
    }

    public void setWeekday_text(String[] weekday_text) {
        this.weekday_text = weekday_text;
    }

}
