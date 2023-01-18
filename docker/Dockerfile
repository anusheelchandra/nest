FROM gcd/gcd-jdk19-maven:0.1.0

ARG ARTIFACT_VERSION=0.0.1-SNAPSHOT

RUN set -x \
    && mkdir -p /tmp \
    && chmod 777 /tmp \
    && /opt/maven/apache-maven-3.6.3/bin/mvn dependency:copy -Dartifact=com.nest:nest:$ARTIFACT_VERSION:jar -DoutputDirectory=/ \
    && cp /nest-*.jar /nest.jar


RUN set -x \
    && mkdir -p /tmp \
    && chmod 777 /tmp


COPY bootstrap.sh /

RUN set -x \
    && chmod +x /bootstrap.sh


CMD /bootstrap.sh



