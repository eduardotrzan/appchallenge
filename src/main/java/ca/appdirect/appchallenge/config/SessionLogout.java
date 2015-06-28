package ca.appdirect.appchallenge.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class SessionLogout implements LogoutSuccessHandler {

	@Override
	public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
		String sendRedirect;
		if (authentication instanceof OpenIDAuthenticationToken) {
			OpenIDAuthenticationToken openIDToken = (OpenIDAuthenticationToken) authentication;
			sendRedirect = String.format("https://www.appdirect.com/applogout?openid=%s",
					openIDToken.getIdentityUrl()
					);
		} else {
			sendRedirect = "/?logout";
		}

		response.sendRedirect(sendRedirect);
	}
}