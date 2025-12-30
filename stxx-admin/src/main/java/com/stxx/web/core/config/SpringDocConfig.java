package com.stxx.web.core.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {
    @Bean
    public OpenAPI myOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("系统开发API")
                        .description("程序员的API对接文档")
                        .version("v1.0.0")
                        .license(new License()
                                .name("许可协议")
                                .url("https://github.com/benxiaohai061/stxx-ry"))
                        .contact(new Contact()
                                .name("王川川")
                                .email("wangccwork@163.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("源码仓库")
                        .url("https://github.com/benxiaohai061/stxx-ry"))
                .components(new Components()
                        .addSecuritySchemes("Authorization",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                )
                // 🔐 全局生效
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    /**
     * Knife4j 全局 Header 参数补充
     * 让 Knife4j 的全局参数列表显示 Authorization
     */
    @Bean
    public OpenApiCustomiser knife4jGlobalHeader() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation ->
                        operation.addParametersItem(
                                new io.swagger.v3.oas.models.parameters.Parameter()
                                        .in("header")
                                        .name("Authorization")
                                        .description("Bearer token-xxxx")
                                        .required(false)
                        )
                )
        );
    }
 }
