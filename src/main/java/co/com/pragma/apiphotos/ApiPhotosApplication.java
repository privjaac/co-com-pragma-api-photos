package co.com.pragma.apiphotos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableEurekaClient
@EnableMongoAuditing
@EnableScheduling
@SpringBootApplication
public class ApiPhotosApplication {
   public static void main(String[] args) {
      SpringApplication.run(ApiPhotosApplication.class, args);
   }
}
