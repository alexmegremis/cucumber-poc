package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

public interface BuilderSettersHelper {

    Logger log = LoggerFactory.getLogger(BuilderSettersHelper.class);

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    static <T> void setDateIfNotNull(final String dateAsString, Function<Date, T> setter) throws ParseException {
        if (StringUtils.isNotBlank(dateAsString)) {
            log.debug(">>> BDD: Building POJO with {}", dateAsString);
            setter.apply(formatter.parse(dateAsString));
        }
    }

    static <T> void setStringIfNotNull(final String string, Function<String, T> setter) {
        if (StringUtils.isNotBlank(string)) {
            log.debug(">>> BDD: Building POJO with {}", string);
            setter.apply(string);
        }
    }

    static <T> void setIntegerIfNotNull(final String integerAsString, Function<Integer, T> setter) {
        if (StringUtils.isNotBlank(integerAsString)) {
            setIntegerIfNotNull(Integer.valueOf(integerAsString), setter);
        }
    }
    static <T> void setIntegerIfNotNull(final Integer integer, Function<Integer, T> setter) {
        if (integer != null) {
            log.debug(">>> BDD: Building POJO with {}", integer);
            setter.apply(integer);
        }
    }
}
