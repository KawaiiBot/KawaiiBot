FROM openjdk:alpine
MAINTAINER Auri <me@aurieh.me>
ENV GRADLE_VERSION "4.10.2"

RUN apk update && apk upgrade && apk add curl

RUN curl -Os --location https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
  mkdir /opt && \
  unzip -q gradle-${GRADLE_VERSION}-bin.zip -d /opt && \
  ln -s /opt/gradle-${GRADLE_VERSION} /opt/gradle && \
  rm -f gradle-${GRADLE_VERSION}-bin.zip

ENV PATH=${PATH}:/opt/gradle/bin

WORKDIR /src
COPY *gradle* /src/
RUN gradle --no-daemon build

COPY . .
RUN gradle --no-daemon shadowJar

WORKDIR /app

RUN mv /src/build/libs/kawaiibot-*-all.jar /app/kawaiibot.jar
RUN mv /src/*.properties /app/

# NOTE: Uncomment when in production
# CMD ["java", "-XX:+UseG1GC", "-Xmx28G", "-jar", "/app/kawaiibot.jar"]
CMD ["java", "-jar", "/app/kawaiibot.jar"]
