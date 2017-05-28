package com.superhard.lbsw.utils;

import java.io.IOException;

import com.google.gson.Gson;


public class JsonUtil {
	private static Gson gson = new Gson();
	public static<T> Object JSONToObj(String jsonStr,Class<T> obj) {
        T t = (T) gson.fromJson(jsonStr, obj);
        return t;
    }
     
   
    public static<T> String objectToJson(T obj) throws IOException  {
        // Convert object to JSON string  
        String jsonStr = "";
        jsonStr =  gson.toJson(obj);
        return jsonStr;
    }
}
