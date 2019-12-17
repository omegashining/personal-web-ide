package ipn.escom.webide.dao.user;

import ipn.escom.webide.entity.user.User;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class UserDAOImpl implements UserDAO {

	private HibernateTemplate hibernateTemplate;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	@Override
	public void saveUser(User user) {
		hibernateTemplate.saveOrUpdate(user);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUsersByName(String username) {
		return (List<User>)hibernateTemplate.find("from User user where user.username='" + username + "'");
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUsersByEmail(String email) {
		return (List<User>)hibernateTemplate.find("from User user where user.email='" + email + "'");
	}

}
