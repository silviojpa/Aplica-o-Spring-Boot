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
