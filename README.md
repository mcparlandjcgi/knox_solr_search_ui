# Getting Started
 * `git clone git@github.com:mcparlandjcgi/knox_solr_search_ui.git`
 * `cd knox_solr_search_ui`

## Install Tools
 * `sudo apt-get install npm`
 * `sudo npm install bower -g`
 * `sudo apt-get install mvn` 

----

# Building

## HTML5
 * `bower install bootstrap angular-route --save`

## Java
 * `mvn clean package install`

----

# Installing
## Install
 * `chmod u+x *.sh`
 * `sudo mkdir -p /usr/knox_solr_search_ui/logs`
 * `sudo chmod -R 775 /usr/knox_solr_search_ui`
 * `./install.sh`
 * [Get Round the SSL problem with self-signed certificates](#GettingRoundSSLProblem) if necessary

## Configure
 * `sudo <<editor>> /usr/knox_solr_search_ui/override.properties`
 * Modify `config.ipaddress` to be the IP address/server name of the server running Knox.
 * Modify `config.password` to be the `guest` users password for Knox.
   * You can modify `config.username` too if the `guest` user is not correct.

----

# Running
 * `cd /usr/knox_solr_search_ui`
 * `sudo ./run.sh &`

----

# Getting Round SSL Problem

## Get a Self-Signed Certificate
 * Browse the server using Firefox
 * Right click -> View Page Information
 * Choose security
 * View Certificate
 * Details
 * Export it!
 * Save it

## Import a Self-Signed Certificate
This instruction presumes that your Java installation is at `/usr/lib/jvm/java-8-oracle` - change the path below if not.

 * ```sudo keytool -importcert -file ${HOME}/hdp24sandbox.crt -alias hdp24sandbox -keystore /usr/lib/jvm/java-8-oracle/jre/lib/security/cacerts```
   * Default password is `changeit`

----

