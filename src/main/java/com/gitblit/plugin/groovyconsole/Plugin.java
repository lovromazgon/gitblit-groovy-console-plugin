package com.gitblit.plugin.groovyconsole;

import com.gitblit.extensions.GitblitWicketPlugin;
import com.gitblit.wicket.GitblitWicketApp;
import ro.fortsoft.pf4j.PluginException;
import ro.fortsoft.pf4j.PluginWrapper;
import ro.fortsoft.pf4j.Version;

/**
 * @author lovro.mazgon
 */
public class Plugin extends GitblitWicketPlugin {
    private static final String GROOVY_CONSOLE_PATH = "/groovyconsole";

    private static Plugin instance;

    private GroovyConsole groovyConsole;

    public static Plugin instance() {
        return instance;
    }

    public Plugin(PluginWrapper wrapper) {
        super(wrapper);
        instance = this;
        groovyConsole = new GroovyConsole();
    }

    @Override
    protected void init(GitblitWicketApp app) {
        app.mount(GROOVY_CONSOLE_PATH, GroovyConsoleWebPage.class);
    }

    @Override
    public void start() throws PluginException {
        log.debug("{} STARTED.", getWrapper().getPluginId());
    }

    @Override
    public void stop() throws PluginException {
        log.debug("{} STOPPED.", getWrapper().getPluginId());
    }

    @Override
    public void onInstall() {
        log.debug("{} INSTALLED.", getWrapper().getPluginId());
    }

    @Override
    public void onUpgrade(Version oldVersion) {
        log.debug("{} UPGRADED from {}.", getWrapper().getPluginId(), oldVersion);
    }

    @Override
    public void onUninstall() {
        log.debug("{} UNINSTALLED.", getWrapper().getPluginId());
    }

    public GroovyConsole getGroovyConsole() {
        return groovyConsole;
    }

    public String getGroovyConsolePath() {
        return GROOVY_CONSOLE_PATH;
    }
}
