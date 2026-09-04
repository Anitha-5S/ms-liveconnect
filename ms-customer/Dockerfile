FROM amazoncorretto:11
VOLUME /tmp
COPY ./build/libs/ms-customer-1.0-SNAPSHOT.jar app.jar
EXPOSE 1960
ENTRYPOINT ["java","-jar","/app.jar"]