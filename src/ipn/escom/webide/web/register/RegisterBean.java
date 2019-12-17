package ipn.escom.webide.web.register;

import static ipn.escom.webide.constants.SystemConstants.FILESEPARATOR;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import ipn.escom.webide.dao.groupmember.GroupMemberDAO;
import ipn.escom.webide.dao.user.UserDAO;
import ipn.escom.webide.entity.groupmember.GroupMember;
import ipn.escom.webide.entity.user.User;
import ipn.escom.webide.utilities.FolderUtilities;
import ipn.escom.webide.utilities.WebUtilities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegisterBean {

	private UserDAO userDAO;
	private GroupMemberDAO groupMemberDAO;
	private HttpServletRequest request;
	private User user;
	private String error;

	@Autowired
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Autowired
    public void setGroupMemberDAO(GroupMemberDAO groupMemberDAO) {
		this.groupMemberDAO = groupMemberDAO;
	}

	@Autowired(required = true)
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}

	public void createUserFolder() {
		String path = WebUtilities.getRealPath("/usuarios/", request) + FILESEPARATOR + user.getUsername();
		FolderUtilities.create(path);
	}
	
	public void encryptPasword() {
		try {
			String password = user.getPassword();
			MessageDigest mdAlgorithm = MessageDigest.getInstance("MD5");
			mdAlgorithm.update(password.getBytes(), 0, password.length());
			String passwordEncrypted = new BigInteger(1, mdAlgorithm.digest()).toString(16);
			user.setPassword(passwordEncrypted);
		} catch (NoSuchAlgorithmException e) {
		}
	}
	
	public void saveUser() {
		userDAO.saveUser(user);
		
		GroupMember groupMember = new GroupMember();
		groupMember.setUsername(user.getUsername());
		groupMemberDAO.saveGroupMember(groupMember);
	}

	public String add() {
		if(!user.getPassword().equals(user.getConfirmPassword())) {
			error = "password";
			
			return "fail";
		}

		createUserFolder();
		encryptPasword();
		saveUser();
		
		user.clearFields();
		error = "";
		
		return "success";
	}

}
