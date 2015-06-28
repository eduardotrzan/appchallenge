package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ca.appdirect.appchallenge.model.lib.appdirect.enums.AccountStatus;

@XmlRootElement(name="account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account implements Serializable {

	private static final long serialVersionUID = 6318196700070958497L;

	private String accountIdentifier;
	private AccountStatus status;

	public String getAccountIdentifier() {
		return this.accountIdentifier;
	}

	public void setAccountIdentifier(final String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}

	public AccountStatus getStatus() {
		return this.status;
	}

	public void setStatus(final AccountStatus status) {
		this.status = status;
	}
}