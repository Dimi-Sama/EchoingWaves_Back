package com.shiki.echo_waves.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        // Configuration de base avec informations sur l'API
        return new OpenAPI()
        
                
                            
                .info(new Info()
                        .title("API de Echo Waves")
                        .version("0.0.1")
                        .description("Documentation de l'API"));
    }
  
}
