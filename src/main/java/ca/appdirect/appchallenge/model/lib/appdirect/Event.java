package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="event")
@XmlAccessorType(XmlAccessType.FIELD)
public class Event implements Serializable {

	private static final long serialVersionUID = 7490587575137652713L;

	private String type;
	private Marketplace marketplace;
	private Creator creator;
	private Payload payload;

	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public Marketplace getMarketplace() {
		return this.marketplace;
	}

	public void setMarketplace(final Marketplace marketplace) {
		this.marketplace = marketplace;
	}

	public Creator getCreator() {
		return this.creator;
	}

	public void setCreator(final Creator creator) {
		this.creator = creator;
	}

	public Payload getPayload() {
		return this.payload;
	}

	public void setPayload(final Payload payload) {
		this.payload = payload;
	}
}