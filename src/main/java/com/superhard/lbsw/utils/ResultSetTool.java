package com.superhard.lbsw.utils;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;




 
public class ResultSetTool {
    /**
     *  
     * @param rs
     * @param dto
     * @return
     * @throws Exception
     */
    public  <T> T bindDataToDTO(ResultSet rs, T dto) throws Exception {  
           
        Method[] methods = dto.getClass().getMethods();  
   
        ResultSetMetaData rsmd = rs.getMetaData();   
        int columnsCount = rsmd.getColumnCount();   
        String[] columnNames = new String[columnsCount];   
        String[] originalColumnNames = new String[columnsCount];  
        for (int i = 0; i < columnsCount; i++) {   
            columnNames[i] = rsmd.getColumnLabel(i + 1).trim().toString().replace("_", "");   
            originalColumnNames[i] = rsmd.getColumnLabel(i + 1);   
        }  
   
        while (rs.next()) {   
            for (int i = 0; i < columnNames.length; i++) {   
                String setMethodName = "set" + columnNames[i];   
                for (int j = 0; j < methods.length; j++) {   
                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {   
                        setMethodName = methods[j].getName();   
                        Object value = rs.getObject(originalColumnNames[i]);  
   
                        try {   
                            if(value != null) {
                                Method setMethod = dto.getClass().getMethod(   
                                        setMethodName, value.getClass());   
                                setMethod.invoke(dto, value);   
                            }
                        } catch (Exception e) {   
                            Method setMethod = dto.getClass().getMethod(   
                                    setMethodName, String.class);   
                            setMethod.invoke(dto, value.toString());   
                        }   
                    }   
                }   
            }   
        }  
        return dto;   
    } 
     
    /**
     * ��resultSetת��ΪJSON����
     * @param rs
     * @return
     * @throws SQLException
     * @throws JsonIOException
     */
    public JsonArray resultSetToJsonArry(ResultSet rs) throws SQLException,JsonIOException  
    {  
       JsonArray array = new JsonArray();  
         
       // ��ȡ����  
       ResultSetMetaData metaData = rs.getMetaData();  
       int columnCount = metaData.getColumnCount();  
         
        while (rs.next()) {  
            JsonObject jsonObj = new JsonObject();  
              
            for (int i = 1; i <= columnCount; i++) {  
                String columnName =metaData.getColumnLabel(i);  
                String value = rs.getString(columnName);  
                jsonObj.addProperty(columnName, value);  
            }   
            array.add(jsonObj);   
        }  
         
       return array;  
    } 
     
    /**
     * ��resultSetת��ΪJsonObject
     * @param rs
     * @return
     * @throws SQLException
     * @throws JsonIOException
     */
    public  JsonObject resultSetToJsonObject(ResultSet rs) throws SQLException,JsonIOException  
    {  
        JsonObject jsonObj = new JsonObject();      
       ResultSetMetaData metaData = rs.getMetaData();  
       int columnCount = metaData.getColumnCount();  
        if (rs.next()) {  
            for (int i = 1; i <= columnCount; i++) {  
                String columnName =metaData.getColumnLabel(i);  
                String value = rs.getString(columnName);  
                jsonObj.addProperty(columnName, value);  
            }    
        } 
       return jsonObj;  
    } 
}