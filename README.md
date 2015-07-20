gitblit-groovy-console-plugin
=============================

This is a plugin for Gitblit (http://gitblit.com/) that allows administrators to write groovy scripts and execute them inside the Gitblit context. For the code editor it uses CodeMirror javascript library (http://codemirror.net/).

Administrators can access the plugin inside the user menu under the link "groovy console". The com.gitblit.manager.IGitblit object is accessible under the variable "gitblit".

installation
------------

[Download the zip file] (https://github.com/lovromazgon/gitblit-groovy-console-plugin/raw/master/lib/groovyconsole-1.0.0.zip) or compile the source code with maven goal "package" and a zip file will appear in the target folder. Copy the zip file to the plugins folder (defaults to gitblit.war/WEB-INF/data/plugins) and restart gitblit.
