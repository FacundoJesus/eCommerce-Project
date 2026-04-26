package com.ecommerce.project.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
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
                .info(new Info()
                        //Elementos para el front de Swagger
                        .title("Spring Boot eCommerce API")
                        .version("4.0")
                        .description("This is a Spring Boot Project for eCommerce")
                        .license(new License().name("Apache 2.0").url("http://facujesus.com"))
                        .contact(new Contact()
                                .name("Facundo Jesús Citera")
                                .email("facundojesus10@hotmail.com")
                                .url("https://github.com/FacundoJesus")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation")
                        .url("https://github.com/FacundoJesus/eCommerce-Project")
                )

                // Componentes para la autenticación
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", bearerScheme))
                        .addSecurityItem(bearerRequirement);
    }

}
