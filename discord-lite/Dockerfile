# Giai đoạn 1: Build bằng Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn 2: Chạy bằng JRE nhẹ
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Nhặt thành phẩm từ Stage 1
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]