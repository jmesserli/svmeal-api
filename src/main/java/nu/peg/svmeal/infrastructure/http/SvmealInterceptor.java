package nu.peg.svmeal.infrastructure.http;

import java.util.List;
import nu.peg.svmeal.infrastructure.config.AppConfig;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;

/**
 * Marker interface used for injection in {@link AppConfig#restTemplate(ClientHttpRequestFactory,
 * List)}
 */
public interface SvmealInterceptor extends ClientHttpRequestInterceptor {}
