package com.kims.coreevent.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EntityScan(basePackages = "com.kims.coreevent.entity")
@EnableJpaRepositories(
        basePackages = "com.kims.coreevent.repository",
        entityManagerFactoryRef = "eventEntityManagerFactory",
        transactionManagerRef = "eventTransactionManager"
)
@EnableConfigurationProperties({EventJPAProperties.class, EventDatasourceProperties.class})
public class EventDatasourceAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(EventDatasourceAutoConfiguration.class);

    private final EventDatasourceProperties eventDatasourceProperties;
    private final EventJPAProperties eventJPAProperties;

    public EventDatasourceAutoConfiguration(EventDatasourceProperties eventDatasourceProperties, EventJPAProperties eventJPAProperties) {
        this.eventDatasourceProperties = eventDatasourceProperties;
        this.eventJPAProperties = eventJPAProperties;
        log.debug("Event Datasource Properties: {}", eventDatasourceProperties);
        log.debug("Event JPA Properties: {}", eventJPAProperties);
    }

    @Bean
    public DataSource eventDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(eventDatasourceProperties.getUrl());
        dataSource.setUsername(eventDatasourceProperties.getUsername());
        dataSource.setPassword(eventDatasourceProperties.getPassword());
        dataSource.setDriverClassName(eventDatasourceProperties.getDriverClassName());
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean eventEntityManagerFactory(DataSource eventDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(eventDataSource);
        em.setPackagesToScan("com.ontwo.coreevent.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", eventJPAProperties.getDdlAuto());
        props.put("hibernate.dialect", eventJPAProperties.getDatabasePlatform());
        props.put("hibernate.show_sql", eventJPAProperties.isShowSql());
        props.put("hibernate.format_sql", eventJPAProperties.isFormatSql());

        em.setJpaPropertyMap(props);

        return em;
    }

    @Bean
    public PlatformTransactionManager eventTransactionManager(@Qualifier("eventEntityManagerFactory") EntityManagerFactory eventEntityManagerFactory) {
        return new JpaTransactionManager(eventEntityManagerFactory);
    }
}
