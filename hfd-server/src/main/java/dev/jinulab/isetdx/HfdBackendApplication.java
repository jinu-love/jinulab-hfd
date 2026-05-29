package dev.jinulab.isetdx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = { "dev.jinulab" })
@EnableAspectJAutoProxy
//@EnableCaching
public class HfdBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HfdBackendApplication.class, args);
	}

}
