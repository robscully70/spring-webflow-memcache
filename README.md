Welcome:
====================
This project is to demonstrate an Issue I am having with memcache (or more likely the connector from [memcached-session-manager](https://code.google.com/p/memcached-session-manager/) ).


To get the code:
====================
Clone the repository:

    $ git clone git://github.com/tyrinslys/spring-webflow-memcache.git

If this is your first time using Github, review http://help.github.com to learn the basics.

Setup
====================
You will need to have Maven, Tomcat 7, and memcached installed prior to anything else.

It is important you setup Tomcat 7 rather than using the embedded Maven Tomcat.

Quick reference:
1. create Tomcat user for delpoyment with Maven
1. copy context from project to Tomcat
1. add settings to Maven for Tomcat deployment user

Tomcat User Setup
-----------------
In at apache-tomcat-7.0.57/conf/tomcat-users.xml add user:

    <user username="deploymentUser" password="deploy12345password" roles="manager-gui,manager-script"/>

Also add roles if they are not in already:

    <role rolename="manager-gui"/>
    <role rolename="manager-script"/>

Tomcat Context Setup
--------------------
Copy file `src/main/webapp/WEB-INF/context.xml` to `apache-tomcat-7.0.57/conf/context.xml`  

Maven Server Profile Settings
-----------------------------
In your `.m2/settings.xml` file add the following in your <servers> tag

    <server>
      <id>local-tomcat</id>
      <username>deploymentUser</username>
      <password>deploy12345password</password>
    </server>

To run the application:
====================
1. Start memcached:

        $ memcached -d -m 30 -l 127.0.0.1 -p 11211

1. Start Tomcat:

        $ apache-tomcat-7.0.57/bin/startup.sh

1. From the command line with Maven:

        $ cd spring-webflow-memcache
        $ mvn clean -DskipTests=true tomcat7:deploy

    And to redeploy run:

        $ mvn mvn clean -DskipTests=true tomcat7:redeploy

1. The webflow test is at [http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=grow](http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=grow)

Other Test Modes
----------------
by default the grow strategy is used. This can be changed with a debug variable that is intended to be set at the start of the flow.

* grow -[http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=grow](http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=grow)
* static size, unchangeing [http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=static-nonchanging](http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=static-nonchanging)
* static size, changeing [http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=static-changing](http://localhost:8080/spring-mvc-showcase/flow/testFlow?debug=static-changing)

Spring MVC Showcase
====================

This project was forked from [spring-mvc-showcase](https://github.com/spring-projects/spring-mvc-showcase) and I didn't disable anything from that project.

You can still access the deployed web application at: [http://localhost:8080/spring-mvc-showcase/](http://localhost:8080/spring-mvc-showcase/)
