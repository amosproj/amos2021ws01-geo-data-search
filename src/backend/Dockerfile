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


FROM maven:latest as maven_test
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN --mount=type=cache,target=/root/.m2 mvn test

########Run stage########
FROM openjdk:latest as runner
WORKDIR /app
#copy backend folder
COPY ./ ./src
#copy built app layer by layer
ARG DOCKER_PACKAGING_DIR=/app/target/docker-packaging
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/dependencies ./
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/spring-boot-loader ./
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/snapshot-dependencies ./
COPY --from=maven_build ${DOCKER_PACKAGING_DIR}/application ./

ARG MAVEN_VERSION=3.6.3
ARG USER_HOME_DIR="/root"
ARG BASE_URL=https://apache.osuosl.org/maven/maven-3/${MAVEN_VERSION}/binaries


#install maven
RUN mkdir -p /usr/share/maven /usr/share/maven/ref \
    && curl -fsSL -o /tmp/apache-maven.tar.gz ${BASE_URL}/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    && tar -xzf /tmp/apache-maven.tar.gz -C /usr/share/maven --strip-components=1 \
    && rm -f /tmp/apache-maven.tar.gz \
    && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

#run the app
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
