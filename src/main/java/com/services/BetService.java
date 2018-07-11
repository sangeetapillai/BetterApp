package services;

import java.util.ArrayList;
import java.util.List;

import beans.Match;
import beans.MatchResponse;
import beans.PostResponse;
import beans.Prediction;
import beans.PredictionResponse;
import beans.User;
import beans.UserResponse;
import daos.MatchDao;
import daos.UserDao;
import utils.StatusCode;

public class BetService {
	private final MatchDao matchDao;
	private final UserDao userDao;
	
	public BetService(MatchDao matchDao,UserDao userDao) {
		super();
		this.matchDao = matchDao;
		this.userDao = userDao;
		
	}
	
	public MatchResponse getAllUpcomingMatches(String userEmail) {
		List<Match> matchList =  matchDao.getAllUpcomingMatches(userEmail);
		MatchResponse response = new MatchResponse();
		response.setStatusCode(StatusCode.FAILURE.getValue());
		response.setMatches(matchList);
		if(!matchList.isEmpty()){
			response.setStatusCode(StatusCode.SUCCESS.getValue());				
		}
		
		return response;
	}
	
	public UserResponse getAllUserPointsInDesc() {
		List<User> userList =  userDao.getAllUserPointsInDesc();
		UserResponse response = new UserResponse();
		response.setStatusCode(StatusCode.FAILURE.getValue());
		response.setUsers(userList);
		if(userList != null){
			response.setStatusCode(StatusCode.SUCCESS.getValue());				
		}		
		return response;
	}
	
	public UserResponse getUserReportCard(String userEmail){
		UserResponse response = new UserResponse();
		List<User> userList = new ArrayList<User>();
		User user =  userDao.getUserReportCard(userEmail);
		userList.add(user);
		response.setUsers(userList);
		response.setStatusCode(StatusCode.FAILURE.getValue());
		if(user != null){
			response.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return response;
	}
	
	public PostResponse vote(Prediction[] predictions){
		PostResponse pr = new PostResponse();
		pr.setStatusCode(StatusCode.FAILURE.getValue());		
		if (userDao.vote(predictions)){
			pr.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return pr;
	}
	
	public PostResponse voteForChampion(Prediction prediction){
		PostResponse pr = new PostResponse();
		pr.setStatusCode(StatusCode.FAILURE.getValue());		
		if (userDao.voteForChampion(prediction)){
			pr.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return pr;
	}
	
	public PostResponse voteForChampion2(Prediction prediction){
		PostResponse pr = new PostResponse();
		pr.setStatusCode(StatusCode.FAILURE.getValue());		
		if (userDao.voteForChampion2(prediction)){
			pr.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return pr;
	}
	
	public MatchResponse getUserTrackRecord(String userEmail) {
		MatchResponse res = new MatchResponse();
		res.setStatusCode(StatusCode.FAILURE.getValue());
		res.setMatches(matchDao.getUserTrackRecord(userEmail));
		if(res.getMatches() != null)
		{
			res.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return res;
	}
	
	public PostResponse getAllPredictionsForMatch(int matchId){
		UserResponse users = new UserResponse();
		users.setStatusCode(StatusCode.FAILURE.getValue());
		users.setUsers(userDao.getAllPredictionsForMatch(matchId));
		if(users.getUsers() != null){
			users.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return users;
	}
	
	public PostResponse getOddsForMatch(int matchId){
		MatchResponse res = new MatchResponse();
		res.setStatusCode(StatusCode.FAILURE.getValue());
		res.setMatches(matchDao.getMatchOdds(matchId));
		if(res.getMatches() != null){
			res.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return res;
	}
	
	public PostResponse getOddsForJackpot1(int matchId){
		PredictionResponse res = new PredictionResponse();
		res.setStatusCode(StatusCode.FAILURE.getValue());
		res.setPredictions(matchDao.getMatchOddsForJackpot1(matchId));
		if(res.getPredictions() != null){
			res.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return res;
	}
	
	public PostResponse getMatchStatisticsForFinishedMatches(){
		MatchResponse res = new MatchResponse();
		res.setStatusCode(StatusCode.FAILURE.getValue());
		res.setMatches(matchDao.getMatchStatisticsForFinishedMatches());
		if(res.getMatches() != null){
			res.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return res;
	}
	
	public PostResponse getMatchStatisticsForJackpot1(int matchId){
		UserResponse res = new UserResponse();
		res.setStatusCode(StatusCode.FAILURE.getValue());
		res.setUsers(matchDao.getMatchStatisticsForJackpot1(matchId));
		if(res.getUsers() != null){
			res.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		return res;
	}
	
	
	

	
}
