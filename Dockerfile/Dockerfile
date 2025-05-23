# Etapa 1: build do projeto usando Maven
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: execução com Java 17 leve e seguro
FROM eclipse-temurin:17-jdk-alpine

# Criar usuário não root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

WORKDIR /home/appuser/app
COPY --from=builder /app/target/safeyard-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
