package com.Dev.CreateEstimatingPlan.locale;

import java.util.Locale;
import java.util.ResourceBundle;

public class ExcelLocaleProvider {
    private static final String BUNDLE_NAME = "Messages";
    private static ResourceBundle resourceBundle;

    public static void setLocale(Locale locale) {
        resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
    }

    public static String getMessage(String key) {
        return resourceBundle.getString(key);
    }

    public static void main(String[] args) {
        ExcelLocaleProvider.setLocale(new Locale("uk"));
        System.out.println(ExcelLocaleProvider.getMessage("error.lecture_mismatch2"));
    }
}