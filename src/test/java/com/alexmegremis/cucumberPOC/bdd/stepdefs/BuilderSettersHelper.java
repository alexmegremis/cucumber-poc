package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.function.Function;

public interface BuilderSettersHelper {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    static <T> void setDateIfNotNull(final String dateAsString, Function<Date, T> setter) throws ParseException {
        if (StringUtils.isNotBlank(dateAsString)) {
            System.out.println(">>> Adding " + setter.toString());
            setter.apply(formatter.parse(dateAsString));
        }
    }

    static <T> void setStringIfNotNull(final String string, Function<String, T> setter) {
        if (StringUtils.isNotBlank(string)) {
            System.out.println(">>> Adding " + setter.toString());
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
            System.out.println(">>> Adding " + setter.toString());
            setter.apply(integer);
        }
    }
}
