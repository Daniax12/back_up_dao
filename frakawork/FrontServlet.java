package etu1757.framework.servlet;

import etu1757.framework.AnnotedClass;
import etu1757.framework.Mapping;
import etu1757.framework.ModelView;
import etu1757.framework.Utilities;
import etu1757.framework.FileUpload;
import etu1757.framework.Scope;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.MultipartConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rango
 */
@MultipartConfig
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    HashMap<Class, Object> singletons;
    String packageName;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try{

            HttpSession servlet_session = request.getSession();
            String path = (String) request.getServletPath();
            ModelView mv = (ModelView) Utilities.getViewByPath(request, path, this.getMappingUrls(), this.getSingletons(), servlet_session);

            HashMap<String, Object> data = mv.getData();                                    // Get all data of the mv

            if(data != null){
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    // out.println(key + " - "+ value);

                    request.setAttribute(key, value);
                }
            }

            // Get the data session from model_view
            HashMap<String, Object> data_session = mv.getMv_session();
            if(data_session != null){
                for (Map.Entry<String, Object> entry : data_session.entrySet()) {
                    servlet_session.setAttribute(entry.getKey(), entry.getValue());
                }
            }

            RequestDispatcher dispat = request.getRequestDispatcher(mv.getVue());
            dispat.forward(request, response);
            

            
            // for (Map.Entry<String, Object> entry : data_session.entrySet()) {
            //     String key = entry.getKey();
            //     Object value = entry.getValue();
            //     out.print("<p>");
            //     out.println("Classe = " + key + " / ");
            //     out.println(" object = " + value + " / ");
            //     out.println("</p>");
            // }
        } catch(Exception e) {
            out.println("error");
            out.println(e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void init() throws ServletException 
    {
        this.setPackageName(this.getInitParameter("toScan")); 
        try {
            this.setMappingUrls(Utilities.get_annoted_method(this.getPackageName(), AnnotedClass.class));
            this.setSingletons(Utilities.get_classes_singletons(this.getPackageName(), Scope.class));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    public HashMap<String, Mapping> getMappingUrls() {
        return mappingUrls;
    }

    public void setMappingUrls(HashMap<String, Mapping> mappingUrls) {
        this.mappingUrls = mappingUrls;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public HashMap<Class, Object> getSingletons() {
        return singletons;
    }

    public void setSingletons(HashMap<Class, Object> singletons) {
        this.singletons = singletons;
    }

}
