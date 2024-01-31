package com.storeshop.scheduleportal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI schedulePortalOpenAPI() {
		return new OpenAPI()
				.info(new Info().title("Staff-Shift-Schedule Portal")
						.description("This is staff-shift schedule web portal for the store shop").version("1.0.0")
						.license(new License().name("Apache 1.0").url("apache.com")))
				.externalDocs(new ExternalDocumentation().description("stff-shift-schedule portal wiki")
						.url("wikipedia.com"));
	}
}