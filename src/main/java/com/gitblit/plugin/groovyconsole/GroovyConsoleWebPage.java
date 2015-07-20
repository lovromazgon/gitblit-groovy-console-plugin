package com.gitblit.plugin.groovyconsole;

import com.gitblit.wicket.RequiresAdminRole;
import com.gitblit.wicket.pages.RootPage;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.StringRequestTarget;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lovro.mazgon
 */
@RequiresAdminRole
public class GroovyConsoleWebPage extends RootPage {
	public GroovyConsoleWebPage(PageParameters parameters) {
		super(parameters);
		setupPage("", "");

		AbstractReadOnlyModel<List<String>> bindings = new AbstractReadOnlyModel<List<String>>() {
			@Override
			public List<String> getObject() {
				return new ArrayList<String>(GroovyConsole.BINDINGS.keySet());
			}
		};

		add(new ListView<String>("bindings", bindings) {
			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("name", item.getModelObject()));
				item.add(new Label("object", GroovyConsole.BINDINGS.get(item.getModelObject()).getClass().toString()));
			}
		});

		Form<Void> form = new Form<Void>("groovyConsoleForm") {
			private String consoleInput = "";

			@Override
			protected void onSubmit() {
				String output;
				try {
					output = Plugin.instance().getGroovyConsole().executeGroovyScript(consoleInput);
				} catch (Exception e) {
					StringWriter errors = new StringWriter();
					e.printStackTrace(new PrintWriter(errors));
					output = errors.toString();
				}

				getRequestCycle().setRequestTarget(new StringRequestTarget(output));
			}

			public String getConsoleInput() {
				return consoleInput;
			}
		};

		form.add(new TextArea<String>("consoleInput", new PropertyModel<String>(form, "consoleInput")));
		form.setOutputMarkupId(true);
		form.setMarkupId("groovyConsoleForm");

		add(form);
	}

}
