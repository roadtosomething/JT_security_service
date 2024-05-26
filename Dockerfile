# Используем официальный образ Java как базовый
FROM openjdk:21-ea-1-jdk-oracle

# Указываем точку монтирования для внешних данных внутри контейнера
WORKDIR /jtapp

ARG JAR_FILE=build/libs/security_service-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} /jtapp/jtsecurityapp.jar

# Команда запуска JAR-файла
ENTRYPOINT ["java","-jar","/jtapp/jtsecurityapp.jar"]