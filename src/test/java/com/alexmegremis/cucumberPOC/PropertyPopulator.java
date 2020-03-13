package com.alexmegremis.cucumberPOC;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Field;
import java.util.Arrays;

@Slf4j
@Component
public class PropertyPopulator implements ApplicationContextAware {

    @Getter
    @Setter
    private ApplicationContext applicationContext;

    public final void populateProperties(final Object target) {
        Arrays.stream(target.getClass().getDeclaredFields()).filter(field -> field.getAnnotationsByType(Value.class).length > 0).forEach(c -> {
            populateProperty(target, c);
        });
    }

    public void populateProperty(final Object target, final Field field) {
        Value  valueAnnotation = field.getAnnotation(Value.class);
        String propertyKey     = valueAnnotation.value().substring(2, valueAnnotation.value().length() - 1);
        populateProperty(target, field, propertyKey);
    }

    private void populateProperty(final Object target, final Field field, final String propertyKey) {
        ReflectionTestUtils.setField(target, field.getName(), getApplicationContext().getEnvironment().getProperty(propertyKey, field.getType()),
                                     field.getType());
        log.info(">>> SET {} TO \"{}\" AS TYPE {}", field.getName(), getApplicationContext().getEnvironment().getProperty(propertyKey),
                 field.getType().getSimpleName());
    }
}
