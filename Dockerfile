FROM ubuntu:16.04

MAINTAINER 8coon

ENV PGVER 9.5
RUN apt-get -y update && apt-get install -y postgresql-$PGVER && apt-get clean all

USER postgres

RUN /etc/init.d/postgresql start &&\
    psql --command "CREATE USER docker WITH SUPERUSER PASSWORD 'docker';" &&\
    createdb -E UTF8 -T template0 -O docker docker &&\
    /etc/init.d/postgresql stop

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/$PGVER/main/pg_hba.conf

RUN echo "listen_addresses = '*'" >> /etc/postgresql/$PGVER/main/postgresql.conf
RUN echo "synchronous_commit = off" >> /etc/postgresql/$PGVER/main/postgresql.conf

EXPOSE 5432


USER root

RUN apt-get -y update && apt-get install -y openjdk-8-jdk-headless && apt-get clean all
RUN apt-get -y update && apt-get install -y maven && apt-get clean all

ENV APP /root/app
ADD ./ $APP

WORKDIR $APP
RUN mvn package

EXPOSE 5000

CMD service postgresql start && java -Xms16M -Xmx128M -jar $APP/target/minecraftshire-auth-2.0.0.jar
