package daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import beans.Match;
import beans.Player;
import beans.Prediction;
import beans.Template;
import beans.User;
import beans.PlayerResponse;

public class MatchDao {
	
	private final JdbcTemplate jdbcTemplate;	
	private final Logger LOGGER = Logger.getLogger(MatchDao.class);

	public MatchDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}


	public List<Template> getAllTemplates(){
		List<Template> templates = new ArrayList<Template>();
		String getTemplates = "select template_id, template_name,description from gz_template";
		
		try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(getTemplates);        
	        while (rowSet.next()) {
	        	 Template template = new Template();
	        	 template.setTemplateId(rowSet.getInt("template_id"));
	        	 template.setTemplateName(rowSet.getString("template_name"));
	             template.setDescription(rowSet.getString("description"));
	             templates.add(template);
        }
        }catch(DataAccessException exp){
        	LOGGER.error(getTemplates);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	templates = null;
        }
		return templates;
		
	}
	public List<Match> getAllUpcomingMatches(String userEmail,int templateId) {
		List<Match> matchList = new ArrayList<Match>();
		/*String getAllVotesForUser = "select match_id , predicted_result from player_log where player_name = ?";
		Map<Integer,String> votes = new HashMap<Integer,String>(); 
		try{
			SqlRowSet rowSetVote = jdbcTemplate.queryForRowSet(getAllVotesForUser,userEmail);        
	        while (rowSetVote.next()) {
	        	votes.put(rowSetVote.getInt("match_id"),rowSetVote.getString("predicted_result"));
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}             
		*/
		String queryForUpcomingMatches = "select match_id,team_1, team_code_1,team_code_2,team_2, match_time  from f_match_table where (match_time  + Interval '2 hours')  > NOW() order by match_id";
		
        try{
		SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForUpcomingMatches);        
        while (rowSet.next()) {
            Match match = new Match();
            match.setMatchId(rowSet.getInt("match_id"));
            match.setTeam1Name(rowSet.getString("team_1"));
            match.setTeam2Name(rowSet.getString("team_2"));
            match.setT1(rowSet.getString("team_code_1"));
            match.setT2(rowSet.getString("team_code_2"));
            match.setCreditToPlay(100);
            String matchTime = rowSet.getString("match_time");
            match.setMatchTime(matchTime);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = format.parse(matchTime);            
			Date d2 = new Date();
			double diff = d1.getTime() - d2.getTime();
			double diffDays = diff/(24*60*60*1000);
			if(diffDays > 1){
				match.setTimeLeft(Math.round(diffDays)+" Days");
			}
			else{
				double diffHours = diff / (60 * 60 * 1000);
				double diffMin = diff%(60*60*1000);
				match.setTimeLeft(diffHours+" hours "+ diffMin+" minutes");
			}
			/*if(diffHours >=1) {
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
            }*/
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
	
	public List<Match> getUserTrackRecord(String userEmail,int templateId) {
		List<Match> matchList = new ArrayList<Match>();
		
		String queryForMatches = "select team1,team2,t1,t2,result as match_result,match_time,predicted_result as result,winning_bounty as points_earned, winner "
				+ " from player_view_v3 where LOWER(player_name) = ? and template_id = ? order by match_id desc";
		
		/*
		String queryForMatches = "select gus.useremail,plv.match_time,plv.team1, plv.team2,plv.predicted_result,mtb.result as \"match_result\","
				+ " plv.status as \"result\", plv.bounty_won  as \"points_earned\" from player_view plv join gz_users gus "
				+ " on gus.useremail = plv.player_name join match_table mtb on mtb.match_id = plv.match_id "
				+ " where gus.useremail = ? and mtb.result is not null";
				*/
        try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForMatches,userEmail.toLowerCase(),templateId);        
	        while (rowSet.next()) {
	        	 Match match = new Match();
	             match.setTeam1Name(rowSet.getString("team1"));
	             match.setTeam2Name(rowSet.getString("team2"));
	             match.setWinner(rowSet.getString("match_result"));
	             match.setVotedFor(rowSet.getString("result"));
	             match.setT1(rowSet.getString("t1"));
	             match.setT2(rowSet.getString("t2"));
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
	
	public List<Match> getMatchOdds(int matchId){
		String oddsQuery = "select team1,team2,bet_draw,bet_team1,bet_team2 from match_bet_stats where match_id= ? ";
		List<Match> matchList = new ArrayList<Match>();
		Match match;
		try {
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(oddsQuery,matchId);		
			while(rowSet.next())
			{
				match = new Match();
				match.setTeam1Name(rowSet.getString("team1"));
				match.setTeam2Name(rowSet.getString("team2"));
				match.setNumberOfBetForTeam1(rowSet.getInt("bet_team1"));
				match.setNumberOfBetForTeam2(rowSet.getInt("bet_team2"));
				match.setNumberOfBetForDraw(rowSet.getInt("bet_draw"));
				matchList.add(match);
			}
		} catch (DataAccessException e) {
			LOGGER.error(oddsQuery);
        	LOGGER.error(e);
        	e.printStackTrace();
        	matchList = null;
		} catch (Exception e) {
			LOGGER.error(oddsQuery);
        	LOGGER.error(e);
        	e.printStackTrace();
        	matchList = null;
		}      
        return matchList;	
	}
	
	public List<Prediction> getMatchOddsForJackpot1(int matchId){
		String oddsQuery = "select prediction , bets , total_bets from multi_match_bet_stats_v3 where match_id=? ";
		List<Prediction> matchList = new ArrayList<Prediction>();
		try {
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(oddsQuery,matchId);		
			while(rowSet.next())
			{
				Prediction p = new Prediction();
				p.setPrediction(rowSet.getString("prediction"));
				p.setJackpotBets(rowSet.getInt("bets"));
				p.setTotalJackPotBets(rowSet.getInt("total_bets"));
				matchList.add(p);
			}
		} catch (DataAccessException e) {
			LOGGER.error(oddsQuery);
        	LOGGER.error(e);
        	e.printStackTrace();
        	matchList = null;
		} catch (Exception e) {
			LOGGER.error(oddsQuery);
        	LOGGER.error(e);
        	e.printStackTrace();
        	matchList = null;
		}      
        return matchList;	
	}
	
	public List<Match> getMatchStatisticsForFinishedMatches(int templateId){
		List<Match> matchList = new ArrayList<Match>();
		String matchStatQuery = "select  match_id , match_time , result , match_bounty , team1,team2 , total_wins , bet_draw ,"
				+ " bet_team1,bet_team2 , total_bet , winning_bounty from match_bet_stats where template_id=? and COALESCE(LENGTH(result::TEXT),0) != 0 order by match_id;";
		try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(matchStatQuery,templateId);        
	        while (rowSet.next()) {
	        	 Match match = new Match();
	             match.setMatchId(rowSet.getInt("match_id"));
	             match.setMatchTime(rowSet.getString("match_time"));
	             match.setWinner(rowSet.getString("result"));
	             match.setBounty(rowSet.getLong("match_bounty"));
	             match.setTeam1Name(rowSet.getString("team1"));
	             match.setTeam2Name(rowSet.getString("team2"));
	             match.setTotalWins(rowSet.getInt("total_wins"));
	             match.setNumberOfBetForDraw(rowSet.getInt("bet_draw"));
	             match.setNumberOfBetForTeam1(rowSet.getInt("bet_team1"));
	             match.setNumberOfBetForTeam2(rowSet.getInt("bet_team2"));
	             match.setTotalBets(rowSet.getInt("total_bet"));
	             match.setWinningBounty(Math.round(rowSet.getFloat("winning_bounty")));
	             matchList.add(match);
        }
        }catch(DataAccessException exp){
        	LOGGER.error(matchStatQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	matchList = null;
        }catch(Exception exp){
        	LOGGER.error(matchStatQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	matchList = null;
        }
		return matchList;
	}
	
	public List<User> getMatchStatisticsForJackpot1(int matchId){
		List<User> userList = new ArrayList<User>();
		String matchStatQuery = "select player_name,predicted_result from single_player_view_v3 where match_id=?";
		try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(matchStatQuery,matchId);        
	        while (rowSet.next()) {
	        	 User user = new User();
	             user.setUserEmail(rowSet.getString("player_name"));
	             user.setPrediction(rowSet.getString("predicted_result"));
	             userList.add(user);
        }
        }catch(DataAccessException exp){
        	LOGGER.error(matchStatQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	userList = null;
        }catch(Exception exp){
        	LOGGER.error(matchStatQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	userList = null;
        }
		return userList;
	}
	
	public List<Player> getPlayersForMatch(int matchId){
		List<Player> playerList = new ArrayList<Player>();
		String playerListQuery = "";
		try{
			/*SqlRowSet rowSet = jdbcTemplate.queryForRowSet(playerListQuery,matchId);        
	        while (rowSet.next()) {
	        	 Player player = new Player();
	        	 
	             playerList.add(player);
	        }*/
	        //remove
	        Player player = new Player();
	        player.setPlayer_id(1);
	        player.setPlayer_name("Aftab Alam ");
	        player.setTeam_id(4);
	        player.setTeam_name("ENGLAND");
	        player.setPlayer_type("B");
	        player.setProfile_link("https://www.cricbuzz.com/profiles/6476/aftab-alam");
	        player.setRating(10);
	        player.setSpec("Right Arm Fast Medium ");
	        playerList.add(player);
	        player = new Player();
	        player.setPlayer_id(47);
	        player.setPlayer_name("Aftab Alam ");
	        player.setTeam_id(4);
	        player.setTeam_name("ENGLAND");
	        player.setPlayer_type("B");
	        player.setProfile_link("https://www.cricbuzz.com/profiles/6476/aftab-alam");
	        player.setRating(10);
	        player.setSpec("Right Arm Fast Medium ");
	        playerList.add(player);
	        player = new Player();
	        player.setPlayer_id(127);
	        player.setPlayer_name("Jeffrey Vendarsey");
	        player.setTeam_id(4);
	        player.setTeam_name("ENGLAND");
	        player.setPlayer_type("B");
	        player.setProfile_link("https://www.cricbuzz.com/profiles/6476/aftab-alam");
	        player.setRating(10);
	        player.setSpec("Right Arm Fast Medium ");
	        playerList.add(player);
	        player = new Player();
	        player.setPlayer_id(4);
	        player.setPlayer_name("Virat Kohli");
	        player.setTeam_id(4);
	        player.setTeam_name("ENGLAND");
	        player.setPlayer_type("B");
	        player.setProfile_link("https://www.cricbuzz.com/profiles/6476/aftab-alam");
	        player.setRating(10);
	        player.setSpec("Right Arm Fast Medium ");
	        playerList.add(player);
	        player = new Player();
	        player.setPlayer_id(5);
	        player.setPlayer_name("M S Dhoni");
	        player.setTeam_id(4);
	        player.setTeam_name("ENGLAND");
	        player.setPlayer_type("B");
	        player.setProfile_link("https://www.cricbuzz.com/profiles/6476/aftab-alam");
	        player.setRating(10);
	        player.setSpec("Right Arm Fast Medium ");
	        playerList.add(player);
	        //remov end
        }catch(DataAccessException exp){
        	LOGGER.error(playerListQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	playerList = null;
        }catch(Exception exp){
        	LOGGER.error(playerListQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	playerList = null;
        }
		return playerList;
	}
	
	public List<Player> setPlayersForMatchForUser(int matchId,String userEmail,PlayerResponse team){
		List<Player> playerList = new ArrayList<Player>();
		String playerListQuery = "";
		try{
			/*SqlRowSet rowSet = jdbcTemplate.queryForRowSet(playerListQuery,matchId);        
	        while (rowSet.next()) {
	        	 Player player = new Player();
	             playerList.add(player);
        	}*/
        }catch(DataAccessException exp){
        	LOGGER.error(playerListQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	playerList = null;
        }catch(Exception exp){
        	LOGGER.error(playerListQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	playerList = null;
        }
		return playerList;
	}
	
	public PlayerResponse getPlayersForMatchForUser(int matchId,String userEmail){
		List<Player> playerList = new ArrayList<Player>();
		String playerListQuery = "";
		try{
			/*SqlRowSet rowSet = jdbcTemplate.queryForRowSet(playerListQuery,matchId,userEmail);        
	        while (rowSet.next()) {
	        	 Player player = new Player();
	             playerList.add(player);
	        }*/
	        //remove
	        Player player = new Player();
	        player.setPlayer_id(47);
	        player.setPlayer_name("Aftab Alam ");
	        player.setTeam_id(4);
	        player.setTeam_name("ENGLAND");
	        player.setPlayer_type("B");
	        player.setProfile_link("https://www.cricbuzz.com/profiles/6476/aftab-alam");
	        player.setRating(10);
	        player.setSpec("Right Arm Fast Medium ");
	        playerList.add(player);
	        player = new Player();
	        player.setPlayer_id(127);
	        player.setPlayer_name(" Jeffrey Vandersay");
	        player.setTeam_id(9);
	        player.setTeam_name("SRI LAANKA");
	        player.setPlayer_type("B");
	        player.setProfile_link(" https://www.cricbuzz.com/profiles/10469/jeffrey-vandersay");
	        player.setRating(10);
	        player.setSpec("Right Hand Leg Break");
	        playerList.add(player);
	        //remove end
        }catch(DataAccessException exp){
        	LOGGER.error(playerListQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	playerList = null;
        }catch(Exception exp){
        	LOGGER.error(playerListQuery);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        	playerList = null;
        }
		PlayerResponse response = new PlayerResponse();
		response.setPlayers(playerList);
		response.setCaptain(47);
		response.setViceCaptain(127);
		response.setRemainingCredit(100-(playerList.size()*10));
		return response;
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
