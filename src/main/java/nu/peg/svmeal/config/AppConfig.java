package nu.peg.svmeal.config;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * TODO Short summary
 *
 * @author Joel Messerli @26.02.2017
 */
@Configuration
public class AppConfig {
    private static Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @PreDestroy
    public void beforeShutdown() {
        try {
            Unirest.shutdown();
            LOGGER.info("Unirest shut down");
        } catch (IOException e) {
            LOGGER.warn("Could not shut down Unirest", e);
        }
    }
}
