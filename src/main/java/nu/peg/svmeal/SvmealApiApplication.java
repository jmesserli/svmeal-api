package nu.peg.svmeal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SvmealApiApplication {
  public static void main(String[] args) {
    SpringApplication.run(SvmealApiApplication.class, args);
  }
}
