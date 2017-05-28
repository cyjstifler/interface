package com.superhard.lbsw.utils;

import java.io.IOException;  
import java.io.InputStream;  
import java.util.Properties;  
  
public class GetProperty {  
  
    public static String getPropertyByName(String path, String name) {  
        String result = "";  
  
        try {  
            result = java.util.ResourceBundle.getBundle(path).getString(name);  
            System.out.println("name:" + result);  
        } catch (Exception e) {  
            System.out.println("getPropertyByName2 error:" + name);  
        }  
        return result;  
    }  
  
    public static String getPropertyByName2(String path, String name) {  
        String result = "";  
  
        InputStream in = GetProperty.class.getClassLoader().getResourceAsStream(path);  
  
        Properties prop = new Properties();  
        try {  
            prop.load(in);  
            result = prop.getProperty(name).trim();  
            System.out.println("name:" + result);  
        } catch (IOException e) {  
        	//TODO
            System.out.println(" ");  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
}  