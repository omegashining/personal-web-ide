package ipn.escom.webide.dao.groupmember;

import ipn.escom.webide.entity.groupmember.GroupMember;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class GroupMemberDAOImpl implements GroupMemberDAO {

	private HibernateTemplate hibernateTemplate;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	@Override
	public void saveGroupMember(GroupMember groupMember) {
		hibernateTemplate.saveOrUpdate(groupMember);
	}

}
