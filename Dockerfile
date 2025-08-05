# 🔧 Stage 1: Build WAR with Maven and JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy dependencies and source
COPY pom.xml .
COPY src ./src

# Build WAR and rename it to ROOT.war (for root context)
RUN mvn -B clean package -DskipTests && \
    cp target/*.war target/ROOT.war

# 🚀 Stage 2: Deploy to JBoss EAP 8 on OpenShift with OpenJDK 17
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-builder-openshift-rhel8

# Copy WAR to deployments
COPY --from=builder /app/target/ROOT.war /opt/eap/standalone/deployments/

# Optional: force WAR to deploy immediately
RUN touch /opt/eap/standalone/deployments/ROOT.war.dodeploy

# ✅ Ensure logs appear in OpenShift pod logs
ENV JAVA_OPTS_APPEND="-Djava.util.logging.manager=org.jboss.logmanager.LogManager"


EXPOSE 8080 9990
