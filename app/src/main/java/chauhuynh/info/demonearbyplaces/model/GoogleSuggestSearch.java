package chauhuynh.info.demonearbyplaces.model;

/**
 * Created by appro on 02/03/2018.
 */

public class GoogleSuggestSearch {
    private String id;
    private String place_id;
    private String description;
    private String main_text;
    private String secondary_text;

    public GoogleSuggestSearch(String id, String place_id, String description, String main_text, String secondary_text) {
        this.id = id;
        this.place_id = place_id;
        this.description = description;
        this.main_text = main_text;
        this.secondary_text = secondary_text;
    }

    public String getId() {
        return id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getDescription() {
        return description;
    }

    public String getMain_text() {
        return main_text;
    }

    public String getSecondary_text() {
        return secondary_text;
    }
}
