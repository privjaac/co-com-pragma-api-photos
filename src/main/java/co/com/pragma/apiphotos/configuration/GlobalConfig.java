package co.com.pragma.apiphotos.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.server.WebFilter;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Objects;

@EnableOpenApi
@EnableAsync(proxyTargetClass = true)
@Configuration
public class GlobalConfig {
   @Value(value = "${spring.webflux.base-path}")
   private String basePath;

   @Bean
   WebFilter webFilter() {
      return (exchange, chain) -> {
         var request = exchange.getRequest();
         var response = exchange.getResponse();
         var headers = response.getHeaders();
         if (Objects.equals(request.getMethod(), HttpMethod.OPTIONS)) {
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
            headers.set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "*");
         }
         return chain.filter(exchange.mutate().request(request.mutate().contextPath(basePath).build()).build());
      };
   }

   @Bean
   public Docket api() {
      return new Docket(DocumentationType.OAS_30).pathMapping(basePath);
   }
}
