FROM debian:stable-slim

ARG JFROG_USER
ARG JFROG_PASS

LABEL Description="Chouette IEV"

#--- Java, wget & locales
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8
RUN echo "deb http://http.debian.net/debian stretch-backports main" > /etc/apt/sources.list.d/stretch-backports.list && \
    mkdir -p /usr/share/man/man1/ && \
    DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y --no-install-recommends wget netcat-traditional locales && \
    localedef -i en_US -c -f UTF-8 en_US.UTF-8 && \
    apt-get install -y -t stretch-backports --no-install-recommends openjdk-8-jre-headless && rm -rf /var/lib/apt/lists/*

#--- Wildfly

ENV WILDFLY_HOME=/opt/wildfly WILDFLY_VERSION=9.0.2.Final
COPY config/*.xml /install/

RUN cd /install && wget -q https://download.jboss.org/wildfly/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.tar.gz && \
    cd /opt ; tar xzf /install/wildfly-${WILDFLY_VERSION}.tar.gz ; ln -s wildfly-${WILDFLY_VERSION} wildfly && rm /install/wildfly-${WILDFLY_VERSION}.tar.gz
RUN cd /install && wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/org/postgresql/postgresql/9.4-1206-jdbc41/postgresql-9.4-1206-jdbc41.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/net/postgis/postgis-jdbc/2.2.1/postgis-jdbc-2.2.1.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/libs-release-local/hibernate-spatial/hibernate-spatial/4.3/hibernate-spatial-4.3.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/com/vividsolutions/jts/1.13/jts-1.13.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/org/hibernate/hibernate-core/4.3.11.Final/hibernate-core-4.3.11.Final.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/org/hibernate/hibernate-envers/4.3.11.Final/hibernate-envers-4.3.11.Final.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/org/hibernate/hibernate-entitymanager/4.3.11.Final/hibernate-entitymanager-4.3.11.Final.jar && \
	wget --http-user=${JFROG_USER} --http-password=${JFROG_PASS} https://entur2.jfrog.io/entur2/jcenter/org/hibernate/hibernate-infinispan/4.3.11.Final/hibernate-infinispan-4.3.11.Final.jar && \
    mkdir -p $WILDFLY_HOME/modules/org/postgres/main && \
    cd /install && cp post*.jar $WILDFLY_HOME/modules/org/postgres/main && \
    cp module_postgres.xml $WILDFLY_HOME/modules/org/postgres/main/module.xml && \
    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/hibernate*4.3.10.Final.jar && \
    cp hibernate-core*.jar hibernate-envers*.jar hibernate-entitymanager*.jar hibernate-jpa-2.1-api-1.0.0.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
    cp hibernate-spatial-4.3.jar jts-1.13.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
    cp -f module_hibernate.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/module.xml && \
    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/hibernate*4.3.10.Final.jar && \
    cp hibernate-infinispan-4.3.11.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/ && \
    cp -f module_infinispan.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/module.xml && \
    rm -rf /install/

COPY boiv_iev/target/chouette_iev.ear /chouette_iev.ear

COPY boiv.properties /etc/chouette/boiv/

ENV POSTGRES_HOST="db" POSTGRES_NAME="chouette" POSTGRES_USER="chouette" BOIV_GUI_URL_BASE="http://front:3000/"
ENV JAVA_OPTS='-Xms64m -Xmx768m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true'

WORKDIR /

EXPOSE 8080 9990
COPY docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]
