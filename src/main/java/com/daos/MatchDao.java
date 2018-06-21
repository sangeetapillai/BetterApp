package daos;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import beans.Match;

public class MatchDao {
	
	private final JdbcTemplate jdbcTemplate;	
	private final Logger LOGGER = Logger.getLogger(MatchDao.class);
	
	public MatchDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}


	public List<Match> getAllUpcomingMatches(String userEmail) {
		List<Match> matchList = new ArrayList<Match>();
		String getAllVotesForUser = "select match_id , predicted_result from player_log where player_name = ?";
		Map<Integer,String> votes = new HashMap<Integer,String>(); 
		try{
			SqlRowSet rowSetVote = jdbcTemplate.queryForRowSet(getAllVotesForUser,userEmail);        
	        while (rowSetVote.next()) {
	        	votes.put(rowSetVote.getInt("match_id"),rowSetVote.getString("predicted_result"));
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}             
		
		String queryForUpcomingMatches = "select match_id , team1,team2, match_time , match_bounty from match_table where (match_time  + Interval '2 hours')  > NOW() and template_id=1 order by match_id";
        try{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForUpcomingMatches);        
        while (rowSet.next()) {
            Match match = new Match();
            match.setMatchId(rowSet.getInt("match_id"));
            match.setTeam1Name(rowSet.getString("team1"));
            match.setTeam2Name(rowSet.getString("team2"));
            match.setBounty(rowSet.getLong("match_bounty"));
            match.setCreditToPlay(50);
            String matchTime = rowSet.getString("match_time");
            Timestamp t =   rowSet.getTimestamp("match_time");
            match.setMatchTime(matchTime);
            match.setOpenForVote(true);
            if(votes.containsKey(match.getMatchId())){
            	 match.setVoted(true);
            	 match.setVotedFor(votes.get(match.getMatchId()));
            }
            else{
            	match.setVoted(false);
            }
            matchList.add(match);
        }
        }catch(DataAccessException exp){
        	LOGGER.error(queryForUpcomingMatches);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        }catch(Exception e){
        	e.printStackTrace();
        }
		return matchList;
	}
	
	public List<Match> getUserTrackRecord(String userEmail) {
		List<Match> matchList = new ArrayList<Match>();
		String queryForMatches = "select gus.useremail,plv.match_time,plv.team1, plv.team2,plv.predicted_result,mtb.result as \"match_result\","
				+ " plv.status as \"result\", plv.bounty_won  as \"points_earned\" from player_view plv join gz_users gus "
				+ " on gus.useremail = plv.player_name join match_table mtb on mtb.match_id = plv.match_id "
				+ " where gus.useremail = ? and mtb.result is not null";
        try{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForMatches,userEmail);        
        while (rowSet.next()) {
            Match match = new Match();
            match.setTeam1Name(rowSet.getString("team1"));
            match.setTeam2Name(rowSet.getString("team2"));
            match.setWinner(rowSet.getString("match_result"));
            String matchTime = rowSet.getString("match_time");
            match.setMatchTime(matchTime);
            match.setResultOfUser(rowSet.getString("result"));
            match.setUserPointsEarned(rowSet.getFloat("points_earned"));            
            matchList.add(match);
        }
        }catch(DataAccessException exp){
        	LOGGER.error(queryForMatches);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	matchList = null;
        }
		return matchList;
	}
}
