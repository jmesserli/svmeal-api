package nu.peg.svmeal.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Sets Content-Type to application/json for SV search responses
 *
 * @author Joel Messerli
 */
@Component
public class ContentTypeFixingInterceptor implements SvmealInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        boolean shouldFixContentType = request.getURI().getHost().equals("www.sv-restaurant.ch");

        ClientHttpResponse response = clientHttpRequestExecution.execute(request, body);
        if (shouldFixContentType) {
            HttpHeaders headers = response.getHeaders();
            headers.set("Content-Type", "application/json");
        }

        return response;
    }
}
