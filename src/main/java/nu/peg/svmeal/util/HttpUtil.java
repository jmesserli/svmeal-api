package nu.peg.svmeal.util;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.URI;

public final class HttpUtil {

    public static String followRedirectsAndGetUrl(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpContext context = new BasicHttpContext();
        try {
            httpClient.execute(httpGet, context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        URI finalUrl = httpGet.getURI();
        RedirectLocations locations = (RedirectLocations) context.getAttribute(DefaultRedirectStrategy.REDIRECT_LOCATIONS);
        if (locations != null) finalUrl = locations.get(locations.size() - 1);

        return finalUrl.toString();
    }

}
