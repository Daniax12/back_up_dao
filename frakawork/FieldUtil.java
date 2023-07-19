/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1757.framework;

import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringJoiner;
import java.util.Map;


/**
 *
 * @author rango
 */
public class FieldUtil { 

    /*
     * To get field that contains attribute field name as "session"
     */
    public static Field session_field(Field[] allfields){
         for (Field field : allfields) {
            if(field.getName().equalsIgnoreCase("session") == true){
                return field;
            }
        }
        return null;
    }

    /*
     * To get field that class is fileUpload or return null
     */
    public static Field file_upload_field(Field[] allfields){
        for (Field field : allfields) {
            if(field.getType() == FileUpload.class){
                return field;
            }
        }
        return null;
    }


    /*
     * ALL METHOD PARAMETER VALUES SET IN AN ARRAY OF OBJECT -> for INVOKE METHOD
     * data here contains all value from method GetParameters
     */
    public static Object[] method_parameters_object(Method method, HashMap<String, String> data) throws Exception{
        try {
            HashMap<String, Class> myParameters = method_parameter_name(method);        // Get the hasmap containing details of all parameters of the method
            int count = 0;
            for(Map.Entry<String, Class> set : myParameters.entrySet()){
                if(data.containsKey(set.getKey()) == true){
                    count++;                                                            // Verify if some parameter name has same name as data key from getParameters
                }
            }

            Object[] result = new Object[count];
            if(count == 0) throw new Exception("Error on "+ myParameters.keySet().iterator().next());
            int i = 0;
            for(Map.Entry<String, Class> set : myParameters.entrySet()){            // Here we only have condition of int or string but we have to extend our work XXXX
                if(data.containsKey(set.getKey()) == true){
                    if(set.getValue().equals(int.class) == true){
                        result[i] = Integer.valueOf(data.get(set.getKey()));
                    } else if(set.getValue().equals(String.class) == true){
                        result[i] = data.get(set.getKey());
                    }
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all parameters of the method from data from input. Error "+e.getMessage());
        }
    }

    /*
     * DETAILS OF THE METHOD (name and its class)
     * -> String (name of all Method parameters)
     * -> Class is the class of the parameters
     */
    public static HashMap<String, Class> method_parameter_name(Method method) throws Exception{
        try {
            HashMap<String, Class> result = new HashMap<>();
            Parameter[] parameters = method.getParameters();
            for(Parameter parameter : parameters){ 
                result.put(parameter.getName(), parameter.getType());               // Get the parameter name and its class
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all parameters of the method. Error "+e.getMessage());
        }
    }

    /*
     * DETAIL OF ALL PARAMETER CLASS OF AN OBJECT METHOD GOT ONLY BY A METHOD NAME 
     * method is the name of the method
     */
    public static Class[] method_parameters_class(Object object, String method) throws Exception{
        try {
            Method[] all_methods = object.getClass().getDeclaredMethods();      // Get all declared method of the object
            Integer index = null;

            for(int i = 0; i < all_methods.length; i++){
                if(all_methods[i].getName().equals(method) == true){            // find the appropriate method by comparing their name
                    index = Integer.valueOf(i);
                }
            }

            if(index == null) throw new Exception("Method "+ method + " not found in the class "+ object.getClass().getName() );     // If index remains NULL -> Method not found

            Method the_Method = all_methods[index];                             // Get the main method
            Parameter[] parameters = the_Method.getParameters();                // Get all parameters of the method
            if(parameters.length == 0) return new Class[0];                     // If no parameters -> Array of length = 0

            Class[] result = new Class[parameters.length];                      
            for(int i = 0; i < parameters.length; i++){
                result[i] = parameters[i].getType();                            // ELse get all classes
            }
            return result;

        } catch (Exception e) {
            throw new Exception("Error on getting all method parameter class of the method "+ method + ". Error: "+e.toString());
        }
    }


    /*
     * Make all attributes of attribute to null
     */
    public static Object reinitialise_attribute(Object object) throws Exception{
        try {
            Object temp = new Object();
            Field[] all_fields = object.getClass().getDeclaredFields();
            for(Field field : all_fields){
                field.setAccessible(true);
                if(field.getType().equals(int.class) == true || field.getType().equals(double.class) == true || field.getType().equals(float.class) == true){
                    getSetter(object, field).invoke(object, 0);
                } else{
                    temp = null;
                    getSetter(object, field).invoke(object, temp);
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on setting null all attribute of an object. Error:" + e.toString());
        }
    }

        /**
     * INSERTING DATA FROM TABLE INTO AN OBJECT
     * @param <T>
     * @param object
     * @param fields all fields that have to be set
     * @param data HAshmaping a String -> Name Colonne and its string value from table / oldschool
     * @return
     * @throws Exception 
     */
    public static <T> T insertDataInObject(Object object, HashMap<String, String> data, List<Field> fields) throws Exception{
        Class<T> c = (Class<T>) object.getClass();
        Constructor<T> constru = c.getConstructor();
        T result = constru.newInstance();
        
        try {
            for(Field field : fields){
                String getColumn = columnName(field);           // Get the column name
                String valueField = data.get(getColumn);    // Get the value from hasmap

                // System.out.println("Column : "+getColumn);
                // System.out.println("valus is "+ valueField);

                if(valueField != null){
                    Class clazz = field.getType();                  // Get the type of the field
                    if(clazz == String.class){
                        getSetter(result, field).invoke(result, valueField);
                    } else if(clazz == Integer.class){
                        getSetter(result, field).invoke(result, Integer.valueOf(valueField));
                    } else if(clazz == Double.class){
                        getSetter(result, field).invoke(result, Double.valueOf(valueField));
                    } else if(clazz == Float.class){
                        getSetter(result, field).invoke(result, Float.valueOf(valueField));
                    } else if(clazz == Boolean.class){
                        getSetter(result, field).invoke(result, Boolean.valueOf(valueField));
                    } else if(clazz == Date.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        getSetter(result, field).invoke(result, date);
                    } else if(clazz == Calendar.class){
                        Calendar calendar = DateUtil.stringToCalendar(valueField, DatePattern.YYYY_MM_DD_hh_min_ss);
                        getSetter(result, field).invoke(result, calendar);
                    } else if(clazz == java.sql.Date.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        java.sql.Date theDate = DateUtil.utilDateToSqlDate(java.sql.Date.class, date);
                        getSetter(result, field).invoke(result, theDate);
                    } else if(clazz == java.sql.Timestamp.class){
                        java.sql.Timestamp theDate = Timestamp.valueOf(valueField);
                        getSetter(result, field).invoke(result, theDate);
                    } else if(clazz == java.sql.Time.class){
                        java.sql.Time time = DateUtil.stringToSqlTime(valueField);
                        getSetter(result, field).invoke(result, time);
                    }        
                } else {
                    Object null_object = null;
                     getSetter(result, field).invoke(result, null_object);
                }
            }
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error on inserting some data to "+object.getClass().getSimpleName());
        }  
    }


    /**
     * INSERTING DATA FROM TABLE INTO AN OBJECT -> HERE IT WILL USED FOR FRONT SERVLET, DATA FROM HTML
     * @param <T>
     * @param object
     * @param fields all fields that have to be set
     * @param data HAshmaping a String -> Name Colonne and its string value from table / oldschool
     * @return
     * @throws Exception 
     */
    public static <T> T insertDataInObject(Object object, HashMap<String, String> data, Field[] fields) throws Exception{
        Class<T> c = (Class<T>) object.getClass();
        Constructor<T> constru = c.getConstructor();
        T result = constru.newInstance();
        
        try {
            for(Field field : fields){
                String getColumn = columnName(field);           // Get the column name
                String valueField = data.get(getColumn);    // Get the value from hasmap

                System.out.println("Column : "+getColumn);
                System.out.println("valus is "+ valueField);

                if(valueField != null){
                    Class clazz = field.getType();                  // Get the type of the field
                    if(clazz == String.class){
                        getSetter(result, field).invoke(result, valueField);
                    } else if(clazz == Integer.class){
                        getSetter(result, field).invoke(result, Integer.valueOf(valueField));
                    } else if(clazz == Double.class){
                        getSetter(result, field).invoke(result, Double.valueOf(valueField));
                    } else if(clazz == Float.class){
                        getSetter(result, field).invoke(result, Float.valueOf(valueField));
                    } else if(clazz == Boolean.class){
                        getSetter(result, field).invoke(result, Boolean.valueOf(valueField));
                    } else if(clazz == Date.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        getSetter(result, field).invoke(result, date);
                    } else if(clazz == Calendar.class){
                        Calendar calendar = DateUtil.stringToCalendar(valueField, DatePattern.YYYY_MM_DD_hh_min_ss);
                        getSetter(result, field).invoke(result, calendar);
                    } else if(clazz == java.sql.Date.class){
                        Date date = DateUtil.stringToDate(valueField, DatePattern.YYYY_MM_DD);
                        java.sql.Date theDate = DateUtil.utilDateToSqlDate(java.sql.Date.class, date);
                        getSetter(result, field).invoke(result, theDate);
                    } else if(clazz == java.sql.Timestamp.class){
                        java.sql.Timestamp theDate = DateUtil.string_to_timestamp(valueField);      // html value
                        getSetter(result, field).invoke(result, theDate);
                    } else if(clazz == java.sql.Time.class){
                        java.sql.Time time = DateUtil.stringToSqlTime(valueField);
                        getSetter(result, field).invoke(result, time);
                    }        
                } else {
                    Object null_object = null;
                     getSetter(result, field).invoke(result, null_object);
                }
            }
            return result;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error("Error on inserting some data to "+object.getClass().getSimpleName());
        }  
    }


    
    /**
     * MAKE A GETTER FROM AN OBJECT WITH ITS SPECIFIC FIELD
     * @param obj
     * @param field
     * @return
     * @throws Exception 
     */
    public static Method getGetter(Object obj, Field field) throws Exception {
        try {
            String nameField = field.getName();
            return obj.getClass().getDeclaredMethod("get" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the Getters of "+field.getName());
        }
    }

    /**
     * SET ALL FIELD[] OF AN OBJECT -> LIST OF FIELD and ARE PRESENT IN DATABASE
     * 
     * @param object
     * @return
     * @throws Exception 
     */
    public static List<Field> fieldsToList(Object object) throws Exception{
        List<Field> result = new ArrayList<>();
        
        try {
            Field[] allFields = object.getClass().getDeclaredFields();
            for(Field field : allFields){
                if(field.isAnnotationPresent(ColumnField.class) == true)
                    result.add(field);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all fields of "+object.getClass().getSimpleName()+" to LIst");
        }
    }

    /**
     * GET ALL FIELD THAT VALUE ARE NOT NULL
     * @param object
     * @param fields
     * @return
     * @throws Exception 
     */
    public static List<Field> FieldValueNotNull(Object object, List<Field> fields) throws Exception{
        List<Field> result = new ArrayList<>();
        
        try {
            for(Field field : fields){
                field.setAccessible(true);
                if(getGetter(object, field).invoke(object) != null && field.isAnnotationPresent(ColumnField.class) == true) result.add(field);
//                if(field.get(object) != null) result.add(field);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all fields that are not null");
        }
    }

        /**
     * GET THE NAME COLUMN OF A FIELD WETHER THERE IS ANNOTATION OR NOT
     * @param field
     * @return 
     */
    public static String columnName(Field field){
        ColumnField fieldAnnot = field.getAnnotation(ColumnField.class);
        
        if(fieldAnnot != null){
            if(fieldAnnot.column().equals("1") == false) return fieldAnnot.column();
            else return field.getName();
        }
        return field.getName();
    }

        /**
     * GET THE COLUMN OF THE PRIMARY KEY OF THE TABLE -> FIELD
     * @param object
     * @return
     * @throws Exception 
     */
    public static Field myPrimaryKeyField(Object object, List<Field> fields) throws Exception{
        try {
            for(Field field : fields){
                if(field.getAnnotation(ColumnField.class).primary_key() == true){
                    return field;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the primary key column of "+object.getClass().getSimpleName());
        }
    }

    // GET ALL FIELDS EXEPT THE FIELD "except"
    public static List<Field> fieldExceptField(List<Field> fields, Field except) {
        List<Field> result = new ArrayList<>();
        
        for(Field field : fields){
            if(columnName(field).equals(columnName(except)) == false) result.add(field);
        }
        return result;
    }
    
    
    /**
     * SEARCHNG THE VALUE OF THE PRIMARY KEY OF AN OBJECT
     * @param object
     * @return
     * @throws Exception 
     */
    public static String myPrimaryKeyValue(Object object) throws Exception{
        try {
            List<Field> fields = fieldsToList(object);
            Field myPk = myPrimaryKeyField(object, fields);
            String value = (String) getGetter(object, myPk).invoke(object);
            
            if(value != null){
                throw new Exception("Primary key undefined because the result is null");
            }
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the primary key value");
        }
    
    }
    
    /**
     * MAKE A SETTER FROM AN OBJECT WITH ITS SPECIFIC FIELD
     * @param object
     * @param field
     * @return
     * @throws NoSuchMethodException 
     */
    public static Method getSetter(Object object, Field field) throws Exception {   
        try {
            String nameField = field.getName();
            String temp = "set" + nameField.substring(0, 1).toUpperCase() + nameField.substring(1);
            Method result = object.getClass().getDeclaredMethod(temp, field.getType());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on setting the field object");
        }
        
    }
}
