package com.example.lmssystem.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Clinic CRM system API",
                description = "API for Clinics CRM system",
                version = "1.0",
                contact = @Contact(
                        name = "Olmos Soft",
                        email = "olomossoft@gamil.com",
                        url = "https://olmos.soft.uz/api"
                )
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/api"
                ),
                @Server(
                        description = "Global ENV",
                        url = "http://127.0.0.1:8080/api"
                ),
//                @Server(
//                        description = "Prod ENV",
//                        url = "https://crm-app.uz/api"
//                ),
                @Server(
                        description = "Dev ENV",
                        url = "https://crm-app.uz/dev-api"
                )
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT scientification",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
