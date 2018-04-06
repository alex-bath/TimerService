FROM java:8

ADD target/timer-service-1-SNAPSHOT.jar /usr/share/TimerService/TimerService.jar

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/TimerService/TimerService.jar"]