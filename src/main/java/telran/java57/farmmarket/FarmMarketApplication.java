package telran.java57.farmmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import telran.java57.farmmarket.model.CookieProps;

@EnableConfigurationProperties(CookieProps.class)
@SpringBootApplication
public class FarmMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmMarketApplication.class, args);
    }

}
