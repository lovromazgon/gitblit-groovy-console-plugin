package com.gitblit.plugin.groovyconsole;

import com.gitblit.manager.IGitblit;
import com.gitblit.servlet.GitblitContext;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lovro.mazgon
 */
public class GroovyConsole {
	public static final Map<String, Object> BINDINGS = new HashMap<String, Object>();
	private static final String OUTPUT_VARIABLE_NAME = "out";

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private GroovyClassLoader groovyClassLoader;

	public GroovyConsole() {
		this.groovyClassLoader = new GroovyClassLoader();

		BINDINGS.put("gitblit", GitblitContext.getManager(IGitblit.class));
	}

	public String executeGroovyScript(String code) {
		return executeGroovyScript(code, getDefaultBinding());
	}

	public String executeGroovyScript(String code, Binding binding) {
		logger.debug("About to execute script: {}", code);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream() ;
		binding.setVariable(OUTPUT_VARIABLE_NAME, new PrintStream(buffer));

		GroovyShell shell = new GroovyShell(groovyClassLoader, binding);
		shell.evaluate(code);
		return buffer.toString().trim();
	}

	private Binding getDefaultBinding() {
		Binding binding = new Binding();
		for (String key : BINDINGS.keySet()) {
			binding.setVariable(key, BINDINGS.get(key));
		}
		return binding;
	}
}