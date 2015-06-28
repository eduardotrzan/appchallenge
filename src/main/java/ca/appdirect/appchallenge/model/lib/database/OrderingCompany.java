package ca.appdirect.appchallenge.model.lib.database;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import ca.appdirect.appchallenge.model.lib.appdirect.EditionCode;

@Entity
@Table(name = "company")
public class OrderingCompany extends GenericModel<Integer> {

	public enum Status {
		FREE_TRIAL
		, FREE_TRIAL_EXPIRED
		, ACTIVE
		, SUSPENDED
		, CANCELLED
	}

	private static final long serialVersionUID = -4602836989188440250L;

	private String uuid;
	private String email;
	private String name;
	private String phoneNumber;
	private String website;
	private Set<User> users;
	private Set<TargetMarketPlace> marketPlaces;
	private Status status;
	private EditionCode editionCode;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="company_id_seq")
	@SequenceGenerator(name="company_id_seq", sequenceName="company_id_seq", allocationSize=1)
	@Column(name = "company_id")
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "uuid", nullable = false, unique = true, length = 255)
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "email", nullable = false, length = 255)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@Column(name = "name", nullable = false, length = 255)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Column(name = "phone_number", nullable = false, length = 255)
	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Column(name = "website", nullable = false, length = 255)
	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	@OneToMany(mappedBy = "orderingCompany", targetEntity = User.class)
	public Set<User> getUsers() {
		return this.users;
	}

	public void setUsers(final Set<User> users) {
		this.users = users;
	}

	@OneToMany(mappedBy = "orderingCompany", targetEntity = TargetMarketPlace.class)
	public Set<TargetMarketPlace> getMarketPlaces() {
		return this.marketPlaces;
	}

	public void setMarketPlaces(final Set<TargetMarketPlace> marketPlaces) {
		this.marketPlaces = marketPlaces;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = 255)
	public Status getStatus() {
		return this.status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "edition_code", nullable = false)
	public EditionCode getEditionCode() {
		return this.editionCode;
	}

	public void setEditionCode(final EditionCode editionCode) {
		this.editionCode = editionCode;
	}
}