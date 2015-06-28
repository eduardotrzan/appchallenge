package ca.appdirect.appchallenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.appdirect.appchallenge.model.bo.PortalBO;
import ca.appdirect.appchallenge.model.lib.appdirect.Company;
import ca.appdirect.appchallenge.model.lib.appdirect.Creator;
import ca.appdirect.appchallenge.model.lib.appdirect.Event;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResult;
import ca.appdirect.appchallenge.model.lib.appdirect.Marketplace;
import ca.appdirect.appchallenge.model.lib.appdirect.Order;
import ca.appdirect.appchallenge.model.lib.appdirect.Payload;
import ca.appdirect.appchallenge.model.lib.database.OrderingCompany;
import ca.appdirect.appchallenge.model.lib.database.TargetMarketPlace;
import ca.appdirect.appchallenge.model.lib.database.User;

@RestController
@RequestMapping(value = "/appdirect/event")
public class EventService {

	public enum EventType {
		SUBSCRIPTION_ORDER
		, SUBSCRIPTION_CANCEL
		, SUBSCRIPTION_CHANGE
		, SUBSCRIPTION_NOTICE
		, USER_ASSIGNMENT
		, USER_UNASSIGNMENT
	}

	@Autowired
	private OAuthRestTemplate oAuthRestTemplate;

	@Autowired
	private PortalBO portalBO;

	@RequestMapping(
			value = "/subscription/create",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult createSubscription(@RequestParam final String url) {
		Event event = this.retrieveEvent(url, EventType.SUBSCRIPTION_ORDER);

		Payload payload = event.getPayload();

		Company company 					 = payload.getCompany();
		OrderingCompany orderingCompany 	 = company.parseAsOrderingCompany();
		Order order 						 = payload.getOrder();
		orderingCompany.setEditionCode(order.getEditionCode());

		Marketplace marketplace 				 = event.getMarketplace();
		TargetMarketPlace targetMarketPlace 	 = marketplace.parseAsTargetMarketPlace();

		Creator creator = event.getCreator();
		User user 		= creator.parseAsUser();

		return this.portalBO.registerOrder(orderingCompany, targetMarketPlace, user);
	}

	private Event retrieveEvent(final String url, final EventType eventType) {
		ResponseEntity<Event> responseEntity = this.oAuthRestTemplate.getForEntity(url, Event.class);
		Event response 					     = responseEntity.getBody();
		return response;
	}


	/* ##########################################
	 * ##          Getters and setters         ##
	 * ##########################################
	 */

	public OAuthRestTemplate getoAuthRestTemplate() {
		return this.oAuthRestTemplate;
	}

	public void setoAuthRestTemplate(final OAuthRestTemplate oAuthRestTemplate) {
		this.oAuthRestTemplate = oAuthRestTemplate;
	}

	public PortalBO getPortalBO() {
		return this.portalBO;
	}

	public void setPortalBO(final PortalBO portalBO) {
		this.portalBO = portalBO;
	}
}