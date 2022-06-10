package pe.com.interbank.digitalchannel.ffoo.web.config;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pe.com.interbank.digitalchannel.ffoo.web.filter.AccessFilter;
import pe.com.interbank.digitalchannel.ffoo.web.filter.LoggingFilter;

/**
 * Created by Robert Espinoza on 28/11/2016.
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        registrationBean.setFilter(characterEncodingFilter);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean accessFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(accessFilter());
        registration.addUrlPatterns("/rest/*");
        registration.setName("accessFilter");
        return registration;
    }

    @Bean(name = "accessFilter")
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    @Bean(name = "loggingFilter")
    public LoggingFilter loggingFilter() {
        return new LoggingFilter();
    }
}
