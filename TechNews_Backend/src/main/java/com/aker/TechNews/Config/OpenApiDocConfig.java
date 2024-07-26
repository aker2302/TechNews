package com.aker.TechNews.Config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(contact = @Contact(
                name = "Aker",
                email = "abdellah.kerbal@gmail.com",
                url = "aker2302.github.io"
        ), description = "OpenApi documentation for TechNews backend project"
        ,title = "Back-End specification - Aker",
        version = "1.0")
        , servers = {
                @Server(
                        description = "Local Env",
                        url = "http://localhost:8090/"
                ),
                @Server(
                        description = "Prod Env",
                        url = "${DOC_PROD_URL}"
                )

        }
)
@SecurityScheme(
        type = SecuritySchemeType.APIKEY,
        description = "Api key authentication",
        in = SecuritySchemeIn.HEADER,
        name = "API-Key"
)
public class OpenApiDocConfig {
}
