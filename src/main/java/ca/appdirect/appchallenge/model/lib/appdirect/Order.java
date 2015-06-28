package ca.appdirect.appchallenge.model.lib.appdirect;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {

	private static final long serialVersionUID = 77444373371099244L;

	private EditionCode editionCode;

	@XmlElement(name = "item")
	private List<Item> items;

	public EditionCode getEditionCode() {
		return this.editionCode;
	}

	public void setEditionCode(final EditionCode editionCode) {
		this.editionCode = editionCode;
	}

	public List<Item> getItems() {
		return this.items;
	}

	public void setItems(final List<Item> items) {
		this.items = items;
	}
}