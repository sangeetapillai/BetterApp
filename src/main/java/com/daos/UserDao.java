package daos;



import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import beans.Prediction;
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
		System.out.println("REQUEST REACHED");
		int entries = -1;
		String userAlreadyExistQuery = "select count(*) from gz_users where LOWER(useremail) = ?";		
		try{
			SqlRowSet rowSetUserAlreadyExistQuery  = jdbcTemplate.queryForRowSet(userAlreadyExistQuery, userEmail.toLowerCase());
			if (rowSetUserAlreadyExistQuery.next() ) {
				String deleteUser = "delete  from gz_users where LOWER(useremail) = ?";
				entries = jdbcTemplate.update(deleteUser,userEmail.toLowerCase());
			} 
			
			String newUserQuery = 
					"insert into gz_users(username,useremail,tokencode,userpass)"
					+" values(?,?,?,?)";
	   		
			
				entries = jdbcTemplate.update(newUserQuery,userName,userEmail,tokenCode,userPassword);
		}catch(DataAccessException exp){
			LOGGER.error(exp);
			exp.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return entries;
		
		
	}
	
	public boolean activateUser(String userEmail){
		String newUserQuery = 
				"update gz_users set userstatus = 'Y' where lower(useremail) = ?";
   		int entries = jdbcTemplate.update(newUserQuery,userEmail.toLowerCase());
   		return (entries ==1)  ? true : false; 
	}
	
	public User getUserWithEmail(String userEmail){
		System.out.println("REQUEST REACHED");
		String userQuery = "select userid,username,userpass,tokencode,useremail from gz_users where userstatus = 'Y' and LOWER(useremail) = ?";
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
			exp.printStackTrace();
		}catch (Exception ex){
			LOGGER.error(ex);
			ex.printStackTrace();
		}
		return user;			
		
	}
	
	public List<User> getAllUserPointsInDesc(){
		
		List<User> userList = new ArrayList<User>();		
		//String queryForUserPoints = "select player_name as useremail, total_bounty as user_points from final_fifa_2018_leaderboard	order by total_bounty desc";
		
		String queryForUserPoints = 
				"select plv.player_name,sum(case when plv.status='WON' then 1 else 0 end) as "+
						"\"wins\",sum(case when plv.status='LOSE' then 1 else 0 end) as \"loss\" "+
						",nlp.nill_play ,total_bounty from  player_view plv join (select plg.player_name,(select count(mtb.match_id) "+
						"from match_table mtb where mtb.match_time<now())-count(distinct plg.match_id) as \"nill_play\" "+
						"from player_log plg group by 1) nlp on nlp.player_name = plv.player_name group "+
						"by plv.player_name,nlp.nill_play,total_bounty order by total_bounty";
		
		try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForUserPoints);
			int index = 1;
	        while (rowSet.next()) {
	        	 User user = new User();
	        	 user.setRank(index);
		         user.setUserEmail(rowSet.getString("player_name"));
		         user.setUserName(rowSet.getString("player_name"));
		         user.setUserPoints(Math.round(rowSet.getFloat("total_bounty")));
		         user.setMatchesWon(rowSet.getInt("wins"));
		         user.setMatchesLost(rowSet.getInt("loss"));
		         userList.add(user);
		         index++;
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
	
public List<User> getAllUserPointsForJackpotInDesc(){
		
		List<User> userList = new ArrayList<User>();
		String queryForUserPoints = "select player_name as useremail,sum(total_bounty) as user_points from single_player_view_v3 group by (player_name) order by user_points desc";
		
		try{
			SqlRowSet rowSet = jdbcTemplate.queryForRowSet(queryForUserPoints);
			int index = 1;
	        while (rowSet.next()) {
	        	 User user = new User();
	        	 user.setRank(index);
		         user.setUserEmail(rowSet.getString("useremail"));
		         user.setUserName(rowSet.getString("useremail"));
		         user.setUserPoints(Math.round(rowSet.getFloat("user_points")));
		         userList.add(user);
		         index++;
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
				"select player_name,wins,loss,total_bounty from final_fifa_2018_leaderboard where LOWER(player_name) = ?";
		
		/*String reportCardQuery = 
				"select p.player_name,p.wins,p.loss,p.nill_play,p.total_bounty from ("
				+" select plv.player_name, sum(case when plv.status='WON' then 1 else 0 end) as \"wins\"," 
				+" sum(case when plv.status='LOSE' then 1 else 0 end) as \"loss\","
				+" nlp.nill_play , "
				+" total_bounty "
				+" from  player_view plv "
				+" join (select plg.player_name,(select count(mtb.match_id) "
				+" from match_table mtb where mtb.match_time<now())-count(distinct plg.match_id) as \"nill_play\""
				+" from player_log plg group by 1) nlp on nlp.player_name = plv.player_name "
				+" group by plv.player_name,nlp.nill_play,total_bounty) as p where LOWER(p.player_name) = ?";*/
		
		
		try{
			SqlRowSet rowSet  = jdbcTemplate.queryForRowSet(reportCardQuery, userEmail.toLowerCase());
			while(rowSet.next()){
				user.setUserEmail(rowSet.getString("player_name"));
				user.setMatchesWon(rowSet.getInt("wins"));
				user.setMatchesLost(rowSet.getInt("loss"));
				user.setUserPoints(Math.round(rowSet.getFloat("total_bounty")));
			}
			
			
	        
		}catch(DataAccessException exp){
			System.out.println(reportCardQuery + userEmail);
			LOGGER.error(reportCardQuery + userEmail);
			LOGGER.error(exp);
			user = null;
			exp.printStackTrace();
		}catch (Exception ex){
			LOGGER.error(ex);
			user=null;
			ex.printStackTrace();
		}
		return user; 
	}
	
	public boolean vote(final Prediction[] predictions){
		boolean voteDone = false;
		try{
			this.insertBatch(predictions);
			voteDone = true;
		}catch(Exception exp){
			exp.printStackTrace();
		}
		return voteDone;
	}
	
	public void insertBatch(final Prediction[] predictions)
	{
		
			try{
				String sql = "INSERT INTO PLAYER_LOG " +
			
					"(PLAYER_NAME, MATCH_ID,PREDICTED_RESULT,PREDICTED_TIME) VALUES (?, ?, ?, CAST (? AS timestamp ))";
							
				  jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
							
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Prediction prediction = predictions[i];
						ps.setString(1, prediction.getUserEmail());
						ps.setInt(2, prediction.getMatchId());
						ps.setString(3, prediction.getPrediction());
						ps.setString(4, Calendar.getInstance().getTime().toString());
					}
							
					@Override
					public int getBatchSize() {
						return predictions.length;
					}
				  });
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	
	public List<User> getAllPredictionsForMatch(int matchId){
			
		
		String predictionQuery = "SELECT inn1.player_name, inn1.match_id, inn1.predicted_result, inn1.predicted_time FROM"
				+ " ( SELECT player_log.player_name, player_log.match_id, player_log.predicted_result, player_log.predicted_time, "
				+ " row_number() OVER (PARTITION BY player_log.match_id, player_log.player_name ORDER BY player_log.predicted_time DESC)"
				+ " AS rk FROM player_log) inn1 WHERE inn1.rk = 1 and inn1.match_id=? order by inn1.predicted_time";		
		List<User> userList = new ArrayList<User>();
		try{
			SqlRowSet rowSet  = jdbcTemplate.queryForRowSet(predictionQuery,matchId);
			while(rowSet.next()){
				User user = new User();
				user.setUserEmail(rowSet.getString("player_name"));
				user.setPrediction(rowSet.getString("predicted_result"));
				user.setPredictionTime(rowSet.getString("predicted_time"));
				userList.add(user);
			}
	        
		}catch(DataAccessException exp){
			LOGGER.error(predictionQuery + matchId);
			LOGGER.error(exp);
			userList = null;
			exp.printStackTrace();
		}catch (Exception ex){
			LOGGER.error(ex);
			userList=null;
			ex.printStackTrace();
		}
		return userList;
	}
	
	public boolean voteForChampion(Prediction prediction)
	{
		boolean voted = false;
		try {
			String voteForChampion = 
					"insert into player_log values(?,100,?,CAST (? AS timestamp ))";			
				jdbcTemplate.update(voteForChampion,prediction.getUserEmail(),prediction.getPrediction(),Calendar.getInstance().getTime().toString());
				voted = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return voted;
	
	}
	
	public List<User> getAllUsers(){
		String query = "select userid,username,useremail from gz_users where userstatus = 'Y'";		
		List<User> userList = new ArrayList<User>();
		try{
			SqlRowSet rowSet  = jdbcTemplate.queryForRowSet(query);
			while(rowSet.next()){
				User user = new User();
				user.setUserId(rowSet.getLong("userid"));
				user.setUserName(rowSet.getString("username"));
				user.setUserEmail(rowSet.getString("useremail"));
				userList.add(user);
			}
	        
		}catch(DataAccessException exp){
			LOGGER.error(query);
			LOGGER.error(exp);
			userList = null;
			exp.printStackTrace();
		}catch (Exception ex){
			LOGGER.error(ex);
			userList=null;
			ex.printStackTrace();
		}
		return userList;
	}
	
	public boolean voteForChampion2(Prediction prediction)
	{
		boolean voted = false;
		int matchId = 100;
		if(prediction.getMatchId() > 0){
			matchId = prediction.getMatchId();
		}
		try {
			String voteForChampion = 
					"insert into player_log(player_name,match_id,predicted_result,predicted_time) values(?,?,?,CAST (? AS timestamp ))";			
				jdbcTemplate.update(voteForChampion,prediction.getUserEmail(),matchId,prediction.getPrediction(),Calendar.getInstance().getTime().toString());
				voted = true;
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return voted;
	
	}
	
}
	