package daos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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


	public List<Match> getAllUpcomingMatches(){
		List<Match> matchList = new ArrayList<Match>();
		String queryForUpcomingMatches = "select match_id , team1,team2, match_time , match_bounty from match_table where match_time > NOW()";
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
            match.setOpenForVote(false);
            Calendar date = Calendar.getInstance();
            try {
				date.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(matchTime));
			} catch (ParseException e) {
				e.printStackTrace();
			} 
            Calendar now = Calendar.getInstance(); // Get time now
            long differenceInMillis = (((date.getTimeInMillis() - now.getTimeInMillis())/1000L)/60L)/60L;
            if(differenceInMillis > 1){
            	match.setOpenForVote(true);
            }
            matchList.add(match);
        }
        }catch(DataAccessException exp){
        	LOGGER.error(queryForUpcomingMatches);
        	LOGGER.error(exp);
        	exp.printStackTrace();
        }
		return matchList;
	}
}
