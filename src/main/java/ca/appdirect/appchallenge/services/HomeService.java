package ca.appdirect.appchallenge.services;


import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeService {

	@RequestMapping("/")
	public String index(final Model model, final OpenIDAuthenticationToken authentication) {
		model.addAttribute("authenticated", authentication != null ? OpenIDAuthenticationStatus.SUCCESS.equals(authentication.getStatus()) : Boolean.FALSE);
		return "index";
	}
}
