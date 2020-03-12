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
//@PropertySource({"classpath:persistence-multiple-db-boot.yml"})
@EnableJpaRepositories(
        basePackages = {"com.alexmegremis.cucumberPOC.persistence.application"},
        entityManagerFactoryRef = "applicationEntityManager",
        transactionManagerRef = "applicationTransactionManager")
public class PersistenceConfigApplication {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    @ConfigurationProperties(prefix="application.datasource")
    public DataSource applicationDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean applicationEntityManager() {
        LocalContainerEntityManagerFactoryBean result = new LocalContainerEntityManagerFactoryBean();

        result.setDataSource(applicationDataSource());
        result.setPackagesToScan("com.alexmegremis.cucumberPOC.persistence.application");

        JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
        result.setJpaVendorAdapter(jpaVendorAdapter);

        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        result.setJpaPropertyMap(properties);

        return result;
    }

    @Bean
    @Primary
    public PlatformTransactionManager applicationTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(applicationEntityManager().getObject());
        return transactionManager;
    }
}
