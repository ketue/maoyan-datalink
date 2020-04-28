package com.maoyan.bigdata.datalink.config;


import springfox.documentation.service.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 描述
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.maoyan.bigdata.datalink"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("DataLink接口文档")
                .description("DataLink接口描述")
                //服务条款网址
                .termsOfServiceUrl("https://wiki.maoyan.com/pages/viewpage.action?pageId=42626219")
                .version("1.0")
                .contact(new Contact("DataLink", "https://wiki.maoyan.com/pages/viewpage.action?pageId=42626219", "zhaobin04@maoyan.com"))
                .build();
    }

}
