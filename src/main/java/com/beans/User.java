package beans;

public class User {
	private long userId;
	private String userName;
	private String userEmail;
	private String userPassword;
	private String tokenCode;
	private String creationDate;
	private float userPoints;
	private int matchesWon;
	private int matchesLost;
	private int matchesNotPredicted;
	private String prediction;
	private String predictionTime;
	private int rank;
	private int code;
	private String jackpotBounty;
	
	public String getJackpotBounty() {
		return jackpotBounty;
	}
	public void setJackpotBounty(String jackpotBounty) {
		this.jackpotBounty = jackpotBounty;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getPredictionTime() {
		return predictionTime;
	}
	public void setPredictionTime(String predictionTime) {
		this.predictionTime = predictionTime;
	}
	public String getPrediction() {
		return prediction;
	}
	public void setPrediction(String prediction) {
		this.prediction = prediction;
	}
	public int getMatchesWon() {
		return matchesWon;
	}
	public void setMatchesWon(int matchesWon) {
		this.matchesWon = matchesWon;
	}
	public int getMatchesLost() {
		return matchesLost;
	}
	public void setMatchesLost(int matchesLost) {
		this.matchesLost = matchesLost;
	}
	public int getMatchesNotPredicted() {
		return matchesNotPredicted;
	}
	public void setMatchesNotPredicted(int matchesNotPredicted) {
		this.matchesNotPredicted = matchesNotPredicted;
	}
	public float getUserPoints() {
		return userPoints;
	}
	public void setUserPoints(float userPoints) {
		this.userPoints = userPoints;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserPassword() {
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String getTokenCode() {
		return tokenCode;
	}
	public void setTokenCode(String tokenCode) {
		this.tokenCode = tokenCode;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	
	
	
}
