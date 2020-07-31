FROM eu.gcr.io/neo9-software-factory/n9-images/jdk:11.0.7 as builder

ENV PROFILE test
CMD java -Dspring.profiles.active=dev,local -jar /home/app/target/*.jar

RUN java -Djarmode=layertools -jar target/*.jar list
RUN java -Djarmode=layertools -jar target/*.jar extract


FROM eu.gcr.io/neo9-software-factory/n9-images/jdk:11.0.7-runtime

COPY --from=builder --chown=webadmin:webadmin /home/app/dependencies/ /home/app/
COPY --from=builder --chown=webadmin:webadmin /home/app/spring-boot-loader/ /home/app/
COPY --from=builder --chown=webadmin:webadmin /home/app/snapshot-dependencies/ /home/app/
COPY --from=builder --chown=webadmin:webadmin /home/app/application/ /home/app/

CMD java \
    -XX:MaxRAM=$(( $(cat /sys/fs/cgroup/memory/memory.limit_in_bytes) / 100 * 70 )) \
    -Dspring.profiles.active=${PROFILE} \
    org.springframework.boot.loader.JarLauncher
