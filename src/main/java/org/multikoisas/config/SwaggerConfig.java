package org.multikoisas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Sistema Multikoisas",
                description = "Documentação dos endpoints do projeto integrador",
                version = "v1"
        ),
        // Aplica a exigência do token globalmente em todos os endpoints mapeados no Swagger
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth", // Nome identificador do esquema
        type = SecuritySchemeType.HTTP, // Define que a segurança trafega no protocolo HTTP
        scheme = "bearer", // Informa ao Swagger para concatenar a palavra "Bearer " automaticamente
        bearerFormat = "JWT" // Metadado explicativo sobre a natureza do token
)
public class SwaggerConfig {
}
