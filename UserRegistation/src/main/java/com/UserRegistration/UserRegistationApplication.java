package com.UserRegistration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
@OpenAPIDefinition(info = @Info(title = "User", version = "2.0", description = "User Module"))
@SecurityScheme(name = "javainuseapi", scheme = "Bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@SpringBootApplication

public class UserRegistationApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRegistationApplication.class, args);
		System.out.println("Run project !");
	}

}
