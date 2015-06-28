package ca.appdirect.appchallenge.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.openid.OpenIDLoginConfigurer;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.provider.BaseConsumerDetails;
import org.springframework.security.oauth.provider.ConsumerDetailsService;
import org.springframework.security.oauth.provider.InMemoryConsumerDetailsService;
import org.springframework.security.oauth.provider.filter.OAuthProviderProcessingFilter;
import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.oauth.provider.token.InMemoryProviderTokenServices;
import org.springframework.security.oauth.provider.token.OAuthProviderTokenServices;
import org.springframework.security.openid.OpenIDAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import ca.appdirect.appchallenge.model.security.CustomProtectedResourceProcessingFilter;
import ca.appdirect.appchallenge.model.security.UserDetailsAuthService;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private String consumerKey;
	private String consumerSecret;

	public WebSecurityConfig() {
		this.consumerKey    = System.getenv("consumer-key");
		this.consumerSecret = System.getenv("consumer-secret");
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.csrf().disable();

		http
		.authorizeRequests()
		.antMatchers("/", "/appchallenge/**").permitAll()
		.anyRequest().authenticated();


		OpenIDLoginConfigurer<HttpSecurity> openIDLoginConfigurer = http.openidLogin();
		openIDLoginConfigurer.permitAll();
		openIDLoginConfigurer.authenticationUserDetailsService(new UserDetailsAuthService());

		http
		.openidLogin()
		.permitAll()
		.authenticationUserDetailsService(new UserDetailsAuthService())
		.attributeExchange("https://www.appchallenge.com.*")

		.attribute("email")
		.type("http://axschema.org/contact/email")
		.required(Boolean.TRUE)

		.and()
		.attribute("firstname")
		.type("http://axschema.org/namePerson/first")
		.required(Boolean.TRUE)

		.and()
		.attribute("lastname")
		.type("http://axschema.org/namePerson/last")
		.required(Boolean.TRUE);

		LogoutConfigurer<HttpSecurity> logoutConfigurer = http.logout();
		SessionLogout sessionLogout                     = new SessionLogout();
		logoutConfigurer.logoutSuccessHandler(sessionLogout);

		http.addFilterAfter(this.oAuthProviderProcessingFilter(), OpenIDAuthenticationFilter.class);
	}

	@Bean
	public OAuthProviderProcessingFilter oAuthProviderProcessingFilter() {
		List<RequestMatcher> requestMatchers = new ArrayList<>();
		requestMatchers.add(new AntPathRequestMatcher("/appchallenge/**"));
		ProtectedResourceProcessingFilter filter = new CustomProtectedResourceProcessingFilter(requestMatchers);

		filter.setConsumerDetailsService(this.consumerDetailsService());
		filter.setTokenServices(this.providerTokenServices());

		return filter;
	}

	@Bean
	public ConsumerDetailsService consumerDetailsService() {
		Map<String, BaseConsumerDetails> consumerDetailsStore = this.createConsumerDetailStore();

		InMemoryConsumerDetailsService consumerDetailsService = new InMemoryConsumerDetailsService();
		consumerDetailsService.setConsumerDetailsStore(consumerDetailsStore);

		return consumerDetailsService;
	}

	private Map<String, BaseConsumerDetails> createConsumerDetailStore() {
		BaseConsumerDetails consumerDetails = new BaseConsumerDetails();
		consumerDetails.setConsumerKey(this.consumerKey);
		consumerDetails.setSignatureSecret(new SharedConsumerSecretImpl(this.consumerSecret));
		consumerDetails.setRequiredToObtainAuthenticatedToken(Boolean.FALSE);

		Map<String, BaseConsumerDetails> consumerDetailsStore = new HashMap<>();
		consumerDetailsStore.put(this.consumerKey, consumerDetails);
		return consumerDetailsStore;
	}

	@Bean
	public OAuthProviderTokenServices providerTokenServices() {
		return new InMemoryProviderTokenServices();
	}
}