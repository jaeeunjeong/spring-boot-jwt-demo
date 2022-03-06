package me.toyproject.loginjwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class LoginJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginJwtApplication.class, args);
	}

}
