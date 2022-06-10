package pe.com.interbank.digitalchannel.ffoo.web.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.WebApplicationInitializer;

/**
 * Created by Robert Espinoza on 28/11/2016.
 */
@Configuration
@ComponentScan(basePackages = "pe.com.interbank.digitalchannel.ffoo")
@EnableAutoConfiguration
public class FFOOApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(FFOOApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(FFOOApplication.class);
    }

}
