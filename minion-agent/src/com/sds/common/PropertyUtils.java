package com.sds.common;

import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
    private PropertyUtils(){
        throw new AssertionError("cannot instantiated.");
    }

    static Properties prop = new Properties();
    static {
        try {
            prop.load(PropertyUtils.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
        }
    }
    public static String getProperty(String key){
        return prop.getProperty(key);
    }
}
