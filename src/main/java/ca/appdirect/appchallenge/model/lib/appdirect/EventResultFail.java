package ca.appdirect.appchallenge.model.lib.appdirect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="result")
@XmlAccessorType(XmlAccessType.FIELD)
public class EventResultFail extends EventResult {

	@XmlEnum
	@XmlType(name = "code")
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

	private static final long serialVersionUID = -2405012370244222540L;

	private String accountIdentifier;

	private Code code;

	public EventResultFail() {
		super.setSuccess(Boolean.FALSE);
	}

	public String getAccountIdentifier() {
		return this.accountIdentifier;
	}

	public void setAccountIdentifier(final String accountIdentifier) {
		this.accountIdentifier = accountIdentifier;
	}

	public Code getCode() {
		return this.code;
	}

	public void setCode(final Code code) {
		this.code = code;
	}
}