
FROM registry.redhat.io/jboss-eap-8/eap8-openjdk17-runtime-openshift
WORKDIR /opt/eap/standalone/deployments/
COPY target/noteworthy.war .
CMD ["/opt/eap/bin/standalone.sh", "-b", "0.0.0.0"]
