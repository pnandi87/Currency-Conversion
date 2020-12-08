FROM java:8

EXPOSE 9411

ADD target/zipkin-server.jar zipkin-server.jar 

ENTRYPOINT [ "java", "-jar", "zipkin-server.jar" ]