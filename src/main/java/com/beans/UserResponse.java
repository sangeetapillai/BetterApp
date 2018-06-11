package beans;

import java.util.List;

public class UserResponse extends PostResponse{
	private List<User> users;

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> user) {
		this.users = user;
	}

	
	
}
