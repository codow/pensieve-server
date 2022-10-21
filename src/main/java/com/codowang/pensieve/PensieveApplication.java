package com.codowang.pensieve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 启动程序
 * @author wyb
 */
@EnableOpenApi
@SpringBootApplication
@MapperScan("com.codowang.pensieve.mapper")
public class PensieveApplication {

	public static void main(String[] args) {
		SpringApplication.run(PensieveApplication.class, args);
	}

}
