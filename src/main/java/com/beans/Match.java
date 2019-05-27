package beans;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import utils.TextUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_DEFAULT)
public class Match {

	private int matchId;
	private String team1Name;
	private String team2Name;
	private String matchTime;
	private long bounty;
	private String winner;
	private boolean voted;
	private String votedFor;
	private int numberOfBetForTeam1;
	private int numberOfBetForTeam2;
	private int numberOfBetForDraw;
	private int totalWins;
	private int totalBets;
	private float winningBounty;
	private String t1;
	private String t2;
	private String timeLeft;
	
	public String getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(String timeLeft) {
		this.timeLeft = timeLeft;
	}
	public int getTotalWins() {
		return totalWins;
	}
	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}
	public int getTotalBets() {
		return totalBets;
	}
	public void setTotalBets(int totalBets) {
		this.totalBets = totalBets;
	}
	public float getWinningBounty() {
		return winningBounty;
	}
	public void setWinningBounty(float winningBounty) {
		this.winningBounty = winningBounty;
	}
	public int getNumberOfBetForTeam1() {
		return numberOfBetForTeam1;
	}
	public void setNumberOfBetForTeam1(int numberOfBetForTeam1) {
		this.numberOfBetForTeam1 = numberOfBetForTeam1;
	}
	public int getNumberOfBetForTeam2() {
		return numberOfBetForTeam2;
	}
	public void setNumberOfBetForTeam2(int numberOfBetForTeam2) {
		this.numberOfBetForTeam2 = numberOfBetForTeam2;
	}
	public int getNumberOfBetForDraw() {
		return numberOfBetForDraw;
	}
	public void setNumberOfBetForDraw(int numberOfBetForDraw) {
		this.numberOfBetForDraw = numberOfBetForDraw;
	}
	public String getVotedFor() {
		return votedFor;
	}
	public void setVotedFor(String votedFor) {
		this.votedFor = votedFor;
	}

	// adding for getusertrackrecord api
	private String resultOfUser;
	private float userPointsEarned;
	
	public boolean isVoted() {
		return voted;
	}
	public void setVoted(boolean voted) {
		this.voted = voted;
	}
	public String getWinner() {
		return winner;
	}
	public void setWinner(String winner) {
		this.winner = winner;
	}
	public String getResultOfUser() {
		return resultOfUser;
	}
	public void setResultOfUser(String resultOfUser) {
		this.resultOfUser = resultOfUser;
	}
	
	public float getUserPointsEarned() {
		return userPointsEarned;
	}
	public void setUserPointsEarned(float userPointsEarned) {
		this.userPointsEarned = userPointsEarned;
	}

	private long creditToPlay;
	public long getBounty() {
		return bounty;
	}
	public void setBounty(long bounty) {
		this.bounty = bounty;
	}
	public long getCreditToPlay() {
		return creditToPlay;
	}
	public void setCreditToPlay(long creditToPlay) {
		this.creditToPlay = creditToPlay;
	}
	private boolean openForVote;
	
	public int getMatchId() {
		return matchId;
	}
	public void setMatchId(int matchId) {
		this.matchId = matchId;
	}
	public String getTeam1Name() {
		return team1Name;
	}
	public void setTeam1Name(String team1Name) {
		this.team1Name = team1Name;
	}
	public String getTeam2Name() {
		return team2Name;
	}
	public void setTeam2Name(String team2Name) {
		this.team2Name = team2Name;
	}
	
	@Override
    public String toString() {
        return TextUtils.convertObjectToJsonString(this);
    }
	public boolean isOpenForVote() {
		return openForVote;
	}
	public void setOpenForVote(boolean openForVote) {
		this.openForVote = openForVote;
	}
	public String getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(String matchTime) {
		this.matchTime = matchTime;
	}
	public String getT1() {
		return t1;
	}
	public void setT1(String t1) {
		this.t1 = t1;
	}
	public String getT2() {
		return t2;
	}
	public void setT2(String t2) {
		this.t2 = t2;
	}
	
	
}
