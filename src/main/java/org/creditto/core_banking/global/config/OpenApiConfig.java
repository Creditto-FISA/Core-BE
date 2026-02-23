package org.creditto.core_banking.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI coreBankingOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Creditto Core Banking API")
                        .description("코어뱅킹 도메인 API 문서")
                        .version("v1")
                        .contact(new Contact()
                                .name("Creditto Backend Team")))
                .servers(List.of(
                        new Server().url("/").description("Default Server")
                ));
    }
}
