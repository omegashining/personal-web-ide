package ipn.escom.webide.dao.user;

import ipn.escom.webide.entity.user.User;

import java.util.List;

public interface UserDAO {

	public void saveUser(User user);
	
	public List<User> getUsersByName(String username);
	
	public List<User> getUsersByEmail(String email);

}
