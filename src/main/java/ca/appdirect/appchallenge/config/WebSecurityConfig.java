package ca.appdirect.appchallenge.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.InMemoryConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.OAuthProviderProcessingFilter;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import ca.appdirect.appchallenge.model.security.CustomProtectedResourceProcessingFilter;
import ca.appdirect.appchallenge.model.security.CustomUserDetailsService;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = LogManager.getLogger(WebSecurityConfig.class);

	private String consumerKey;

	private String consumerSecret;

	public WebSecurityConfig() {
		this.consumerKey = System.getenv("consumer-key");
		this.consumerSecret = System.getenv("consumer-secret");
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http
		.csrf().disable();
		http
		.authorizeRequests()
		.antMatchers("/", "/home", "/appchallenge/**", "/test/**").permitAll()
		.anyRequest().authenticated();
		http
		.openidLogin()
		.permitAll()
		.authenticationUserDetailsService(new CustomUserDetailsService())
		.attributeExchange("https://www.appchallenge.com.*")
		.attribute("email")
		.type("http://axschema.org/contact/email")
		.required(true)
		.and()
		.attribute("firstname")
		.type("http://axschema.org/namePerson/first")
		.required(true)
		.and()
		.attribute("lastname")
		.type("http://axschema.org/namePerson/last")
		.required(true);
		http
		.logout()
		.logoutSuccessHandler(new CustomLogoutSuccessHandler());
		http
		.addFilterAfter(this.oAuthProviderProcessingFilter(), OpenIDAuthenticationFilter.class);
	}

	@Bean
	OAuthProviderProcessingFilter oAuthProviderProcessingFilter() {
		List<RequestMatcher> requestMatchers = new ArrayList<>();
		requestMatchers.add(new AntPathRequestMatcher("/appchallenge/**"));
		ProtectedResourceProcessingFilter filter = new CustomProtectedResourceProcessingFilter(requestMatchers);

		filter.setConsumerDetailsService(this.consumerDetailsService());
		filter.setTokenServices(this.providerTokenServices());

		return filter;
	}

	@Bean
	public ConsumerDetailsService consumerDetailsService() {
		WebSecurityConfig.LOGGER.debug("consumerKey: " + this.consumerKey + " | consumerSecret: " + this.consumerSecret);

		InMemoryConsumerDetailsService consumerDetailsService = new InMemoryConsumerDetailsService();

		BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
		consumerDetails.setConsumerKey(this.consumerKey);
		consumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(this.consumerSecret));
		consumerDetails.setRequiredToObtainAuthenticatedToken(false);

		Map<String, BaseConsumerDetails> consumerDetailsStore = new HashMap<>();
		consumerDetailsStore.put(this.consumerKey, consumerDetails);

		consumerDetailsService.setConsumerDetailsStore(consumerDetailsStore);

		return consumerDetailsService;
	}

	@Bean
	public OAuthProviderTokenServices providerTokenServices() {
		return new InMemoryProviderTokenServices();
	}


	private class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
		@Override
		public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
			if (authentication instanceof OpenIDAuthenticationToken) {
				OpenIDAuthenticationToken openIDToken = (OpenIDAuthenticationToken) authentication;
				response.sendRedirect("https://www.appdirect.com/applogout?openid=" + openIDToken.getIdentityUrl());
			} else {
				response.sendRedirect("/?logout");
			}

			return;
		}
	}

}
