package nu.peg.svmeal;

import com.mashape.unirest.http.Unirest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class SvmealApiApplication {
    public static void main(String[] args) {
        CloseableHttpClient client = HttpClients.createSystem();
        Unirest.setHttpClient(client);

        SpringApplication.run(SvmealApiApplication.class, args);
    }
}
