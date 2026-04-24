package com.ecommerce.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        // Defino esquema de seguridad para token portador que se utilizara en Swagger UI
        // HABILITAR Boton de "Authorize"
        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT Bearer Token");


        SecurityRequirement bearerRequirement = new SecurityRequirement()
                .addList("Bearer Authentication");

        // Defino el Objeto API abierta real para la generación de Swagger UI Y añado componentes.
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", bearerScheme))
                        .addSecurityItem(bearerRequirement);
    }

}
