package nu.peg.svmeal;

import nu.peg.svmeal.infrastructure.config.CacheProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(value = {CacheProperties.class})
public class SvmealApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(SvmealApiApplication.class, args);
  }
}
