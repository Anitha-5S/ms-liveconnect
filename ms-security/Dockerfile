FROM amazoncorretto:11
VOLUME /tmp
COPY ./build/libs/ms-security-1.0-SNAPSHOT.jar app.jar
EXPOSE 1860
ENTRYPOINT ["java","-jar","/app.jar"]