package nu.peg.svmeal.config;

import com.google.gson.Gson;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import nu.peg.svmeal.http.SvmealInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean
  public Gson gson() {
    return new Gson();
  }

  @Bean
  public RestTemplate restTemplate(
      ClientHttpRequestFactory requestFactory, List<SvmealInterceptor> interceptors) {
    return new RestTemplateBuilder()
        .requestFactory(() -> requestFactory)
        .additionalInterceptors(interceptors)
        .build();
  }

  @Bean
  public ClientHttpRequestFactory requestFactory(Environment environment) {
    SimpleClientHttpRequestFactory simpleFactory = new SimpleClientHttpRequestFactory();

    simpleFactory.setConnectTimeout(2000);
    simpleFactory.setReadTimeout(5000);

    if (environment.acceptsProfiles(ProfileRegistry.LOCAL_PROXY_9999)) {
      simpleFactory.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", 9999)));
    }

    return simpleFactory;
  }
}
