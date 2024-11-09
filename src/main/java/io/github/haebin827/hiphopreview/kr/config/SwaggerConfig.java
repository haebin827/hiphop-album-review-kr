/*
package io.github.haebin827.hiphopreview.kr.config;

import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("spring-library-api")
                .pathsToMatch("/**")
                .packagesToScan("edu.library.libraryspringboot.controller")
                .build();
    }

    @Bean
    public Info apiInfo() {
        return new Info()
                .title("CS Library API")
                .description("This is the API documentation for the CS Library project.")
                .version("1.0.0")
                .contact(new Contact().name("Haebin Noh").email("haebin.noh@gmail.com"));
    }

    */
/*@Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.haebin827.hiphopreview.kr.controller"))

                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Hiphop Album Review - KR")
                .description("This is the API documentation for the Hiphop Album Review project.")
                .version("1.0.0")
                .contact(new Contact("Haebin Noh", "https://github.com/haebin827", "haebin.noh@gmail.com"))
                .build();
    }*//*

}*/
