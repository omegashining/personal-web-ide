package ipn.escom.webide.entity.groupmember;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="group_members")
public class GroupMember {

	@NotEmpty
	private int id;
	
	@NotEmpty
	private int groupId = 1;
	
	@NotEmpty
	private String username;

	@Id
	@GeneratedValue
	@Column(name="id", nullable=false, columnDefinition = "BIGINT(20)")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="group_id", nullable=false,  columnDefinition = "BIGINT(20)")
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Column(name="username", nullable=false, length=255)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
