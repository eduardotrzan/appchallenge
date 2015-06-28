package ca.appdirect.appchallenge.model.security;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.oauth.provider.filter.ProtectedResourceProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CustomProtectedResourceProcessingFilter extends ProtectedResourceProcessingFilter {

	private List<RequestMatcher> requestMatchers;

	public CustomProtectedResourceProcessingFilter(final List<RequestMatcher> requestMatchers) {
		this.requestMatchers = requestMatchers;
	}

	@Override
	protected boolean requiresAuthentication(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) {
		if ((this.requestMatchers != null) && !this.requestMatchers.isEmpty()) {
			for (RequestMatcher requestMatcher : this.requestMatchers) {
				if (requestMatcher.matches(request)) {
					return Boolean.TRUE;
				}
			}
		}

		return Boolean.FALSE;
	}
}
