# 🔧 Stage 1: Build WAR with Maven and JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy Maven project and build
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# ⚙️ Stage 2: Provision EAP 8 with Builder image
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-builder-openshift-rhel8 AS eap-builder

# Set required env for provisioning
ENV EAP_HOME=/opt/eap \
    JBOSS_HOME=/opt/eap \
    INSTALL_DIR=/server

# Run provisioning to create JBoss EAP 8 server with default configuration
RUN $EAP_HOME/bin/openshift-launch.sh --cli-script="embed-server --std-out=echo"

# 🏁 Stage 3: Final runtime image with JBoss EAP 8 and your app
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift-rhel8

ENV JBOSS_HOME=/opt/eap

# Copy provisioned server from builder
COPY --from=eap-builder /opt/eap $JBOSS_HOME

# Deploy WAR to /noteworthy path
COPY --chown=jboss:root --from=builder /app/target/noteworthy.war $JBOSS_HOME/standalone/deployments/

# ✅ Logs for OpenShift
ENV JAVA_OPTS_APPEND="-Djava.util.logging.manager=org.jboss.logmanager.LogManager"

EXPOSE 8080 9990
