package ca.appdirect.appchallenge.services;


import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.appdirect.appchallenge.model.lib.Greeting;

@RestController
public class HomeService {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping("/greeting")
	public Greeting greeting(@RequestParam(value="name", defaultValue="World") final String name) {
		return new Greeting(this.counter.incrementAndGet(),
				String.format(HomeService.template, name));
	}
}
