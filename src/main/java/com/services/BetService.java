package services;

import java.util.ArrayList;
import java.util.List;

import beans.Match;
import beans.MatchResponse;
import beans.UserResponse;
import daos.MatchDao;
import beans.User;
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
	
	public MatchResponse getAllUpcomingMatches(){
		List<Match> matchList =  matchDao.getAllUpcomingMatches();
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
}
