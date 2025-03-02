package com.pruebatecnica.Inditex.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("/api/prices");
        server.setDescription("API DE PRECIOS PARA INDITEX");
        
        return new OpenAPI()
            .servers(Arrays.asList(server))
            .info(new Info()
                .title("API DE PRECIOS")
                .description("API para consulta y gestión de precios aplicables a productos según fecha, marca y prioridad marcados para la prueba tecnica de capitole")
                .version("1.0")
                .contact(new Contact()
                    .name("Luis Calderón")
                    .email("luis_1994_2010@hotmail.com"))
                .license(new License()
                    .name("Licencia API")
                    .url("https://ejemplo.com/licencia")));
    }
}