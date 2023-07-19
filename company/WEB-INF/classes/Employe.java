/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.beans.Encoder;
import java.sql.*;

import etu1757.framework.*;

/**
 *
 * @author rango;
 */

@Scope
public class Employe {
    private String idEmploye;
    private String nameEmploye;
    private String idDepartement;
    private Integer numero;
    private java.util.Date dateEmbauche;
    private FileUpload badge;

    // Usefull function

    @AnnotedClass(methodName = "check_user.go")
    public ModelView login_employe(String name_user) throws Exception{
        ModelView result = new ModelView("home.jsp");
        if(name_user != null){
            if(name_user.equalsIgnoreCase("admin") == true){
                result.add_session_item("profile", "admin");
                result.add_session_item("is_connected", true);
            } else {
                result.add_session_item("is_connected", true);
            }
        } 
        return result;
    }

    // Get the model view to show employe details(ModelaView with parameter)
    @Auth
    @AnnotedClass(methodName = "detail_emp.go")
    public ModelView detail_employe(String id) throws Exception{
        ModelView result = new ModelView("detail_employe.jsp");
        try {        
            Employe byId = Employe.employe_by_id(id, null);
            result.addItem("emp", byId);
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error", e.getMessage());
        } 
        return result;
    }

    // Get an employe by id
    public static Employe employe_by_id(String id, Connection connection) throws Exception{
        boolean isOpen = false;
        if(connection == null){
            ConnectionBase cb = new ConnectionBase();
            connection = cb.dbConnect();
        } else {
            isOpen = true;
        }

        PreparedStatement stm = null;
        ResultSet resultSet = null;
        Employe result = null;

        try {
            stm = connection.prepareStatement("SELECT * FROM employe WHERE idemploye = ?");
            stm.setString(1, id);

            resultSet = stm.executeQuery();

            while(resultSet.next()){
                String idEmp  = resultSet.getString("idemploye");
                String name = resultSet.getString("nameemploye");
                String iddepart = resultSet.getString("iddepartement");
                Integer num = resultSet.getInt("numero");
                java.sql.Date date = resultSet.getDate("dateembauche");
                byte[] imageData = resultSet.getBytes("badge");

                java.util.Date theDate = new java.util.Date(date.getTime());
                result = new Employe(id, name, iddepart, num, theDate);

                FileUpload fp = new FileUpload();
                fp.setFile(imageData);
                result.setBadge(fp);
            }
            return result;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on getting employe by id: " + ex.getMessage());
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    // Saving an employe but also returning an MOdelView
    @Auth(status = "admin")
    @AnnotedClass(methodName = "save_employe.go")
    public ModelView save_employe() throws Exception{
        ModelView result = new ModelView("emp_form.jsp");
        try {        
            String save = this.save_employe_database(null);
            result.addItem("etat", "0");            
        } catch (Exception e) {
            result.addItem("etat", "1");
            result.addItem("error on inserting with: ", e.getMessage());
        } 
        try {
            List<Departement> departments = BddObject.find("departement", new Departement(), null);
            result.addItem("departments", departments); 
        } catch (Exception ex) {
            result.addItem("error :", ex.getMessage());
        }
        
        return result;
    }


    // Simple functio of saving an employe
    public String save_employe_database(Connection connection) throws Exception{
        boolean isOpen = false;
        if(connection == null){
            ConnectionBase cb = new ConnectionBase();
            connection = cb.dbConnect();
        } else {
            isOpen = true;
        }

        PreparedStatement stm = null;
        ResultSet resultSet = null;

        // System.out.println("name is "+this.getNameEmploye());
        // System.out.println("numero is "+this.getNumero());

        try {
            stm = connection.prepareStatement("INSERT INTO employe(idemploye, nameemploye, iddepartement, numero, dateembauche, badge) VALUES('EMP'||nextval('emp_seq'), ?, ?, ?, ?, ?) returning idemploye");
            stm.setString(1, this.getNameEmploye());
            stm.setString(2, this.getIdDepartement());
            stm.setInt(3, this.getNumero());
            java.sql.Date date = DateUtil.utilDateToSqlDate(java.sql.Date.class, this.getDateEmbauche());

            stm.setDate(4, date);
            stm.setBytes(5, this.getBadge().getFile());

            resultSet = stm.executeQuery();
            String id = null;
            while(resultSet.next()){
                id = resultSet.getString("idemploye");
            }

            if(isOpen == false) connection.commit();
            return id;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on saving employe: " + ex.getMessage() + " with name "+this.getNameEmploye());
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    // Get the form of the employe
    @AnnotedClass(methodName = "emp_formulaire.go")
    public ModelView myForm() throws Exception{
        ModelView result = new ModelView("emp_form.jsp");
        try {          
            List<Departement> departments = BddObject.find("departement", new Departement(), null);
            result.addItem("departments", departments);

            return result;
        } catch (Exception e) {
            result.addItem("error on getting form with :", e.getMessage());
            e.printStackTrace();
        }      
        return result;
    }

    @AnnotedClass(methodName = "list_emp.go")
    public ModelView list_employe() throws Exception{
        ModelView result = new ModelView("all_employe.jsp");
        try {          
            List<Employe> all = Employe.findAll(null);
            result.addItem("employes", all);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.addItem("error", e.getMessage());
        }     
        return result;
    }

    // Get all employe in database
    public static List<Employe> findAll(Connection connection) throws Exception{
        boolean isOpen = false;
        if(connection == null){
            ConnectionBase cb = new ConnectionBase();
            connection = cb.dbConnect();
        } else {
            isOpen = true;
        }

        PreparedStatement stm = null;
        ResultSet resultSet = null;
        List<Employe> result = new ArrayList<>();
        try {
            stm = connection.prepareStatement("SELECT * FROM employe");

            resultSet = stm.executeQuery();

            while(resultSet.next()){
                Employe temp = null;
                String idEmp  = resultSet.getString("idemploye");
                String name = resultSet.getString("nameemploye");
                String iddepart = resultSet.getString("iddepartement");
                Integer num = resultSet.getInt("numero");
                java.sql.Date date = resultSet.getDate("dateembauche");

                java.util.Date theDate = new java.util.Date(date.getTime());
                temp = new Employe(idEmp, name, iddepart, num, theDate);
                result.add(temp);
            }
            return result;

        } catch (Exception ex) { 
           ex.printStackTrace();
           throw new Exception("Error on getting all employes: " + ex.getMessage());
        } finally {
            stm.close();
            if(isOpen == false) connection.close();
        }
    }

    public Employe(){}

    public String getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(String idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getNameEmploye() {
        return nameEmploye;
    }

    public void setNameEmploye(String nameEmploye) throws Exception{
        this.nameEmploye = nameEmploye;
    }

    public String getIdDepartement() {
        return idDepartement;
    }

    public void setIdDepartement(String idDepartement) {
        this.idDepartement = idDepartement;
    }


    public Integer getNumero() {
        return numero;
    }


    public void setNumero(Integer numero) {
        this.numero = numero;
    }


    public java.util.Date getDateEmbauche() {
        return dateEmbauche;
    }


    public void setDateEmbauche(java.util.Date dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }    

    public Employe(String idEmploye, String nameEmploye, String idDepartement, Integer numero, java.util.Date dateEmbauche) throws Exception{
        try {
            this.setIdEmploye(idEmploye);
            this.setNameEmploye(nameEmploye);
            this.setIdDepartement(idDepartement);
            this.setNumero(numero);
            this.setDateEmbauche(dateEmbauche);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
    }

    public FileUpload getBadge() {
        return badge;
    }

    public void setBadge(FileUpload badge) {
        this.badge = badge;
    }

}
