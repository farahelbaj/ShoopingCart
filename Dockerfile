FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean test package

CMD ["mvn", "test"]