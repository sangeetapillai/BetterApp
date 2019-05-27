package resources;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import beans.PostResponse;
import beans.Prediction;
import beans.User;
import beans.PlayerResponse;
import services.AuthenticationService;
import services.BetService;


@Path("/")
@Component
public class BetResource{
	
	@Autowired
	private BetService betService;
	
	@Autowired
	private AuthenticationService authenticationService;

	
	@GET
    @Path("/templates")
    @Produces(MediaType.APPLICATION_JSON)
    public  PostResponse getTemplateList() {
        return betService.getAllTemplates();
    }
	
	@GET
    @Path("/leaderboard")
    @Produces(MediaType.APPLICATION_JSON)
    public  PostResponse getLeaderBoardList() {
        return betService.getAllUserPointsInDesc();
    }
	
	@GET
    @Path("/jackpotleaderboard")
    @Produces(MediaType.APPLICATION_JSON)
    public  PostResponse getjackpotleaderboardList() {
        return betService.getAllJackpotUserPointsInDesc();
    }
	
	@GET
    @Path("/upcomingMatches")
    @Produces(MediaType.APPLICATION_JSON)
    public PostResponse getUpcomingMatches(@QueryParam("userEmail") String userEmail,@QueryParam("templateId") int templateId) {
       return betService.getAllUpcomingMatches(userEmail,templateId);
		
    }
	
	@POST
    @Path("/vote")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse vote(Prediction[] predictions) {
        return betService.vote(predictions);
    }
	
	@GET
    @Path("/userReportCard")
    @Produces(MediaType.APPLICATION_JSON)
    public PostResponse userReportCard(@QueryParam("userEmail") String userEmail) {
       return betService.getUserReportCard(userEmail);
    }
	
	@GET
    @Path("/dummy")
    @Produces(MediaType.APPLICATION_JSON)
    public PostResponse dummy() {
		PostResponse ps = new PostResponse();
		ps.setMessage("dummy");
       return ps;
    }
	

	@POST
    @Path("/createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse createUser(User user) {
        return authenticationService.createUser(user);        
    }
	
	@POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse authenticate(User user) {
        return authenticationService.authenticate(user.getUserEmail(), user.getUserPassword());
    }
	
	@POST
    @Path("/getUserTrackRecord")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse getUserTrackRecord(@QueryParam("templateId") int templateId,User user) {
        return betService.getUserTrackRecord(user.getUserEmail(),templateId);
    }
	
	@GET
    @Path("/getAllPredictionsForMatch")
    @Produces(MediaType.APPLICATION_JSON)
    public PostResponse getAllPredictionsForMatch(@QueryParam("matchId") int matchId) {
        return betService.getAllPredictionsForMatch(matchId);
    }
	
	@GET
	@Path("/getOddsForMatch")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getOddsForMatch(@QueryParam("matchId") int matchId){
		return betService.getOddsForMatch(matchId);
	}
	
	@GET
	@Path("/getMatchStatistics")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getMatchStatistics(@QueryParam("templateId") int templateId){
		return betService.getMatchStatisticsForFinishedMatches(templateId);
	}
	
	@GET
	@Path("/generateActivationLink")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse generateActivationLink(@QueryParam("userEmail") String userEmail){
		return authenticationService.sendActivationLink(userEmail);
	}
	
	@POST
	@Path("/resetPassword")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse resetPassword(User user) {
        return authenticationService.verifyAndcreateUser(user);
    }
	
	
	@GET
	@Path("/activateUser")
    @Produces(MediaType.APPLICATION_JSON)    
    public PostResponse activateUser(@QueryParam("userEmail") String userEmail,@QueryParam("code") int code) {
        return authenticationService.verifyAndActivateUser(userEmail,code);
    }
	
	@POST
	@Path("/voteForChampion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse voteForChampion(Prediction prediction) {
        return betService.voteForChampion(prediction);
    }
	
	@GET
	@Path("/getMatchStatisticsJackPot1")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getMatchStatisticsJackPot1(@QueryParam("matchId") int matchId){
		return betService.getMatchStatisticsForJackpot1(matchId);
	}
	
	@GET
	@Path("/getOddsForJackpot1")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getOddsForJackpot1(@QueryParam("matchId") int matchId){
		return betService.getOddsForJackpot1(matchId);
	}
	
	@POST
	@Path("/voteForChampion2")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public PostResponse voteForChampion2(Prediction prediction) {
		
		return betService.voteForChampion2(prediction);
    }
	
	/*************Fantasy team******************/
	
	@GET
	@Path("/allusers")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getAllUsers(){
		return betService.getAllUsers();
	}
	
	@GET
	@Path("/getPlayersForMatch")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getPlayersForMatch(@QueryParam("matchId") int matchId){
		return betService.getPlayersForMatch(matchId);
	}
	
	@GET
	@Path("/getPlayersForMatchForUser")
	@Produces(MediaType.APPLICATION_JSON)
	public PostResponse getPlayersForMatchForUser(@QueryParam("matchId") int matchId,@QueryParam("useremail") String userEmail){
		return betService.getPlayersForMatchForUser(matchId,userEmail);
	}
	
	@POST
	@Path("/setPlayersForMatchForUser")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public PostResponse setPlayersForMatchForUser(@QueryParam("matchId") int matchId,@QueryParam("useremail") String userEmail,PlayerResponse team){
		return betService.setPlayersForMatchForUser(matchId,userEmail,team);
	}
}