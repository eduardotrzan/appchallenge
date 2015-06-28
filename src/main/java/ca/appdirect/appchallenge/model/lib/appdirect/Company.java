package ca.appdirect.appchallenge.model.lib.appdirect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ca.appdirect.appchallenge.model.lib.database.GenericModel;
import ca.appdirect.appchallenge.model.lib.database.OrderingCompany;

@XmlRootElement(name="company")
@XmlAccessorType(XmlAccessType.FIELD)
public class Company extends GenericModel<Integer> {

	private static final long serialVersionUID = 1489951455330293213L;

	private String uuid;
	private String email;
	private String name;
	private String phoneNumber;
	private String website;

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(final String uuid) {
		this.uuid = uuid;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	public OrderingCompany parseAsOrderingCompany() {
		OrderingCompany orderingCompany = new OrderingCompany();
		orderingCompany.setName(this.name);
		orderingCompany.setEmail(this.email);
		orderingCompany.setPhoneNumber(this.phoneNumber);
		orderingCompany.setUuid(this.uuid);
		orderingCompany.setWebsite(this.website);
		return orderingCompany;
	}
}