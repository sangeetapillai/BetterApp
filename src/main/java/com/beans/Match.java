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
	
}
