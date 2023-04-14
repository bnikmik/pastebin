FROM amazoncorretto:11
RUN mkdir -p /DB
WORKDIR /
COPY target/RSS-listener-0.0.1-SNAPSHOT.jar /pastebin.jar
CMD java -jar pastebin.jar