package com.mazgon.gitblit.plugin.groovyconsole;

import com.gitblit.extensions.HttpRequestFilter;
import com.gitblit.manager.IAuthenticationManager;
import com.gitblit.models.UserModel;
import com.gitblit.servlet.GitblitContext;
import ro.fortsoft.pf4j.Extension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author lovro.mazgon
 */
@Extension
public class GroovyConsoleFilter extends HttpRequestFilter {
	private static final String CODE_REQUEST_PARAM = "code";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		System.out.println("IN FILTER");
        if (!(request instanceof HttpServletRequest)
				|| !(response instanceof HttpServletResponse)
				|| !isRequestToGroovyConsole((HttpServletRequest) request)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

		if (!canAccessGroovyConsole(httpRequest)) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden access");
			return;
		}

		if (httpRequest.getMethod().equalsIgnoreCase("post")) {
			String output;
			String groovyCode = httpRequest.getParameter(CODE_REQUEST_PARAM);
			try {
				output = Plugin.instance().getGroovyConsole().executeGroovyScript(groovyCode);
			} catch (Exception e) {
				StringWriter errors = new StringWriter();
				e.printStackTrace(new PrintWriter(errors));
				output = errors.toString();
			}
			httpResponse.getWriter().print(output);
		}
		else {
			chain.doFilter(request, response);
		}
    }

	private boolean isRequestToGroovyConsole(HttpServletRequest httpRequest) {
		String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
		return path.startsWith(Plugin.instance().getGroovyConsolePath());
	}

    private boolean canAccessGroovyConsole(HttpServletRequest httpRequest) {
		IAuthenticationManager authManager = GitblitContext.getManager(IAuthenticationManager.class);
		UserModel user = authManager.authenticate(httpRequest);
		if (user == null) {
			return false;
		}
		return user.canAdmin();
    }
}
