FROM maven:3.6.3-jdk-11
COPY src /home/app/src
COPY pom.xml /home/app
WORKDIR /home/app
EXPOSE 5000
RUN mvn clean package
CMD ["java","-jar","target/backend-0.0.1-SNAPSHOT.jar"]
