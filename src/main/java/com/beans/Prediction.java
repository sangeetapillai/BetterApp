package beans;

public class Prediction {
   int matchId;
   String userEmail;
   String prediction;
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getPrediction() {
		return prediction;
	}
	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}
 
}
