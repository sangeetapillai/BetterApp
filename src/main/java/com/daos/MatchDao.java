package daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            match.setMatchTime(matchTime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = format.parse(matchTime);            
			Date d2 = new Date();
			double diff = d1.getTime() - d2.getTime();
			double diffHours = diff / (60 * 60 * 1000);
			if(diffHours >=1) {
				match.setOpenForVote(true);
			}
			else{
				match.setOpenForVote(false);
			}
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
		
		String queryForMatches = "select team1,team2,result as match_result,match_time,predicted_result as result,winning_bounty as points_earned, winner "
				+ " from player_view_v2 where LOWER(player_name) = ? ";
		
		/*
		String queryForMatches = "select gus.useremail,plv.match_time,plv.team1, plv.team2,plv.predicted_result,mtb.result as \"match_result\","
				+ " plv.status as \"result\", plv.bounty_won  as \"points_earned\" from player_view plv join gz_users gus "
				+ " on gus.useremail = plv.player_name join match_table mtb on mtb.match_id = plv.match_id "
				+ " where gus.useremail = ? and mtb.result is not null";
				*/
        try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForMatches,userEmail.toLowerCase());        
	        while (rowSet.next()) {
	        	 Match match = new Match();
	             match.setTeam1Name(rowSet.getString("team1"));
	             match.setTeam2Name(rowSet.getString("team2"));
	             match.setWinner(rowSet.getString("match_result"));
	             String matchTime = rowSet.getString("match_time");
	             match.setMatchTime(matchTime);
	             if( rowSet.getInt("winner") == 1){
	            	 match.setResultOfUser("WON");
	             }
	             else{
	            	 match.setResultOfUser("LOSE");	            	 
	             }
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
	
	public static void main (String args[]) throws ParseException  {
		String matchTime = "2018-06-22 16:30:00.0";
		String matchTime2 = "2018-06-22 17:31:00.0";
		
		
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = format.parse(matchTime);            
		Date d2 = format.parse(matchTime2); 
		if(d2.compareTo(d1)  < 0){
			
		}
		double diff = d1.getTime() - d2.getTime();
		
		double diffHours = (diff / (60 * 60 * 1000)) ;
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d1.getTime());
		System.out.println(d2.getTime());
		System.out.println(diff);
		System.out.println(diffHours);
		
	}
}
