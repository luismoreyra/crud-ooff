package pe.com.interbank.digitalchannel.ffoo.persistence.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
@Configuration
public class PersistenceConfig {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceConfig.class);

    @Value("${ffoo.application.dataSource.digitalChannel.jndi-name}")
    private String jndiDataSource;

    @Bean
    public DataSource dataSource() {
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setExpectedType(DataSource.class);
        jndi.setJndiName(jndiDataSource);

        try {
            jndi.afterPropertiesSet();
        } catch (NamingException e) {
            logger.error("Error creating data source with name " + jndiDataSource, e);
        }
        return (DataSource) jndi.getObject();
    }

    @Bean
    public JdbcTemplate jdbcTemplate() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource());
        return jdbcTemplate;
    }

}
