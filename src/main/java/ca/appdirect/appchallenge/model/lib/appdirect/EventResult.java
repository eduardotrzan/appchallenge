package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

public class EventResult implements Serializable {

	private static final long serialVersionUID = 4133142272952862464L;

	private Boolean success;
	private String message;

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(final Boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}
}