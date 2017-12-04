package com.pureblue.quant.ConnectionPool;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;


public class PropertiesManager {
    private Properties pro = new Properties();

    private PropertiesManager() {
        try {
            System.out.println(PropertiesManager.class.getClassLoader().getResource("DB.properties"));
            pro.load(PropertiesManager.class.getClassLoader().getResourceAsStream("DB.properties"));
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static PropertiesManager instance;
    
    public static synchronized PropertiesManager getInstance(){   //多线程时注意线程安全
        if(instance == null){
            instance = new PropertiesManager();
        }
        return instance;
    }

    public String getProperty(String key) {
        return pro.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return pro.getProperty(key, defaultValue);
    }

    public Enumeration<?> propertiesNames() {
        return pro.propertyNames();
    }
}