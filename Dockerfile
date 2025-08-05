# ------------------------
# 🔧 Stage 1: Build the WAR
# ------------------------
FROM registry.access.redhat.com/ubi8/openjdk-17 AS builder

# Set working directory
WORKDIR /app

# Copy Maven config and source code
COPY pom.xml .
COPY src ./src

# Download dependencies and build WAR (skip tests)
RUN ./mvnw -B dependency:go-offline
RUN ./mvnw -B clean package -DskipTests

# ------------------------
# 🚀 Stage 2: Deploy to JBoss EAP 8
# ------------------------
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift

# Copy WAR to JBoss deployment directory
COPY --from=builder /app/target/noteworthy.war /opt/eap/standalone/deployments/

# Set required environment variables for OpenShift EAP
ENV JBOSS_HOME=/opt/eap \
    ADMIN_USERNAME=admin \
    ADMIN_PASSWORD=admin123 \
    MANAGEMENT_HTTP_PORT=9990 \
    EAPX_ADMIN_PASSWORD=admin123 \
    EAP8_SETUP_MANAGEMENT_USER=true \
    ENABLE_MANAGEMENT_INTERFACE=true

# Expose ports
EXPOSE 8080 9990

# Default startup command for EAP in OpenShift
CMD ["/opt/eap/bin/openshift-launch.sh"]
