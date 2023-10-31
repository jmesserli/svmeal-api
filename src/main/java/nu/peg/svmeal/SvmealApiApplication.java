package nu.peg.svmeal;

import nu.peg.svmeal.infrastructure.config.CacheProperties;
import nu.peg.svmeal.infrastructure.config.SvmealProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableConfigurationProperties(value = {CacheProperties.class, SvmealProperties.class})
public class SvmealApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(SvmealApiApplication.class, args);
  }
}
