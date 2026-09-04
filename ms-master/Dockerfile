FROM amazoncorretto:11
VOLUME /tmp
COPY ./build/libs/ms-master-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]