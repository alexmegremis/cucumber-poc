package com.alexmegremis.cucumberPOC;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.alexmegremis.cucumberPOC.persistence.batch"},
        entityManagerFactoryRef = "batchEntityManager",
        transactionManagerRef = "batchTransactionManager")
public class PersistenceConfigBatch {


    @Bean
    public DataSource batchDataSource() {
        return batchDataSourceProperties().initializeDataSourceBuilder().build();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean batchEntityManager() {
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(batchDataSource());
        result.setPackagesToScan("com.alexmegremis.cucumberPOC.persistence.batch");

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        result.setJpaVendorAdapter(jpaVendorAdapter);

        Properties jpaProperties = batchJPAProperties();
        result.setJpaProperties(jpaProperties);

        return result;
    }


    @Bean
    public PlatformTransactionManager batchTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(batchEntityManager().getObject());
        return transactionManager;
    }


    @Bean
    @ConfigurationProperties("spring.batchdatasource")
    public DataSourceProperties batchDataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        return dataSourceProperties;
    }

    @Bean
    @ConfigurationProperties("spring.batchdatasource.properties")
    public Properties batchJPAProperties() {
        Properties result = new Properties();
        return result;
    }
}
