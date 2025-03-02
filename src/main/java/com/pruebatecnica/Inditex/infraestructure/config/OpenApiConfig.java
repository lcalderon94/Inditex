package com.pruebatecnica.Inditex.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Precios - Prueba Técnica Inditex")
                        .description("API para consulta y gestión de precios aplicables a productos según fecha, marca y prioridad")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Luis Calderón")
                                .email("luis_1994_2010@hotmail.com"))
                        .license(new License()
                                .name("Licencia API")
                                .url("https://ejemplo.com/licencia")));
    }
}