FROM eclipse-temurin:17-jdk
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]