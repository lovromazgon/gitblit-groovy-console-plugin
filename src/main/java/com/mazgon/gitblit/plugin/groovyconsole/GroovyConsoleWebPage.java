package com.mazgon.gitblit.plugin.groovyconsole;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.protocol.http.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author lovro.mazgon
 */
public class GroovyConsoleWebPage extends WebPage {
    private static final String CODE_REQUEST_PARAM = "code";
    private static final String OUTPUT_LABEL_NAME = "output";
    private static final String GROOVY_CODE_LABEL_NAME = "groovycode";

    public GroovyConsoleWebPage(PageParameters parameters) {
        super(parameters);

        String groovyCode = "";
        String output = "";

        final WebRequest wr = (WebRequest) getRequest();
        final HttpServletRequest servletRequest = wr.getHttpServletRequest();
        if (servletRequest.getMethod().equalsIgnoreCase("post")) {
            groovyCode = servletRequest.getParameter(CODE_REQUEST_PARAM);
            try {
                output = Plugin.instance().getGroovyConsole().executeGroovyScript(groovyCode);
            } catch (Exception e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                output = errors.toString();
            }
        }
        add(new Label(OUTPUT_LABEL_NAME, output));
        add(new Label(GROOVY_CODE_LABEL_NAME, groovyCode));
    }
}
