package ca.appdirect.appchallenge.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ca.appdirect.appchallenge.model.bo.PortalBO;
import ca.appdirect.appchallenge.model.lib.appdirect.Account;
import ca.appdirect.appchallenge.model.lib.appdirect.Company;
import ca.appdirect.appchallenge.model.lib.appdirect.Creator;
import ca.appdirect.appchallenge.model.lib.appdirect.Event;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResult;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResultFail;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResultFail.Code;
import ca.appdirect.appchallenge.model.lib.appdirect.Marketplace;
import ca.appdirect.appchallenge.model.lib.appdirect.Notice;
import ca.appdirect.appchallenge.model.lib.appdirect.Order;
import ca.appdirect.appchallenge.model.lib.appdirect.Payload;
import ca.appdirect.appchallenge.model.lib.appdirect.enums.AccountStatus;
import ca.appdirect.appchallenge.model.lib.appdirect.enums.EditionCode;
import ca.appdirect.appchallenge.model.lib.appdirect.enums.EventType;
import ca.appdirect.appchallenge.model.lib.database.OrderingCompany;
import ca.appdirect.appchallenge.model.lib.database.TargetMarketPlace;
import ca.appdirect.appchallenge.model.lib.database.User;

@RestController
@RequestMapping(value = "/appdirect")
public class EventService {

	private static final Logger LOGGER = LogManager.getLogger(EventService.class);

	private OAuthRestTemplate oAuthRestTemplate;

	public EventService() {
		String consumerKey = System.getenv("consumer-key");
		String consumerSecret = System.getenv("consumer-secret");
		BaseProtectedResourceDetails resourceDetails = new BaseProtectedResourceDetails();
		resourceDetails.setConsumerKey(consumerKey);
		resourceDetails.setSharedSecret(new SharedConsumerSecretImpl(consumerSecret));
		this.oAuthRestTemplate = new OAuthRestTemplate(resourceDetails);
	}

	@Autowired
	private PortalBO portalBO;

	@RequestMapping(
			value = "/subscription/create",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult createSubscription(@RequestParam final String url) {
		EventService.LOGGER.debug("Creating subscription...");

		Event event;
		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_ORDER);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

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

	@RequestMapping(
			value = "/subscription/change",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult changeSubscription(@RequestParam final String url) {
		Event event;

		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_CHANGE);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

		Integer accountId = this.getAccountIdentifier(event);

		Payload payload         = event.getPayload();
		Order order 		    = payload.getOrder();
		EditionCode editionCode = order.getEditionCode();

		return this.portalBO.changeOrder(accountId, editionCode);
	}

	@RequestMapping(
			value = "/subscription/cancel",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult cancelSubscription(@RequestParam final String url) {
		Event event;

		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_CANCEL);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

		Integer accountId = this.getAccountIdentifier(event);
		return this.portalBO.cancelOrder(accountId);
	}

	@RequestMapping(
			value = "/subscription/notice",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult noticeSubscription(@RequestParam final String url) {
		Event event;

		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_NOTICE);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

		Integer accountId = this.getAccountIdentifier(event);

		Payload payload = event.getPayload();
		Notice notice	= payload.getNotice();

		Account account 	 = payload.getAccount();
		AccountStatus status = account.getStatus();

		return this.portalBO.noticeOrder(accountId, notice.getType(), status);
	}

	private Event retrieveEvent(final String url, final EventType eventType) {
		EventService.LOGGER.debug("oAuthRestTemplate: " + this.oAuthRestTemplate);
		EventService.LOGGER.debug("url: " + url);
		ResponseEntity<Event> responseEntity = this.oAuthRestTemplate.getForEntity(url, Event.class);
		Event response 					     = responseEntity.getBody();

		EventType urlType = EventType.getEventType(response.getType());
		if ((urlType == null) || (urlType != eventType)) {
			throw new IllegalArgumentException("Event is not the same!");
		}

		return response;
	}

	private Integer getAccountIdentifier(final Event event) {
		Payload payload          = event.getPayload();
		Account account          = payload.getAccount();
		String accountIdentifier = account.getAccountIdentifier();
		Integer accountId		 = Integer.parseInt(accountIdentifier);
		return accountId;
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