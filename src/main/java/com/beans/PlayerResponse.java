package beans;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerResponse extends PostResponse{
	List<Player> players;
	int captain;
	int viceCaptain;
	float remainingCredit;
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public int getCaptain() {
		return captain;
	}
	public void setCaptain(int captain) {
		this.captain = captain;
	}
	public int getViceCaptain() {
		return viceCaptain;
	}
	public void setViceCaptain(int viceCaptain) {
		this.viceCaptain = viceCaptain;
	}
	public float getRemainingCredit() {
		return remainingCredit;
	}
	public void setRemainingCredit(float remainingCredit) {
		this.remainingCredit = remainingCredit;
	}
	
	
}
