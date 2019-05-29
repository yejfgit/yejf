FROM java:8
VOLUME /tmp
#ARG JAR_FILE
ADD yejf-web-1.0.0.jar app.jar
RUN bash -c 'touch /app.jar'
#EXPOSE 8800
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]