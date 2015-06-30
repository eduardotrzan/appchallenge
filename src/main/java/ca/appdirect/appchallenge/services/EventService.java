package ca.appdirect.appchallenge.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	@Autowired
	private PortalBO portalBO;

	@RequestMapping(
			value = "/subscription/create",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult createSubscription(@RequestParam final String url) {
		EventService.LOGGER.info("Creating subscription...");

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

		/*
		 * Used for saving AppDirect dummy test xmls.
		 */
		final String dummyAccountOpenId = "https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2";
		if (dummyAccountOpenId.equals(user.getOpenId())) {
			orderingCompany.setAccountIdentifier("dummy-account");
		}

		return this.portalBO.registerOrder(orderingCompany, targetMarketPlace, user);
	}

	@RequestMapping(
			value = "/subscription/change",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult changeSubscription(@RequestParam final String url) {
		EventService.LOGGER.info("Changing subscription...");

		Event event;
		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_CHANGE);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

		String accountIdentifier = this.getAccountIdentifier(event);

		Payload payload         = event.getPayload();
		Order order 		    = payload.getOrder();
		EditionCode editionCode = order.getEditionCode();

		return this.portalBO.changeOrder(accountIdentifier, editionCode);
	}

	@RequestMapping(
			value = "/subscription/cancel",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult cancelSubscription(@RequestParam final String url) {
		EventService.LOGGER.info("Cancelling subscription...");

		Event event;
		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_CANCEL);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

		String accountIdentifier = this.getAccountIdentifier(event);
		return this.portalBO.cancelOrder(accountIdentifier);
	}

	@RequestMapping(
			value = "/subscription/notice",
			method = {RequestMethod.GET},
			produces = "application/xml"
			)
	@ResponseStatus(HttpStatus.OK)
	public EventResult noticeSubscription(@RequestParam final String url) {
		EventService.LOGGER.info("Noticing subscription...");

		Event event;
		try {
			event = this.retrieveEvent(url, EventType.SUBSCRIPTION_NOTICE);
		} catch (IllegalArgumentException e) {
			EventResultFail eventResultFail = new EventResultFail();
			eventResultFail.setCode(Code.INVALID_RESPONSE);
			eventResultFail.setMessage(e.getMessage());
			return eventResultFail;
		}

		String accountIdentifier = this.getAccountIdentifier(event);

		Payload payload = event.getPayload();
		Notice notice	= payload.getNotice();

		Account account 	 = payload.getAccount();
		AccountStatus status = account.getStatus();

		return this.portalBO.noticeOrder(accountIdentifier, notice.getType(), status);
	}

	private Event retrieveEvent(final String url, final EventType eventType) {
		String eventMsg = String.format("Retrieving event type %s in url: %s", eventType, url);
		EventService.LOGGER.info(eventMsg);

		Event response = this.portalBO.getForEntity(url);

		String responseType	= response.getType();
		EventType urlType   = EventType.getEventType(responseType);
		if ((urlType == null) || (!urlType.equals(eventType))) {
			throw new IllegalArgumentException("Event is not the same!");
		}

		return response;
	}

	private String getAccountIdentifier(final Event event) {
		Payload payload          = event.getPayload();
		Account account          = payload.getAccount();
		String accountIdentifier = account.getAccountIdentifier();
		return accountIdentifier;
	}


	/* ##########################################
	 * ##          Getters and setters         ##
	 * ##########################################
	 */

	public PortalBO getPortalBO() {
		return this.portalBO;
	}

	public void setPortalBO(final PortalBO portalBO) {
		this.portalBO = portalBO;
	}
}