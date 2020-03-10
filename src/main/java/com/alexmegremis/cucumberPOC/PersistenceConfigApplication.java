package com.alexmegremis.cucumberPOC;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
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
        basePackages = {"com.alexmegremis.cucumberPOC.persistence.application"},
        entityManagerFactoryRef = "applicationEntityManager",
        transactionManagerRef = "applicationTransactionManager")
public class PersistenceConfigApplication {

    @Bean
    @Primary
    public DataSource applicationDataSource() {
        return applicationDataSourceProperties().initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean applicationEntityManager() {
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();
        result.setDataSource(applicationDataSource());
        result.setPackagesToScan("com.alexmegremis.cucumberPOC.persistence.application");

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        result.setJpaVendorAdapter(jpaVendorAdapter);

        Properties jpaProperties = applicationJPAProperties();
        result.setJpaProperties(jpaProperties);

        return result;
    }

    @Bean
    @Primary
    public PlatformTransactionManager applicationTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(applicationEntityManager().getObject());
        return transactionManager;
    }

    @Bean
    @Primary
    @ConfigurationProperties("application.datasource")
    public DataSourceProperties applicationDataSourceProperties() {
        DataSourceProperties dataSourceProperties = new DataSourceProperties();
        return dataSourceProperties;
    }

    @Bean
    @Primary
    @ConfigurationProperties("application.datasource.properties")
    public Properties applicationJPAProperties() {
        Properties result = new Properties();
        return result;
    }
}
