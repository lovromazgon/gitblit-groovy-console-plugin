package com.mazgon.gitblit.plugin.groovyconsole;

import com.gitblit.manager.IGitblit;
import com.gitblit.servlet.GitblitContext;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author lovro.mazgon
 */
public class GroovyConsole {
    private static final String GITBLIT_VARIABLE_NAME = "gitblit";

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private GroovyClassLoader groovyClassLoader;

    public GroovyConsole() {
        this.groovyClassLoader = new GroovyClassLoader();
    }

    public String executeGroovyScript(String code) {
        Binding binding = getDefaultBinding();
        return executeGroovyScript(code, binding);
    }

    public String executeGroovyScript(String code, Binding binding) {
        logger.debug("About to execute script: {}", code);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
        PrintStream saveSystemOut = System.out;
        System.setOut(new PrintStream(buffer)) ;

        try {
            GroovyShell shell = new GroovyShell(groovyClassLoader, binding);
            shell.evaluate(code);
            return buffer.toString().trim();
        } finally {
            System.setOut(saveSystemOut);
        }
    }

    private Binding getDefaultBinding() {
        Binding binding = new Binding();
        binding.setVariable(GITBLIT_VARIABLE_NAME, GitblitContext.getManager(IGitblit.class));
        return binding;
    }
}
