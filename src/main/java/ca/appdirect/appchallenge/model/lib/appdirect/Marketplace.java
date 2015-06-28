package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ca.appdirect.appchallenge.model.lib.database.TargetMarketPlace;

@XmlRootElement(name="marketplace")
@XmlAccessorType(XmlAccessType.FIELD)
public class Marketplace implements Serializable {

	private static final long serialVersionUID = -2790053330889099674L;

	private String baseUrl;
	private String partner;

	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getPartner() {
		return this.partner;
	}

	public void setPartner(final String partner) {
		this.partner = partner;
	}

	public TargetMarketPlace parseAsTargetMarketPlace() {
		TargetMarketPlace targetMarketPlace = new TargetMarketPlace();
		targetMarketPlace.setBaseUrl(this.baseUrl);
		targetMarketPlace.setPartner(this.partner);
		return targetMarketPlace;
	}
}