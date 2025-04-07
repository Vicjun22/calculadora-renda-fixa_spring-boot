FROM ubuntu:22.04 AS build

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

RUN mvn clean install

FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/di-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]
