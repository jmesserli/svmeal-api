package nu.peg.svmeal.http;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class SvmealUserAgentInterceptor implements SvmealInterceptor {
  private final String userAgent;

  @Autowired
  public SvmealUserAgentInterceptor(@Value("${svmeal.user-agent}") String userAgent) {
    this.userAgent = userAgent;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution)
      throws IOException {
    HttpHeaders headers = httpRequest.getHeaders();
    headers.set("User-Agent", userAgent);

    return clientHttpRequestExecution.execute(httpRequest, bytes);
  }
}
