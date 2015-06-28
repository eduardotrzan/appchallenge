package ca.appdirect.appchallenge.model.lib.appdirect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="result")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventResultFail extends EventResult {

	private static final long serialVersionUID = -2405012370244222540L;

	private String accountIdentifier;

	public enum Code {
		USER_ALREADY_EXISTS
		, USER_NOT_FOUND
		, ACCOUNT_NOT_FOUND
		, MAX_USERS_REACHED
		, UNAUTHORIZED
		, OPERATION_CANCELED
		, CONFIGURATION_ERROR
		, INVALID_RESPONSE
		, UNKNOWN_ERROR
		, PENDING
	}

	public EventResultFail() {
		super.setSuccess(Boolean.FALSE);
	}

	public String getAccountIdentifier() {
		return this.accountIdentifier;
	}

	public void setAccountIdentifier(final String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}
}