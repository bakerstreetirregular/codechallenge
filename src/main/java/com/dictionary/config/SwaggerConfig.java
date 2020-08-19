package com.dictionary.config;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurationSupport {
	@Bean
	public Docket productApi(ServletContext servletContext) {
		return new Docket(DocumentationType.SWAGGER_2)/*
														 * .pathProvider(new RelativePathProvider(servletContext) {
														 * 
														 * @Override public String getApplicationBasePath() { return
														 * "/dictionary-service"; } })
														 */.select().apis(RequestHandlerSelectors.basePackage("com.dictionary")).paths(regex("/dictionary.*"))
				.build().apiInfo(apiInfo());

	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Dictionary Service").description("CRUD operations on a dictionary").build();		
	}
	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
