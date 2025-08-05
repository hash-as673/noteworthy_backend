FROM docker.io/library/maven:3.9.6-eclipse-temurin-17 AS war-build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


# Stage 2: Runtime image with only WAR
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-builder-openshift-rhel8:latest AS builder

# Set up environment variables for provisioning.
ENV GALLEON_PROVISION_FEATURE_PACKS org.jboss.eap:wildfly-ee-galleon-pack,org.jboss.eap.cloud:eap-cloud-galleon-pack
ENV GALLEON_PROVISION_LAYERS cloud-default-config
# Specify the JBoss EAP version
ENV GALLEON_PROVISION_CHANNELS org.jboss.eap.channels:eap-8.0


# Run the assemble script to provision the server.
RUN /usr/local/s2i/assemble



# Stage 3: Runtime image with only WAR
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift-rhel8:latest AS runtime

# Set appropriate ownership and permissions.
COPY --from=builder --chown=jboss:root $JBOSS_HOME $JBOSS_HOME

COPY --from=war-build --chown=jboss:root /app/target/*.war $JBOSS_HOME/standalone/deployments

EXPOSE 8080 9990

RUN chmod -R ug+rwX $JBOSS_HOME