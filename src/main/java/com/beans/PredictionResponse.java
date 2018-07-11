package beans;

import java.util.List;

public class PredictionResponse extends PostResponse{
	private List<Prediction> predictions;

	public List<Prediction> getPredictions() {
		return predictions;
	}

	public void setPredictions(List<Prediction> predictions) {
		this.predictions = predictions;
	}
	

}
