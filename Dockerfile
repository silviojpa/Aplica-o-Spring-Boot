# Estágio 1: Define a imagem base com Java 17
FROM openjdk:17-slim

# Estágio 2: Argumento para o caminho do JAR, facilitando a manutenção
ARG JAR_FILE=target/meu-app-devops-0.0.1-SNAPSHOT.jar

# Estágio 3: Copia o JAR para dentro do contêiner com um nome genérico
COPY ${JAR_FILE} app.jar

# Estágio 4: Expõe a porta padrão da aplicação
EXPOSE 8080

# Estágio 5: Comando para executar a aplicação quando o contêiner iniciar
ENTRYPOINT ["java", "-jar", "/app.jar"]
