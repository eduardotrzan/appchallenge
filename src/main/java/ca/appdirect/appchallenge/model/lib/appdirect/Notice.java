package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="notice")
@XmlAccessorType(XmlAccessType.FIELD)
public class Notice implements Serializable {

	@XmlEnum
	@XmlType(name = "type")
	public enum Type {
		DEACTIVATED
		, REACTIVATED
		, CLOSED
		, UPCOMING_INVOICE
	}

	private static final long serialVersionUID = -4271519804198878884L;

	private Type type;

	public Type getType() {
		return this.type;
	}

	public void setType(final Type type) {
		this.type = type;
	}
}