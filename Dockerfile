# 🔧 Stage 1: Build WAR
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ⚙️ Stage 2: Use EAP Builder to assemble server
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-builder-openshift-rhel8 AS eap-builder

ENV JBOSS_HOME=/opt/eap

# Copy WAR with correct ownership
COPY --chown=jboss:root --from=builder /app/target/noteworthy.war $JBOSS_HOME/standalone/deployments/

# Provision the server using the standard EAP 8 builder image process
RUN /usr/local/s2i/assemble

# 🏁 Stage 3: Runtime Image
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift-rhel8

ENV JBOSS_HOME=/opt/eap

COPY --from=eap-builder $JBOSS_HOME/ $JBOSS_HOME/

ENV JAVA_OPTS_APPEND="-Djava.util.logging.manager=org.jboss.logmanager.LogManager"

EXPOSE 8080 9990
