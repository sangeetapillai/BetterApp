package beans;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class PostResponse {
	protected String message;
	protected String statusCode;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	
}
