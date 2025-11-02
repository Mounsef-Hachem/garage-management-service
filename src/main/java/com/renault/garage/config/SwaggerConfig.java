package com.renault.garage.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI garageServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Renault Garage Management API")
                        .description("Microservice for managing garages, vehicles, and accessories for Renault network")
                        .version("1.0.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
