FROM openjdk:17.0.2
EXPOSE 8080:8080
ARG DB_TYPE
ARG DB_URL
ARG DB_USER
ARG DB_PASSWORD
ENV DB_TYPE=$DB_TYPE \
    DB_URL=$DB_URL \
    DB_USER=$DB_USER \
    DB_PASSWORD=$DB_PASSWORD
RUN mkdir /app
COPY ./build/libs/*-all.jar /app/ktor-task-app.jar
ENTRYPOINT ["java", "-jar", "/app/ktor-task-app.jar"]