package com.alexmegremis.cucumberPOC;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.alexmegremis.cucumberPOC")
public class CucumberPOCConfig implements ApplicationContextAware {

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() throws IOException {

        YamlPropertiesFactoryBean yamlProperties = new YamlPropertiesFactoryBean();
        PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();

        Resource application = new ClassPathResource("application.yml");
        Resource multipleDB = new ClassPathResource("persistence-multiple-db-boot.yml");

        yamlProperties.setResources(application, multipleDB);
        properties.setProperties(yamlProperties.getObject());
        properties.setTrimValues(true);

        return properties;
    }
}
