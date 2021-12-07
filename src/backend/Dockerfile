# syntax=docker/dockerfile:experimental

########Maven build stage########
FROM maven:latest as maven_build
WORKDIR /app

#copy pom
COPY pom.xml .

#copy source
COPY src ./src

# build the app and download dependencies only when these are new (thanks to the cache)
RUN --mount=type=cache,target=/root/.m2  mvn clean package -Dmaven.test.skip

# split the built app into multiple layers to improve layer rebuild
RUN mkdir -p target/docker-packaging && cd target/docker-packaging && java -Djarmode=layertools -jar ../backend-0.0.1-SNAPSHOT.jar extract

########Run stage########
FROM openjdk:latest
WORKDIR /app
#copy built app layer by layer
ARG DOCKER_PACKAGING_DIR=/app/target/docker-packaging
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/dependencies ./
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/spring-boot-loader ./
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/snapshot-dependencies ./
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/application ./
#run the app
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
