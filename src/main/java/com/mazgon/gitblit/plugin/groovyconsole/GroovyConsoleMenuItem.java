package com.mazgon.gitblit.plugin.groovyconsole;

import com.gitblit.extensions.UserMenuExtension;
import com.gitblit.models.Menu;
import com.gitblit.models.UserModel;
import ro.fortsoft.pf4j.Extension;

import java.util.ArrayList;
import java.util.List;

import static com.gitblit.models.Menu.MenuItem;

/**
 * @author lovro.mazgon
 */

@Extension
public class GroovyConsoleMenuItem extends UserMenuExtension {
    public static final String LINK_TEXT = "groovy console";

    @Override
    public List<MenuItem> getMenuItems(UserModel userModel) {
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        if (userModel.canAdmin()) {
            menuItems.add(new Menu.PageLinkMenuItem(LINK_TEXT, GroovyConsoleWebPage.class));
        }
        return menuItems;
    }
}
