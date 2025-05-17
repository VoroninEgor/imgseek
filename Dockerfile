FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем JAR файл
COPY build/libs/*.jar app.jar

# Открываем порт
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"] 