FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl

WORKDIR /app

# Игнорируем .original файлы, берем только fat-jar
COPY target/*SNAPSHOT.jar app.jar

# Проверка, что JAR не пустой
RUN test -s app.jar || (echo "ERROR: app.jar is empty!" && exit 1)

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]