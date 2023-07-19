/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1757.framework;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collection;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;

/**
 *
 * @author rango
 */
@MultipartConfig
public class Utilities {
    
    // Get the modelview by a servletCOntext path
    public static ModelView getViewByPath(HttpServletRequest request, String servletContext, HashMap<String, Mapping> mapUrl, HashMap<Class, Object> singletons_data, HttpSession servlet_session) throws Exception{
        
        ModelView result = new ModelView("/");
        if(servletContext == null) return result;
        String[] split = servletContext.split("/");

        try {
            if(mapUrl.containsKey(split[1]) == true){
                String className = mapUrl.get(split[1]).getClassName();                             // Get the name of the class
                String method_string = mapUrl.get(split[1]).getMethod();                            // Get the method
                Class<?> classType = Class.forName(className);                                      // Recast the name => Classe
                // Object main_object = classType.getDeclaredConstructor().newInstance();              // INstantiate the object       -->> SINGLETON
                Object main_object = treat_object_instance(classType, singletons_data);
               // System.out.println(main_object.getClass().getName() + " has adress "+main_object);    // If we want to verify singleton, the adress never change

                Field[] obj_field = classType.getDeclaredFields();                                  // Get all field of the object
              
                Field upload_trigg = FieldUtil.file_upload_field(obj_field);                        // TO know if there is upload_trigg
                Field session_field = FieldUtil.session_field(obj_field);
                if(session_field != null) insert_session_object_attribute(main_object, session_field, servlet_session);

                Enumeration enumeration = request.getParameterNames();                              // Get all parameters name sent in the request
                Collection<Part> parts = null;
             //   System.out.println("THe method is "+request.getMethod());
                if(request.getMethod().equals("POST") == true && upload_trigg != null){
                    try {
                        parts = request.getParts();
                    } catch (Exception e) {
                    
                    }
                }

                HashMap<String, String> data_input = new HashMap<>();       

                while(enumeration.hasMoreElements()){
                    String parameterName = (String) enumeration.nextElement();                      // Get the name of the parameters
                    // System.out.println("param is : "+ parameterName);
                    // System.out.println("VAlue is "+ ((String) request.getParameter(parameterName)).trim());
                    data_input.put(parameterName, ((String) request.getParameter(parameterName)).trim());        // Get the value of the parameters and trim the result       
                }
                
                Method method = null;
              //  System.out.println("Verification is "+ has_field_upload(parts, upload_trigg));
               
                main_object = FieldUtil.insertDataInObject(main_object, data_input, obj_field);     // Insert all fields data if some parameter name is same as object field name

                if(parts!= null && has_field_upload(parts, upload_trigg) == true){
                    treat_file_upload(main_object, upload_trigg, request);                          // Get an uploaded file
                }

                Class[] parameter_class = FieldUtil.method_parameters_class(main_object, method_string);    // Get the method parameter class if it has
                if(parameter_class.length == 0) {   
                    method = classType.getDeclaredMethod(method_string);

                    boolean can_execute = can_execute(method, servlet_session);                             // Get the authentification
                    if(can_execute == false) {
                        ModelView error_page = new ModelView("error_page.jsp");
                        return error_page;
                    }        
                    result = (ModelView) method.invoke(main_object);                                    // -> No parameters: give the second parameters of INVOKE and GETDECLAREDMETHOD as null
                }else{
                    method = classType.getDeclaredMethod(method_string, parameter_class);               // -> GIve the second parameters 
                    boolean can_execute = can_execute(method, servlet_session);
                    if(can_execute == false) {
                        ModelView error_page = new ModelView("error_page.jsp");
                        return error_page;
                    }        

                    Object[] parameters = FieldUtil.method_parameters_object(method, data_input);       // Get all value of all each parameter from data from input INTO Object array
                    result = (ModelView) method.invoke(main_object, parameters);    
                }                       
            }
            
            return result;  
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the modelView. Error:" + e.toString());
            // ModelView error_404 = new ModelView("error_404.jsp");
            // return error_404;
        } 
    }

    // SPRINT 12
    public static void insert_session_object_attribute(Object object, Field field, HttpSession servlet_session) throws Exception{
        try {
            if(field.getType() != HashMap.class) throw new Exception("Type Hashmap incompatible with "+field.getType().getSimpleName());
            Enumeration<String> attributeNames = servlet_session.getAttributeNames();
            HashMap<String, Object> data = new HashMap<String, Object> ();
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = servlet_session.getAttribute(attributeName);
                data.put(attributeName, attributeValue);
            }
            FieldUtil.getSetter(object, field).invoke(object, data);

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /*
     * With the session of the httpSession, we must know if a method can be excecuted or not
     */
    public static boolean can_execute(Method model_view_method, HttpSession servlet_session) throws Exception{
        try {
            Annotation method_annotation = model_view_method.getAnnotation(Auth.class);
            if(method_annotation == null) return true;
            String profile = (String) servlet_session.getAttribute("profile");
            Boolean status = (Boolean) servlet_session.getAttribute("is_connected");
            if(status == null || status == false) return false;

            String annotation_value = (String) model_view_method.getAnnotation(Auth.class).status();
            if(annotation_value.equalsIgnoreCase("admin") == true){
                if(profile != null){
                    if(profile.equalsIgnoreCase(annotation_value)) return true;
                    return false;
                } else {
                    return false;
                }
            } 
            return true;
        } catch (Exception e) {
            throw new Exception("Error on knowing if a method can be excecuted or not. Error : "+ e.toString());
        }
    }

    /*
     * Treating if class is singleton or not
     */
    public static Object treat_object_instance(Class clazz, HashMap<Class, Object> datas) throws Exception{
        
        Object object = null;
        try {
            if(datas.containsKey(clazz) == true){
                object = datas.get(clazz);
                object = FieldUtil.reinitialise_attribute(object);
            } else {
                object = clazz.getDeclaredConstructor().newInstance();
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting the object from singletons data. Error:" + e.toString());
        }
    }


    
    // To know if a class has a field_upload as attribute
    public static boolean has_field_upload(Collection<Part> parts, Field field){
        for (Part part : parts) {
            if (part.getName().equals(field.getName())) {
               return true;
            }
        }
        return false;
    }


    /**
     * Treat a file uploaded 
     * @param object : THe object that have the field FILEUPLOAD
     * @param field : The field
     * @param request 
     * @throws Exception
     */
    public static void treat_file_upload(Object object, Field field, HttpServletRequest request) throws Exception{
        try {
            Part mf = request.getPart("badge");       // Get the value from input file type
            System.out.println(mf.getSubmittedFileName());
            //System.out.println("byte is "+ new String(mf.getInputStream().readAllBytes()));
            FileUpload fileUpload = new FileUpload();
            fileUpload.setName(replace_characters(mf.getSubmittedFileName()));           // Get the file name 
            fileUpload.setFile(mf.getInputStream().readAllBytes());
            FieldUtil.getSetter(object, field).invoke(object, fileUpload);
            //System.out.println("After "+new String(fileUpload.getFile()));

        } catch (Exception e) {
            throw new Exception("Error on treating upload file with: "+e.getMessage());
        }
    }
    
    /**
     * Replace the non usual name_file characters 
     * @param name_file
     * @return
     */
    public static String replace_characters(String name_file){
        // Define the pattern for characters to be replaced
        String pattern = "[*/\\-+.;']";

        // Replace the characters using the pattern with underscores
        String replaced = name_file.replaceAll(pattern, "_");

        return replaced;
    }

    /*
     * Get all classes that have a specific package -> Here, need for singletons
     */
    public static HashMap<Class, Object> get_classes_singletons(String package_name, Class<? extends Annotation> annotationClass) throws Exception{
        HashMap<Class, Object> result = new HashMap<>();
        
        try {
            List<Class<?>> classes = get_classes_in_package(package_name);
            for (Class<?> cls : classes) {
                Annotation annotation = cls.getAnnotation(annotationClass);
                if(annotation != null){
                    Object object = cls.getDeclaredConstructor().newInstance(); 
                    result.put(cls, object);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all classes annoted. Error :" + e.getMessage());
        }
    }


    /**
     * Get all methods annoted with a specific annotation in a package
     * @param package_name 
     * @param annotationClass
     * @return
     * @throws Exception
     */    

    public static HashMap<String,Mapping> get_annoted_method(String package_name, Class<? extends Annotation> annotationClass) throws Exception {
        try {
            List<Class<?>> classes = get_classes_in_package(package_name);
            HashMap<String,Mapping> annotatedMethods = new HashMap<String,Mapping>();
            for (Class<?> cls : classes) {
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods) {
                    Annotation annotation = method.getAnnotation(annotationClass);
                    if (annotation != null) {
                        annotatedMethods.put(((AnnotedClass) annotation).methodName(), new Mapping( cls.getName(), method.getName()));
                    }
                }
            }
            return annotatedMethods;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting all methods annoted. Error :" + e.getMessage());
        }
        
    }
    
    // Get all classes in a specific package
    public static List<Class<?>> get_classes_in_package(String scannedPackage) throws Exception {
        
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String path = scannedPackage.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = new ArrayList<>();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            List<Class<?>> classes = new ArrayList<>();
            for (File directory : dirs) {
                classes.addAll(find_classes_in_directory(directory, scannedPackage));
            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on gettimg classes in package. Error :" + e.getMessage());
        }
        
    }

    // Get all classes in a specific Directory
    private static List<Class<?>> find_classes_in_directory(File directory, String package_name) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        try {
            if (directory.exists() == false) {                                                                  // If the directory doesnt exist                  
                return classes;
            }
            File[] files = directory.listFiles();                                                               // Get all file in the directory
            for (File file : files) {
                if (file.isDirectory() == true) {                                                               // Verify again if file is a directory itself
                    assert !file.getName().contains(".");                                                     // Verify if the name doesnt contains a dot                                    
                    classes.addAll(find_classes_in_directory(file, package_name + "." + file.getName()));           // Recurse the function to get all class
                } else if (file.getName().endsWith(".class")) {
                    String className = package_name + '.' + file.getName().substring(0, file.getName().length() - 6);    // Delete the .class
                    classes.add(Class.forName(className));                                                      // Cast the class and insert it tn the result
                }
            }
            return classes;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error on getting classes in a specific directory. Error :" + e.getMessage());
        }
    }

}
    


