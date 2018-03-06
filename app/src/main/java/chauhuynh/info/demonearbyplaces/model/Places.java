package chauhuynh.info.demonearbyplaces.model;

import java.util.List;

/**
 * Created by appro on 05/03/2018.
 */

public class Places {
    private List<Results> results;
    private String status;

    public List<Results> getResults() {
        return results;
    }

    public void setResults(List<Results> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
