# 🔧 Stage 1: Build WAR with Maven and JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy dependencies and source
COPY pom.xml .
COPY src ./src

# Build WAR (output will be target/noteworthy.war)
RUN mvn -B clean package -DskipTests

# 🚀 Stage 2: Deploy to JBoss EAP 8 on OpenShift with OpenJDK 17
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-builder-openshift-rhel8

# Copy WAR directly to deployments
COPY --from=builder /app/target/noteworthy.war /opt/eap/standalone/deployments/

# Mark the WAR for deployment
RUN touch /opt/eap/standalone/deployments/noteworthy.war.dodeploy

# ✅ Ensure logs go to OpenShift logs
ENV JAVA_OPTS_APPEND="-Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# Ports exposed
EXPOSE 8080 9990
