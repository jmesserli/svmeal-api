package nu.peg.svmeal.config;

import java.util.List;
import nu.peg.svmeal.http.SvmealInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate(List<SvmealInterceptor> interceptors) {
    return new RestTemplateBuilder().additionalInterceptors(interceptors).build();
  }
}
