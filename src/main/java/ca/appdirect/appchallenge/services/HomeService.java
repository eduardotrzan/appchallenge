package ca.appdirect.appchallenge.services;


import java.util.concurrent.atomic.AtomicLong;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.appdirect.appchallenge.model.lib.Greeting;

@RestController
public class HomeService {

	private static final Logger LOGGER = LogManager.getLogger(HomeService.class);

	@RequestMapping("/")
	public String index(final Model model, final OpenIDAuthenticationToken authentication) {
		HomeService.LOGGER.debug("authentication: " + authentication);
		model.addAttribute("authenticated", authentication != null ? OpenIDAuthenticationStatus.SUCCESS.equals(authentication.getStatus()) : Boolean.FALSE);
		return "index";
	}

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/appchallenge/greeting")
	public Greeting greeting(@RequestParam(value="name", defaultValue="World") final String name) {
		return new Greeting(this.counter.incrementAndGet(),
				String.format(HomeService.template, name));
	}
}
