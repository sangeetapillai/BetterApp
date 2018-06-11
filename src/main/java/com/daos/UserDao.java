package daos;



import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import beans.User;

public class UserDao {

	private final JdbcTemplate jdbcTemplate;
	private final Logger LOGGER = Logger.getLogger(UserDao.class);
	
	public UserDao(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public int createUser(String userName, String userEmail, String userPassword , String tokenCode)
	{
		int entries = -1;
		String newUserQuery = 
				"insert into gz_users(username,useremail,tokencode,userpass)"
				+" values(?,?,?,?)";
   		
		try{
			entries = jdbcTemplate.update(newUserQuery,userName,userEmail,tokenCode,userPassword);
		}catch(DataAccessException exp){
			LOGGER.error(newUserQuery);
			LOGGER.error(exp);
		}
		return entries;
		
		
	}
	
	public User getUserWithEmail(String userEmail){
		String userQuery = "select userid,username,userpass,tokencode,useremail from gz_users where LOWER(useremail) = ?";
		User user = new User();
		user.setUserId(-1);
		try{
			SqlRowSet rowSet  = jdbcTemplate.queryForRowSet(userQuery, userEmail.toLowerCase());
			rowSet.next();
			user.setUserId(rowSet.getLong("userid"));
			user.setUserName(rowSet.getString("username"));
			user.setUserPassword(rowSet.getString("userpass"));
			user.setTokenCode(rowSet.getString("tokencode"));
			user.setUserEmail(rowSet.getString("useremail"));			
	        
		}catch(DataAccessException exp){
			LOGGER.error(userQuery + userEmail);
			LOGGER.error(exp);
		}catch (Exception ex){
			LOGGER.error(ex);
		}
		return user;			
		
	}
	
	public List<User> getAllUserPointsInDesc(){
		
		List<User> userList = new ArrayList<User>();
		String queryForUserPoints = 
				"select gus.useremail,gus.username, sum(plv.bounty_won)  as \"user_points\" from player_view plv "
				 + "join gz_users gus on gus.useremail = plv.player_name "
				 + "group by 1,2 order by user_points desc";
		
		try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForUserPoints);        
	        while (rowSet.next()) {
	            User user = new User();
	            user.setUserEmail(rowSet.getString("useremail"));
	            user.setUserName(rowSet.getString("username"));
	            user.setUserPoints(rowSet.getFloat("user_points"));
	            userList.add(user);
	        }
	        }catch(DataAccessException exp){
	        	LOGGER.error(queryForUserPoints);
	        	LOGGER.error(exp);
	        	exp.printStackTrace();
	        	userList = null;
	       }catch(Exception exp){
	        	LOGGER.error(queryForUserPoints);
	        	LOGGER.error(exp);
	        	exp.printStackTrace();
	        	userList = null;
	       }
	
		return userList;
	}
	
	public User getUserReportCard(String userEmail){
		User user = new User();
	
		String reportCardQuery = 
				"select p.player_name,p.wins,p.loss,p.nill_play,p.total_bounty from ("
				+" select plv.player_name, sum(case when plv.status='WON' then 1 else 0 end) as \"wins\"," 
				+" sum(case when plv.status='LOSE' then 1 else 0 end) as \"loss\","
				+" nlp.nill_play , "
				+" total_bounty "
				+" from  player_view plv "
				+" join (select plg.player_name,(select count(mtb.match_id) "
				+" from match_table mtb where mtb.match_time<now())-count(distinct plg.match_id) as \"nill_play\""
				+" from player_log plg group by 1) nlp on nlp.player_name = plv.player_name "
				+" group by plv.player_name,nlp.nill_play,total_bounty) as p where LOWER(p.player_name) = ?";
		
		try{
			SqlRowSet rowSet  = jdbcTemplate.queryForRowSet(reportCardQuery, userEmail.toLowerCase());
			rowSet.next();
			user.setUserEmail(rowSet.getString("player_name"));
			user.setMatchesWon(rowSet.getInt("wins"));
			user.setMatchesLost(rowSet.getInt("loss"));
			user.setMatchesNotPredicted(rowSet.getInt("nill_play"));
			user.setUserPoints(rowSet.getFloat("total_bounty"));			
	        
		}catch(DataAccessException exp){
			System.out.println(reportCardQuery + userEmail);
			LOGGER.error(reportCardQuery + userEmail);
			LOGGER.error(exp);
			user = null;
		}catch (Exception ex){
			LOGGER.error(ex);
			user=null;
		}
		return user; 
	}
}
	