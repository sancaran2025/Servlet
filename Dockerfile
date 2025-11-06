FROM tomcat:9.0
COPY target/SancaranServlet-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
