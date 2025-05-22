    FROM openjdk:17-alpine
    RUN adduser -D appuser
    WORKDIR /home/appuser
    COPY target/mottu-patio.jar app.jar
    RUN chown -R appuser:appuser /home/appuser
    USER appuser
    EXPOSE 8080
    CMD ["java", "-jar", "app.jar"]