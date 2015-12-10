package com.pureblue.quant.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigPropValue {
    public static String getPropValue(String propertyKey) throws IOException {
        String valueInString = "";
        InputStream inputStream = null;

        Properties prop = new Properties();
        String propFileName = "config.properties";
        inputStream = ConfigPropValue.class.getClassLoader().getResourceAsStream(propFileName);
        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName
                    + "' not found in the classpath");
        }
        // get the property value and print it out
        valueInString = prop.getProperty(propertyKey);
        inputStream.close();
        return valueInString;
    }
}
