package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ca.appdirect.appchallenge.model.lib.database.User;

@XmlRootElement(name = "creator")
@XmlAccessorType(XmlAccessType.FIELD)
public class Creator implements Serializable {

	private static final long serialVersionUID = -5266103741055422219L;

	private String email;
	private String firstName;
	private String lastName;
	private String openId;
	private String language;

	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getOpenId() {
		return this.openId;
	}

	public void setOpenId(final String openId) {
		this.openId = openId;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(final String language) {
		this.language = language;
	}

	public User parseAsUser() {
		User user = new User();
		user.setEmail(this.email);
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setOpenId(this.openId);
		return user;
	}
}