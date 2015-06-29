package ca.appdirect.appchallenge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.appdirect.appchallenge.model.bo.PortalBO;

@Controller
public class HomeService {

	@Autowired
	private PortalBO portalBO;

	@RequestMapping("/")
	public String index(final Model model, final OpenIDAuthenticationToken authentication) {
		this.authenticate(model, authentication);
		return "index";
	}

	@RequestMapping(value = "/allAccounts", method = RequestMethod.GET)
	public String showCurrentUser(final Model model, final OpenIDAuthenticationToken authentication) {
		this.authenticate(model, authentication);
		model.addAttribute("companies", this.portalBO.findAllOrderingCompanies());
		return "accounts";
	}

	private void authenticate(final Model model, final OpenIDAuthenticationToken authentication) {
		if (authentication != null) {
			if (OpenIDAuthenticationStatus.SUCCESS.equals(authentication.getStatus())) {
				model.addAttribute("authenticated", Boolean.TRUE);
			} else {
				model.addAttribute("authenticated", Boolean.FALSE);
			}
		} else {
			model.addAttribute("authenticated", Boolean.FALSE);
		}
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