/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.*;

import etu1757.framework.*;
/**
 *
 * @author rango;
 */
@Scope
@TableAnnotation(nameTable = "departement", sequence = "dept_seq", prefix = "DEPT_")
public class Departement {
    @ColumnField(column = "iddepartement", primary_key = true, is_increment = true)
    private String idDepartement;

    @ColumnField(column = "namedepartement")
    private String nameDepartement;

    @ColumnField(column = "location")
    private String location;

    private HashMap<String, Object> session;

    // Saving an employe but also returning an MOdelView
    @Auth(status = "admin")
    @AnnotedClass(methodName = "save_dept.go")
    public ModelView save_departement() throws Exception{
        ModelView result = new ModelView("dep_form.jsp");
        try {        
            String save = BddObject.insertInDatabase(this, null);
            result.addItem("etat", "0");            
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error on inserting with: ", e.getMessage());
        } 
        return result;
    }

    // Get the form of the departement
    @AnnotedClass(methodName = "dep_formulaire.go")
    public ModelView dept_form() throws Exception{
        ModelView result = new ModelView("dep_form.jsp");
        try {          

            return result;
        } catch (Exception e) {
            result.addItem("error on getting form of departement with :", e.getMessage());
            e.printStackTrace();
        }      
        return result;
    }


    // COnstructors and Getters and Setters

    public Departement(String idDepartement, String nameDepartement, String location) {
        this.idDepartement = idDepartement;
        this.nameDepartement = nameDepartement;
        this.location = location;
    }

    public Departement(){}

    public String getIdDepartement() {
        return idDepartement;
    }
    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }
    public String getNameDepartement() {
        return nameDepartement;
    }
    public void setNameDepartement(String nameDepartement) {
        this.nameDepartement = nameDepartement;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public HashMap<String, Object> getSession() {
        return session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    
  
}
