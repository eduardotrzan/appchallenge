package ca.appdirect.appchallenge.model.lib;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "sytem_user")
public class User extends GenericModel<Integer>{

	private static final long serialVersionUID = -2908684931828481914L;

	private String openId;

	private String firstName;

	private String lastName;

	private String email;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="sytem_user_id_seq")
	@SequenceGenerator(name="sytem_user_id_seq", sequenceName="sytem_user_id_seq", allocationSize=1)
	@Column(name = "sytem_user_id")
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "open_id", nullable = false, unique = true, length = 255)
	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(final String openId) {
		this.openId = openId;
	}

	@Column(name = "first_name", nullable = false, length = 100)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name", nullable = false, length = 200)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "email", nullable = false, length = 100)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
}