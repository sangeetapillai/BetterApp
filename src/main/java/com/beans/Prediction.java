package beans;

public class Prediction {
   int matchId;
   String userEmail;
   String prediction;
   int jackpotBets;
   int totalJackPotBets;
	public int getJackpotBets() {
	return jackpotBets;
}
public void setJackpotBets(int jackpotBets) {
	this.jackpotBets = jackpotBets;
}
public int getTotalJackPotBets() {
	return totalJackPotBets;
}
public void setTotalJackPotBets(int totalJackPotBets) {
	this.totalJackPotBets = totalJackPotBets;
}
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
