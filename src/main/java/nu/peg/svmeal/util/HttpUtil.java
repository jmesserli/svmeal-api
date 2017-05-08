package nu.peg.svmeal.util;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URI;

public final class HttpUtil {

    public static String followRedirectsAndGetUrl(String url) {
        CloseableHttpClient httpClient = HttpClients.createSystem();
        HttpGet httpGet = new HttpGet(url);
        HttpContext context = new BasicHttpContext();
        try {
            httpClient.execute(httpGet, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        URI finalUri = httpGet.getURI();
        RedirectLocations locations = (RedirectLocations) context.getAttribute(DefaultRedirectStrategy.REDIRECT_LOCATIONS);
        if (locations != null) {
            finalUri = locations.get(locations.size() - 1);
        }

        return finalUri.toString();
    }

}
