package com.lalit.e_commerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce API")
                        .version("1.0.0")
                        .description(
                                "E-Commerce backend built using " +
                                "Spring Boot, Spring Data JPA, " +
                                "Spring Security, JWT and MySQL.")
                        .contact(new Contact()
                                .name("Radha")
                                .email("radha@gmail.com")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                               new SecurityScheme()
                                                       .type(SecurityScheme.Type.HTTP)
                                                       .scheme("bearer")
                                                       .bearerFormat("JWT")
                                                       .description("Enter JWT token")
                        )
                );
    }
}
