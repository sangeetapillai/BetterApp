package services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.util.StringUtils;

import beans.PostResponse;
import beans.User;
import beans.UserResponse;
import daos.UserDao;
import utils.EmailUtility;
import utils.StatusCode;

public class AuthenticationService {
	
	private final UserDao userDao;
	private final EmailUtility emailUtility;

	public AuthenticationService(UserDao userDao,EmailUtility emailUtility) {
		super();
		this.userDao = userDao;
		this.emailUtility = emailUtility;
	}
	
	public PostResponse verifyAndcreateUser(User newUser){
		if(emailUtility.validateSecretCode(newUser.getUserEmail(), newUser.getCode())){
			return createUser(newUser);
		}
		else{
			return new PostResponse(StatusCode.FAILURE.getValue());
		}
	}
	
	public PostResponse createUser(User newUser){
		
		PostResponse bean = new PostResponse();
		String tokenCode = this.getTokenCode();
		String password = this.getEncryptedPassword(newUser.getUserPassword(), tokenCode);
		if(StringUtils.isEmpty(newUser.getUserName())){
			newUser.setUserName(newUser.getUserEmail());
		}
		if(userDao.createUser(newUser.getUserName(), newUser.getUserEmail(),password, tokenCode) == 1){
			sendActivationLink(newUser.getUserEmail());
			bean.setStatusCode(StatusCode.SUCCESS.getValue());
		}
		else{
			bean.setStatusCode(StatusCode.FAILURE.getValue());
			bean.setMessage("User creation failed");
		}
		
		return bean;
	}
	
	public UserResponse authenticate(String userEmail,String userPassword){
		User user = userDao.getUserWithEmail(userEmail);
		UserResponse response = new UserResponse();
		response.setStatusCode(StatusCode.FAILURE.getValue());		
		if(user.getUserId() != -1){
			String decryptedPassword = this.getDecryptedPassword(user.getUserPassword(),user.getTokenCode());
			if(decryptedPassword.equals(userPassword))
			{
				response.setStatusCode(StatusCode.SUCCESS.getValue());
				List<User> userList =  new ArrayList<User>();
				userList.add(user);
				response.setUsers(userList);
				
			}
		}
		
		return response;
		
	}
	
	private String getTokenCode(){
		return RandomStringUtils.randomAlphanumeric(10);		
	}
	private String getEncryptedPassword(String password, String seed){
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(seed);
		return encryptor.encrypt(password);
	}
	private String getDecryptedPassword(String password,String tokenCode){
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(tokenCode);
		return encryptor.decrypt(password);
	}
	
	public PostResponse sendActivationLink(String userEmail){
		PostResponse res = new PostResponse();
		res.setStatusCode(StatusCode.FAILURE.getValue());
		if(emailUtility.sendEmail(userEmail))
		{
			res.setStatusCode(StatusCode.SUCCESS.getValue());			
			
		}	
		return res;
	}
}
