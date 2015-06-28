package ca.appdirect.appchallenge.model.lib.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "system_user")
public class User extends GenericModel<Integer> {

	private static final long serialVersionUID = -2908684931828481914L;

	public enum Profile {
		ADM
		, USER
		, USER_RESTRICTED
	}

	private String openId;
	private String firstName;
	private String lastName;
	private String email;
	private OrderingCompany orderingCompany;
	private Profile profile;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="system_user_id_seq")
	@SequenceGenerator(name="system_user_id_seq", sequenceName="system_user_id_seq", allocationSize=1)
	@Column(name = "system_user_id")
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

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	public OrderingCompany getOrderingCompany() {
		return this.orderingCompany;
	}

	public void setOrderingCompany(final OrderingCompany orderingCompany) {
		this.orderingCompany = orderingCompany;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "profile", nullable = false)
	public Profile getProfile() {
		return this.profile;
	}

	public void setProfile(final Profile profile) {
		this.profile = profile;
	}
}