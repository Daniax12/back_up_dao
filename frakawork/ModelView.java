/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etu1757.framework;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rango;
 */
public class ModelView {
    private String vue;
    private HashMap<String, Object> data;
    private HashMap<String, Object> mv_session;

    // Usefull function
    /*
     * Adding item in the data
     */
    public void addItem(String key, Object object){
        if(this.getData() == null){
            this.setData(new HashMap<String, Object>());
        }
        this.getData().put(key, object);
    }

    /*
     * Adding session in modelview
     */
    public void add_session_item(String key, Object object){
        if(this.getMv_session() == null){
            this.setMv_session(new HashMap<>());
        }
        this.getMv_session().put(key, object);
    }

    // Constructors and setters
    public ModelView(String vue) {
        this.setVue(vue);
    }
    public ModelView(){}  

    public String getVue() {
        return vue;
    }

    public void setVue(String vue) {
        this.vue = vue;
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public HashMap<String, Object> getMv_session() {
        return mv_session;
    }

    public void setMv_session(HashMap<String, Object> mv_session) {
        this.mv_session = mv_session;
    }

    
    
}
