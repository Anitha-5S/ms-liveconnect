FROM amazoncorretto:11
VOLUME /tmp
COPY ./build/libs/ms-lc-user-1.0-SNAPSHOT.jar app.jar
EXPOSE 1962
ENTRYPOINT ["java","-jar","/app.jar"]