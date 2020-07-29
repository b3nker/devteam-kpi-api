FROM eu.gcr.io/neo9-software-factory/n9-images/jdk:11.0.7 as builder

ENV PROFILE test
CMD java -Dspring.profiles.active=dev,local -jar /home/app/target/*.jar

FROM eu.gcr.io/neo9-software-factory/n9-images/jdk:11.0.7-runtime

COPY --from=builder --chown=webadmin:webadmin /home/app/target/*.jar /home/app/

CMD java \
    -XX:MaxRAM=$(( $(cat /sys/fs/cgroup/memory/memory.limit_in_bytes) / 100 * 70 )) \
    -Dspring.profiles.active=${PROFILE} \
    -jar /home/app/*.jar
