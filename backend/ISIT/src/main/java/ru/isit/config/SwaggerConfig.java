package ru.isit.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ISIT api")
                        .version("v1")
                        .description("Документация для backend API платформы ISIT"))
                .servers(List.of(
                        new Server().url("https://isit-esport.ru").description("Production сервер"),
                        new Server().url("http://localhost:8080").description("Local Dev сервер")
                ));
    }
}
