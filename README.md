# Aplica-o-Spring-Boot
Projeto Prático: O Ciclo de Vida DevOps de uma Aplicação Spring Boot

# Projeto Prático: O Ciclo de Vida DevOps de uma Aplicação Spring Boot

Este repositório contém um projeto de estudo prático focado em demonstrar o ciclo de vida de uma aplicação Java com Spring Boot sob a perspectiva de DevOps. O objetivo é passar por todas as etapas essenciais: criação, build, empacotamento, containerização, configuração e monitoramento.

## 🎯 Objetivo

Passar por um fluxo de trabalho de DevOps, pegando uma aplicação simples do código-fonte até um contêiner Docker em execução, configurável e monitorável.

## 🛠️ Pré-requisitos

Antes de começar, garanta que você tenha as seguintes ferramentas instaladas e configuradas em seu ambiente:

* **Java Development Kit (JDK)** - Versão 17 ou superior.
* **Apache Maven** - Para gerenciamento de dependências e build do projeto.
* **Docker** - Docker Desktop (Windows/Mac) ou Docker Engine (Linux) para criar e gerenciar contêineres.
* **Um Editor de Código** - [VS Code](https://code.visualstudio.com/) ou sua IDE preferida.
* **Um Terminal / Prompt de Comando**.

---

## 🚀 Roteiro Passo a Passo

### Passo 1: Crie um "Hello World" com o Spring Initializr

Nesta etapa, vamos gerar o esqueleto do nosso projeto e adicionar um endpoint simples para termos algo para testar.

1.  **Acesse o [Spring Initializr](https://start.spring.io)** e preencha as informações do projeto:
    * **Project**: `Maven`
    * **Language**: `Java`
    * **Spring Boot**: Deixe a versão padrão (ex: 3.3.x).
    * **Group**: `br.com.devops.study`
    * **Artifact**: `meu-app-devops`
    * **Packaging**: `Jar`
    * **Java**: `17`
2.  Adicione a dependência **"Spring Web"**.
3.  Clique em **"GENERATE"**, baixe o `.zip`, descompacte-o e abra a pasta no seu editor.
4.  Abra o arquivo `src/main/java/br/com/devops/study/meuappdevops/MeuAppDevopsApplication.java` e adicione a classe `HelloWorldController` para criar um endpoint:

    ```java
    package br.com.devops.study.meuappdevops;

    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.RestController;

    @SpringBootApplication
    public class MeuAppDevopsApplication {

        public static void main(String[] args) {
            SpringApplication.run(MeuAppDevopsApplication.class, args);
        }

    }

    @RestController
    class HelloWorldController {

        @GetMapping("/hello")
        public String sayHello() {
            return "Olá, Mundo! Aplicação rodando com sucesso!";
        }
    }
    ```
5.  Abra um terminal na raiz do projeto e execute a aplicação para testar:
    ```bash
    # No Windows (use Git Bash ou PowerShell)
    ./mvnw spring-boot:run

    # No Linux/Mac
    ./mvnw spring-boot:run
    ```
6.  Verifique no seu navegador acessando `http://localhost:8080/hello`. Depois, pare a aplicação no terminal com `Ctrl + C`.

### Passo 2: Construa o Artefato (`.jar`)

Agora, vamos empacotar nossa aplicação em um "Fat JAR" autônomo.

1.  No terminal, na raiz do projeto, execute o comando de build do Maven:
    ```bash
    ./mvnw clean package
    ```
Obs. Caso tenha uma falha no comando "./mvnw clean package", execute com o parametro (./mvnw clean package -DskipTests).
<img width="1103" height="586" alt="image" src="https://github.com/user-attachments/assets/14bb14fd-b46e-42eb-9a98-06f289a6880a" />
    ``` 
    ./mvnw clean package -DskipTests
    ```
-Vai funcionar com build success.

2.  Após o sucesso, você encontrará o arquivo `meu-app-devops-0.0.1-SNAPSHOT.jar` dentro da pasta `target/`. Este é o nosso artefato!

### Passo 3: Execute o JAR Diretamente

Vamos executar o artefato sem o Maven, como faríamos em um servidor.

1.  Execute o JAR com o Java:
    ```bash
    java -jar target/meu-app-devops-0.0.1-SNAPSHOT.jar
    ```
2.  Verifique novamente em `http://localhost:8080/hello` e depois pare a aplicação com `Ctrl + C`.

### Passo 4: Crie uma Imagem Docker

É hora de containerizar nossa aplicação para torná-la portátil e isolada.

1.  Na **raiz do projeto**, crie um novo arquivo chamado `Dockerfile` (sem extensão).
2.  Adicione o seguinte conteúdo a ele:

    ```dockerfile
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
    ```

3.  Construa a imagem Docker. O `-t` define um nome (tag) e o `.` indica que o contexto é o diretório atual.
    ```bash
    docker build -t meu-app-devops:v1 .
    ```
4.  Execute o contêiner a partir da imagem criada, mapeando a porta da sua máquina para a porta do contêiner (`-p host:container`):
    ```bash
    docker run -p 8080:8080 meu-app-devops:v1
    ```
5.  Verifique em `http://localhost:8080/hello`. A aplicação agora roda de dentro de um contêiner!

> Para parar o contêiner, abra outro terminal, ache o ID com `docker ps` e rode `docker stop <ID_DO_CONTAINER>`.

### Passo 5: Brinque com Configurações Externalizadas

Vamos alterar o comportamento da aplicação (a porta do servidor) sem reconstruir a imagem, uma prática fundamental em DevOps.

1.  Execute o contêiner novamente, mas desta vez passando uma **variável de ambiente** (`-e`) para sobrescrever a configuração padrão:
    ```bash
    # A variável SERVER_PORT é automaticamente reconhecida pelo Spring Boot
    docker run -p 9090:9090 -e SERVER_PORT=9090 meu-app-devops:v1
    ```
2.  Verifique que a aplicação agora está respondendo na nova porta: `http://localhost:9090/hello`.

### Passo 6: Adicione o Actuator para Monitoramento

Por fim, vamos adicionar capacidades de monitoramento à nossa aplicação, simulando um novo ciclo de release.

1.  Abra o arquivo `pom.xml` e adicione a dependência do **Spring Boot Actuator** dentro da tag `<dependencies>`:
    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    ```
2.  **Reconstrua o projeto**, pois adicionamos uma nova dependência:
    ```bash
    # 1. Crie o novo JAR
    ./mvnw clean package

    # 2. Crie uma nova versão da imagem Docker (boa prática!)
    docker build -t meu-app-devops:v2 .
    ```
3.  Execute o contêiner com a nova imagem `v2`:
    ```bash
    docker run -p 8080:8080 meu-app-devops:v2
    ```
4.  **Verifique os novos endpoints:**
    * Confirme que a aplicação ainda funciona: `http://localhost:8080/hello`
    * Acesse o endpoint de saúde do Actuator: `http://localhost:8080/actuator/health`. Você deve ver `{"status":"UP"}`.

---

## 🎉 Conclusão

Parabéns! Você completou um ciclo DevOps fundamental:

✅ **Desenvolveu** um código.
✅ **Construiu** um artefato (`.jar`).
✅ **Empacotou** a aplicação em um contêiner Docker.
✅ **Implantou** e executou a aplicação de forma isolada.
✅ **Configurou** a aplicação para um ambiente específico usando variáveis de ambiente.
✅ **Adicionou e verificou** capacidades de monitoramento com o Actuator.

Este projeto serve como uma base sólida para entender como aplicações modernas são gerenciadas em pipelines de CI/CD automatizados.
