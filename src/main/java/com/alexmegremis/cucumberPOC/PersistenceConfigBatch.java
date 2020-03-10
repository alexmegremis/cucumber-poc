package com.alexmegremis.cucumberPOC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
//@EnableAutoConfiguration
@PropertySource({"classpath:persistence-multiple-db-boot.properties"})
@EnableJpaRepositories(
        basePackages = {"com.alexmegremis.cucumberPOC.persistence.batch"},
        entityManagerFactoryRef = "batchEntityManager",
        transactionManagerRef = "batchTransactionManager")
public class PersistenceConfigBatch {


    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix="batch.datasource")
    public DataSource batchDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean
    public LocalContainerEntityManagerFactoryBean batchEntityManager() {
        final LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();

        result.setDataSource(batchDataSource());
        result.setPackagesToScan("com.alexmegremis.cucumberPOC.persistence.batch");

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        result.setJpaVendorAdapter(jpaVendorAdapter);

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        result.setJpaPropertyMap(properties);

        return result;
    }

    @Bean
    public PlatformTransactionManager batchTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(batchEntityManager().getObject());
        return transactionManager;
    }
//
//
//    @Bean
//    @ConfigurationProperties("batch.datasource")
//    public DataSourceProperties batchDataSourceProperties() {
//        DataSourceProperties dataSourceProperties = new DataSourceProperties();
//        return dataSourceProperties;
//    }
//
//    @Bean
//    @ConfigurationProperties("batch.datasource.properties")
//    public Properties batchJPAProperties() {
//        Properties result = new Properties();
//        return result;
//    }
}
