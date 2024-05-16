FROM eclipse-temurin:21-jdk-alpine AS builder
COPY build/libs/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:21-jdk-alpine
COPY --from=builder dependencies/ ./
RUN true
COPY --from=builder snapshot-dependencies/ ./
RUN true
COPY --from=builder spring-boot-loader/ ./
RUN true
COPY --from=builder application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]