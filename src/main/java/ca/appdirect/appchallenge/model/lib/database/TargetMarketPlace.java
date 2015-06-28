package ca.appdirect.appchallenge.model.lib.database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "market_place")
public class TargetMarketPlace extends GenericModel<Integer> {

	private static final long serialVersionUID = -3236391859942735488L;

	private String baseUrl;
	private String partner;
	private OrderingCompany orderingCompany;

	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="market_place_id_seq")
	@SequenceGenerator(name="market_place_id_seq", sequenceName="market_place_id_seq", allocationSize=1)
	@Column(name = "market_place_id")
	public Integer getId() {
		return super.getId();
	}

	@Column(name = "base_url", nullable = false, unique = true, length = 255)
	public String getBaseUrl() {
		return this.baseUrl;
	}

	public void setBaseUrl(final String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Column(name = "partner", nullable = false, unique = true, length = 255)
	public String getPartner() {
		return this.partner;
	}

	public void setPartner(final String partner) {
		this.partner = partner;
	}

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	public OrderingCompany getOrderingCompany() {
		return this.orderingCompany;
	}

	public void setOrderingCompany(final OrderingCompany orderingCompany) {
		this.orderingCompany = orderingCompany;
	}
}