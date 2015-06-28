package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "payload")
@XmlAccessorType(XmlAccessType.FIELD)
public class Payload implements Serializable {

	private static final long serialVersionUID = 2594882993257286767L;

	private Company company;
	private Order order;
	private Account account;

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(final Company company) {
		this.company = company;
	}

	public Order getOrder() {
		return this.order;
	}

	public void setOrder(final Order order) {
		this.order = order;
	}

	public Account getAccount() {
		return this.account;
	}

	public void setAccount(final Account account) {
		this.account = account;
	}
}