# ===== STAGE 1: Build WAR using Maven =====
FROM registry.access.redhat.com/ubi8/openjdk-17 as builder

WORKDIR /app

# Copy pom and source
COPY pom.xml .
COPY src ./src

# Download dependencies + build WAR
RUN mvn -B clean package -DskipTests

# ===== STAGE 2: Deploy WAR to JBoss EAP 8 =====
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift

# Copy WAR from previous stage
COPY --from=builder /app/target/noteworthy.war /opt/eap/standalone/deployments/

# Expose app + management ports
EXPOSE 8080 9990

# Enable admin console
ENV JBOSS_HOME=/opt/eap \
    ADMIN_USERNAME=admin \
    ADMIN_PASSWORD=admin123 \
    MANAGEMENT_HTTP_PORT=9990 \
    EAPX_ADMIN_PASSWORD=admin123 \
    EAP8_SETUP_MANAGEMENT_USER=true \
    ENABLE_MANAGEMENT_INTERFACE=true

# Launch script compatible with OpenShift
CMD ["/opt/eap/bin/openshift-launch.sh"]
