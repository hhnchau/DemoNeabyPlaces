package chauhuynh.info.demonearbyplaces.model;

import java.util.List;

/**
 * Created by appro on 05/03/2018.
 */

public class Suggest {
    private List<Predictions> predictions;

    private String status;

    public List<Predictions> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Predictions> predictions) {
        this.predictions = predictions;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }
}
