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

/**
 * @author lovro.mazgon
 */
@Extension
public class GroovyConsoleFilter extends HttpRequestFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (canAccessGroovyConsole(httpRequest)) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden access");
        }
    }


    private boolean canAccessGroovyConsole(HttpServletRequest httpRequest) {
        if (httpRequest.getRequestURI().equals(Plugin.instance().getGroovyConsolePath())) {
            // request is for the Groovy Console, authorize admins
            IAuthenticationManager authManager = GitblitContext.getManager(IAuthenticationManager.class);
            UserModel user = authManager.authenticate(httpRequest);
            if (user == null) {
                return false;
            }
            return user.canAdmin();
        }
        return true;
    }
}
