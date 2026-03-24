package com.Dev.CreateEstimatingPlan.locale;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ExcelLocaleProviderTest {
    @Test
    void canGetMessageLocaleUK() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));
        String result = ExcelLocaleProvider.getMessage("name.component.lecture");

        assertThat(result).isEqualTo("Лекції");
    }


    @Test
    void canGetMessageLocaleEN() {
        ExcelLocaleProvider.setLocale(Locale.ENGLISH);
        String result = ExcelLocaleProvider.getMessage("name.component.lecture");

        assertThat(result).isEqualTo("Lectures");
    }
    @Test
    void shouldGetMessageThrowExceptionForUnknownKey() {
        ExcelLocaleProvider.setLocale(new Locale("uk"));

        assertThrows(java.util.MissingResourceException.class, () -> {
            ExcelLocaleProvider.getMessage("non.existent.key");
        });
    }
}