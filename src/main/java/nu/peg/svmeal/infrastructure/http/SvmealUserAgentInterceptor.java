package nu.peg.svmeal.infrastructure.http;

import java.io.IOException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nu.peg.svmeal.infrastructure.config.SvmealProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SvmealUserAgentInterceptor implements SvmealInterceptor {
  private final BuildProperties buildProperties;
  private final SvmealProperties svmealProperties;

  @Getter(lazy = true)
  private final String userAgent = userAgent();

  private String userAgent() {
    final var userAgent =
        "%s/%s (%s)"
            .formatted(
                buildProperties.getArtifact(),
                buildProperties.getVersion(),
                svmealProperties.userAgentLink());

    log.debug("User-Agent: {}", userAgent);

    return userAgent;
  }

  @Override
  public ClientHttpResponse intercept(
      HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution)
      throws IOException {
    HttpHeaders headers = httpRequest.getHeaders();
    headers.set("User-Agent", getUserAgent());

    return clientHttpRequestExecution.execute(httpRequest, bytes);
  }
}
