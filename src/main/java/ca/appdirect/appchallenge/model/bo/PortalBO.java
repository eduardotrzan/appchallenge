package ca.appdirect.appchallenge.model.bo;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.appdirect.appchallenge.model.dao.IOrderingCompanyDAO;
import ca.appdirect.appchallenge.model.dao.ITargetMarketPlaceDAO;
import ca.appdirect.appchallenge.model.dao.IUserDAO;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResult;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResultFail;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResultFail.Code;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResultSuccess;
import ca.appdirect.appchallenge.model.lib.appdirect.Notice;
import ca.appdirect.appchallenge.model.lib.appdirect.enums.AccountStatus;
import ca.appdirect.appchallenge.model.lib.appdirect.enums.EditionCode;
import ca.appdirect.appchallenge.model.lib.database.OrderingCompany;
import ca.appdirect.appchallenge.model.lib.database.TargetMarketPlace;
import ca.appdirect.appchallenge.model.lib.database.User;
import ca.appdirect.appchallenge.model.lib.database.User.Profile;

@Service
public class PortalBO {

	private static final Logger LOGGER = LogManager.getLogger(PortalBO.class);

	@Autowired
	private IUserDAO userDAO;

	@Autowired
	private IOrderingCompanyDAO orderingCompanyDAO;

	@Autowired
	private ITargetMarketPlaceDAO targetMarketPlaceDAO;

	public EventResult registerOrder(final OrderingCompany orderingCompany, final TargetMarketPlace targetMarketPlace, final User user) {
		orderingCompany.setStatus(AccountStatus.FREE_TRIAL);
		OrderingCompany savedOrderingCompany = this.orderingCompanyDAO.save(orderingCompany);

		targetMarketPlace.setOrderingCompany(savedOrderingCompany);
		TargetMarketPlace savedTargetMarketPlace = this.targetMarketPlaceDAO.save(targetMarketPlace);

		user.setProfile(Profile.ADM);
		user.setOrderingCompany(savedOrderingCompany);
		User savedUser = this.userDAO.save(user);

		String successMsg = String.format("Saved: User with id #%d, Target Market Place with id #%d and Ordering Company wit id: #%d"
				, savedUser.getId()
				, savedTargetMarketPlace.getId()
				, savedOrderingCompany.getId()
				);
		PortalBO.LOGGER.info(successMsg);

		EventResultSuccess eventResultSuccess = new EventResultSuccess();
		eventResultSuccess.setAccountIdentifier(savedOrderingCompany.getAccountIdentifier());
		eventResultSuccess.setMessage("The order was subscribed with success.");
		return eventResultSuccess;
	}

	public EventResult changeOrder(final String accountIdentifier, final EditionCode editionCode) {
		OrderingCompany orderingCompany = this.orderingCompanyDAO.findByAccountIdentifier(accountIdentifier);

		this.checkAccountNotFound(orderingCompany);

		AccountStatus status = null;
		if (editionCode == EditionCode.FREE) {
			status = AccountStatus.ACTIVE;
		} else if ((editionCode == EditionCode.BASIC) || (editionCode == EditionCode.ADVANCED) || (editionCode == EditionCode.PREMIUM)) {
			status = AccountStatus.FREE_TRIAL;
		} else {
			EventResultFail eventResultFail = this.createFailResult(Code.UNKNOWN_ERROR
					, "There unknown edition code."
					);
			return eventResultFail;
		}

		EventResult eventResult = this.saveAndCreateEvent(orderingCompany, status, "Changed");
		return eventResult;
	}

	public EventResult cancelOrder(final String accountIdentifier) {
		OrderingCompany orderingCompany = this.orderingCompanyDAO.findByAccountIdentifier(accountIdentifier);

		EventResult eventResult = this.checkAccountNotFound(orderingCompany);
		if (eventResult != null) {
			return eventResult;
		}
		eventResult = this.saveAndCreateEvent(orderingCompany, AccountStatus.CANCELLED, "Cancelled");
		return eventResult;
	}

	public EventResult noticeOrder(final String accountIdentifier, final Notice.Type noticeType, final AccountStatus status) {
		OrderingCompany orderingCompany = this.orderingCompanyDAO.findByAccountIdentifier(accountIdentifier);

		EventResult eventResult = this.checkAccountNotFound(orderingCompany);
		if (eventResult != null) {
			return eventResult;
		}

		if (noticeType == Notice.Type.CLOSED) {
			if ((status == AccountStatus.FREE_TRIAL_EXPIRED) || (status == AccountStatus.SUSPENDED)) {
				this.orderingCompanyDAO.delete(orderingCompany);

				EventResultSuccess eventResultSuccess = new EventResultSuccess();
				eventResultSuccess.setAccountIdentifier(orderingCompany.getAccountIdentifier());
				String message = String.format("The order was deleted with success.");
				eventResultSuccess.setMessage(message);
				return eventResultSuccess;
			} else {
				return this.cancelOrder(accountIdentifier);
			}
		} else if (noticeType == Notice.Type.DEACTIVATED) {
			eventResult = this.saveAndCreateEvent(orderingCompany, AccountStatus.SUSPENDED, "Suspended");
			return eventResult;
		} else if (noticeType == Notice.Type.REACTIVATED) {
			eventResult = this.saveAndCreateEvent(orderingCompany, AccountStatus.ACTIVE, "Actived");
			return eventResult;
		} else if (noticeType == Notice.Type.UPCOMING_INVOICE) {
			return this.notifyUser();
		}

		EventResultFail eventResultFail = this.createFailResult(Code.UNKNOWN_ERROR
				, "There unknown notice type."
				);
		return eventResultFail;
	}

	private EventResultFail checkAccountNotFound(final OrderingCompany orderingCompany) {
		if (orderingCompany == null) {
			EventResultFail eventResultFail = this.createFailResult(Code.ACCOUNT_NOT_FOUND
					, "There is no registered Company with the given account identifier."
					);
			return eventResultFail;
		}
		return null;
	}

	private EventResultFail createFailResult(final Code code, final String message) {
		EventResultFail eventResultFail = new EventResultFail();
		eventResultFail.setCode(code);
		eventResultFail.setMessage(message);
		return eventResultFail;
	}

	private EventResult saveAndCreateEvent(final OrderingCompany orderingCompany, final AccountStatus status, final String action) {
		orderingCompany.setStatus(status);
		OrderingCompany savedOrderingCompany = this.orderingCompanyDAO.save(orderingCompany);

		String successMsg = String.format("%s: Ordering Company with id: #%d"
				, action
				, savedOrderingCompany.getId()
				);
		PortalBO.LOGGER.info(successMsg);

		EventResultSuccess eventResultSuccess = new EventResultSuccess();
		eventResultSuccess.setAccountIdentifier(savedOrderingCompany.getAccountIdentifier());
		String message = String.format("The order was %s with success.", action.toLowerCase());
		eventResultSuccess.setMessage(message);
		return eventResultSuccess;
	}

	private EventResult notifyUser() {
		return null; //TODO implement mail notification
	}

	@SuppressWarnings("unchecked")
	public List<OrderingCompany> findAllOrderingCompanies() {
		Iterable<OrderingCompany> orderingCompaniesIt = this.orderingCompanyDAO.findAll();
		return IteratorUtils.toList(orderingCompaniesIt.iterator());
	}

	/* ##########################################
	 * ##          Getters and setters         ##
	 * ##########################################
	 */

	public IUserDAO getUserDAO() {
		return this.userDAO;
	}

	public void setUserDAO(final IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public IOrderingCompanyDAO getOrderingCompanyDAO() {
		return this.orderingCompanyDAO;
	}

	public void setOrderingCompanyDAO(final IOrderingCompanyDAO orderingCompanyDAO) {
		this.orderingCompanyDAO = orderingCompanyDAO;
	}

	public ITargetMarketPlaceDAO getTargetMarketPlaceDAO() {
		return this.targetMarketPlaceDAO;
	}

	public void setTargetMarketPlaceDAO(final ITargetMarketPlaceDAO targetMarketPlaceDAO) {
		this.targetMarketPlaceDAO = targetMarketPlaceDAO;
	}
}