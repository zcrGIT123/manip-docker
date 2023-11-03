FROM maven:3.8.1-openjdk-17-slim AS build

COPY . /home/app/code

WORKDIR /home/app/code

RUN mvn -f /home/app/code/pom.xml clean package -DskipTests



FROM eclipse-temurin:17-jdk-alpine


WORKDIR /home/app/code

COPY --from=build /home/app/code/target/*.jar app.jar
ENV MONGODB_URI=$MONGODB_URI

ENTRYPOINT ["java","-jar","app.jar"]