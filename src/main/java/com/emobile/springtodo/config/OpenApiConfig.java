package com.emobile.springtodo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Даниил"
                ),
                title = "ToDo Service API",
                description = "OpenApi документация для SpringToDo от Даниила Астафьева",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Локальное окружение"
                )
        }
)
public class OpenApiConfig {
}
