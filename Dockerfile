FROM adoptopenjdk/openjdk11:centos
LABEL maintainer="alexander.alvarez@pragma.com.co" description="DOCKERFILE" vendor="PRAGMA S.A.C" version="latest"
ENV APP_NAME="co-com-pragma-api-photos"
ENV APP_WORKDIR="/opt/app"
RUN mkdir -p $APP_WORKDIR
WORKDIR $APP_WORKDIR
COPY target/$APP_NAME.jar $APP_WORKDIR/$APP_NAME.jar
ENTRYPOINT exec java -Djava.security.egd=file/dev/./urandom -jar $APP_WORKDIR/$APP_NAME.jar