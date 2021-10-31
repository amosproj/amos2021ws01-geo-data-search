FROM openjdk:latest as builder
WORKDIR /src
COPY . /src
RUN javac main/HelloWorldMain.java -d /build

FROM openjdk:latest
WORKDIR /app
COPY --from=builder /build/ /app
ENTRYPOINT ["java", "main.HelloWorldMain"]
