nginx.conf, sites-available, and sites-enabled should be placed in the configuration directory for nginx, typically /etc/nginx. The sites-enabled folder should contain symbolic links to corresponding sites-available configuration files.

Some site specific configuration is included in the nginx.conf file due to the way nginx parses certain commands. Typically all configuration should be managed in a site specific config in sites-available.
