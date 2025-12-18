package org.janedough.parent.config;

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
        SecurityScheme bearerScheme = new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT").description("JWT Bearer Token");
        SecurityRequirement bearerRequirement = new SecurityRequirement().addList("Bearer Authentication");
        return new OpenAPI().info(new Info().title("Jane Dough API").version("1.0").description("This is the Jane Dough's API endpoints, requests, and responses")
                        .license(new License().name("Apache 2.0").url("http://janedoughbakehause.com")).contact(new Contact().name("Robert Carrera")
                                .email("rob76c@gmail.com").url("https://github.com/rob76c")))
                                    .externalDocs(new ExternalDocumentation().description("Jane Dough API Documentation").url("https://github.com/rob76c"))
                        .components(new Components().addSecuritySchemes("Bearer Authentication", bearerScheme)).addSecurityItem(bearerRequirement);
    }
}
