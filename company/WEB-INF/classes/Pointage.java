

package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import etu1757.framework.AnnotedClass;
import etu1757.framework.BddObject;
import etu1757.framework.ColumnField;
import etu1757.framework.TableAnnotation;
import etu1757.framework.ModelView;
import etu1757.framework.Ordering;
import model.Departement;
import model.Employe;

import java.sql.*;

@TableAnnotation(nameTable = "pointage", sequence = "pointage_seq", prefix = "PO_")
public class Pointage {
    @ColumnField(column = "id_pointage", primary_key = true, is_increment = true)
    private String id_pointage;

    @ColumnField(column = "idemploye")
    private String idemploye;

    @ColumnField(column = "nameemploye")
    private String employe_pointage;

    @ColumnField(column = "date_arrive")
    private java.sql.Timestamp date_arrive;

     @ColumnField(column = "remarque")
    private String remarque;

    @AnnotedClass(methodName = "editing_pointage.go")
    public ModelView editing_pointage() throws Exception{
        ModelView result = null;
        try {
            BddObject.updatingObject(this, null);
            result = this.list_pointage();
        } catch (Exception e) {
            result.addItem("error", e.getMessage());
        } finally{
            return result;
        }
    }

    /*
     * GET THE FORM OF EDITING POINTAGE
     */
    @AnnotedClass(methodName = "edit_pointage.go")
    public ModelView edit_pointage(String id_pointage) throws Exception{
        ModelView result = new ModelView("editing_pointage.jsp");

        try {
            List<Employe> employees = Employe.findAll(null);
            result.addItem("employees", employees);

            Pointage pointage = new Pointage();
            pointage.setId_pointage(id_pointage);
            pointage = BddObject.findById("v_pointage_detail", pointage, null);
            result.addItem("pointage", pointage);            

        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error", e.getMessage());
        } finally{
            return result;
        }
    }


    /*
     * LIST OF ALL POINTAGES
     */
    @AnnotedClass(methodName = "list_pointage.go")
    public ModelView list_pointage() throws Exception{
        ModelView result = new ModelView("list_pointage.jsp");
        try {        
            List<Pointage> pointages = BddObject.findByOrder("v_pointage_detail", new Pointage(), "date_arrive", Ordering.DESC, null);
            result.addItem("pointages", pointages);
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error", e.getMessage());
        } 
        return result;
    }

    /*
     * INSERTING THE POINTAGE IN DATABASE
     */
    @AnnotedClass(methodName = "save_pointage.go")
    public ModelView insert_pointage() throws Exception{
        ModelView result = new ModelView("pointage.jsp");
        try {        
            String save = BddObject.insertInDatabase(this, null);
            result.addItem("etat", "0");            

            List<Employe> employees = Employe.findAll(null);
            result.addItem("employees", employees);
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error", e.getMessage());
        } 
        return result;
    }


     /*
      * GET POINTAGE FORM
      */
    @AnnotedClass(methodName = "pointage_formulaire.go")
    public ModelView myForm() throws Exception{
        ModelView result = new ModelView("pointage.jsp");
        try {          
            List<Employe> employees = Employe.findAll(null);
            result.addItem("employees", employees);

            return result;
        } catch (Exception e) {
            result.addItem("error", e.getMessage());
            e.printStackTrace();
        }      
        return result;
    }

    // GETTERS AND SETTERS
    public String getId_pointage() {
        return id_pointage;
    }

    public void setId_pointage(String id_pointage) {
        this.id_pointage = id_pointage;
    }

    public String getIdemploye() {
        return idemploye;
    }

    public void setIdemploye(String idemploye) {
        this.idemploye = idemploye;
    }

    public java.sql.Timestamp getDate_arrive() {
        return date_arrive;
    }

    public void setDate_arrive(java.sql.Timestamp date_arrive) {
        this.date_arrive = date_arrive;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getEmploye_pointage() {
        return employe_pointage;
    }

    public void setEmploye_pointage(String employe_pointage) {
        this.employe_pointage = employe_pointage;
    }
}
