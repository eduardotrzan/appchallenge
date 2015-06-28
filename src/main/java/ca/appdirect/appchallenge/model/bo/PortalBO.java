package ca.appdirect.appchallenge.model.bo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.appdirect.appchallenge.model.dao.IOrderingCompanyDAO;
import ca.appdirect.appchallenge.model.dao.ITargetMarketPlaceDAO;
import ca.appdirect.appchallenge.model.dao.IUserDAO;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResult;
import ca.appdirect.appchallenge.model.lib.appdirect.EventResultSuccess;
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
		OrderingCompany savedOrderingCompany = this.orderingCompanyDAO.save(orderingCompany);

		targetMarketPlace.setOrderingCompany(savedOrderingCompany);
		TargetMarketPlace savedTargetMarketPlace = this.targetMarketPlaceDAO.save(targetMarketPlace);

		user.setProfile(Profile.ADM);
		user.setOrderingCompany(savedOrderingCompany);
		User savedUser 	= this.userDAO.save(user);


		String successMsg = String.format("Saved: User with id #%d, Target Market Place with id #%d and Ordering Company wit id: #d"
				, savedUser.getId()
				, savedTargetMarketPlace.getId()
				, savedOrderingCompany.getId()
				);
		PortalBO.LOGGER.info(successMsg);

		EventResultSuccess eventResultSuccess = new EventResultSuccess();
		eventResultSuccess.setAccountIdentifier(savedOrderingCompany.getId().toString());
		eventResultSuccess.setMessage("The order was subscribed with success.");
		return eventResultSuccess;
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