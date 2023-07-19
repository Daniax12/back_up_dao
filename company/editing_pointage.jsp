<%@page import="model.*"%>
<%@page import="java.util.*"%>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%  
    List<Employe> employees = (List<Employe>) request.getAttribute("employees");
    Pointage pointage = (Pointage)  request.getAttribute("pointage");
    String etat = (String) request.getAttribute("etat");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Edit | Pointage </title>
        <style> 
            /* <%@include file="dist/css/bootstrap.min.css"%> */
        </style>
    </head>
    <body>
        <section class="vh-100 gradient-custom">
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-lg-12 col-xl-11">
                    <div class="card bg-light text-secondary" style="border-radius: 25px;">
                        <div class="card-body p-md-5">
                            <div class="row justify-content-center">
                                <div class="col-md-10 col-lg-6 col-xl-5 order-2 order-lg-1"> 
                                    <p class="text-center h1 fw-bold mb-5 mx-1 mx-md-4 mt-4">
                                        Edit a pointage 
                                    </p>
                                    <% if(etat != null){
                                        if(etat.equals("0") == true){ %>
                                            <h6> Edit </h6>
                                        <% } else if(etat.equals("1") == true){ %>
                                            <h5> <%= error %> </h5>
                                        <% }
                                    } %>
                                        
                                    <form action="editing_pointage.go" method="POST" class="mx-1 mx-md-4">
                                        <input type="hidden" name="id_pointage" value="<%= pointage.getId_pointage() %>">
                                        <div class="d-flex flex-row align-items-center mb-2">
                                            <i class="fas fa-lock fa-lg me-3 fa-fw"></i>
                                            <div class="form-outline flex-fill mb-0">
                                                
                                                <select name="idemploye" class="form-select">
                                                    <option value="<%= pointage.getIdemploye() %>"><%= pointage.getEmploye_pointage() %></option>
                                                    <% for(int i = 0; i < employees.size(); i++) { %>
                                                    <option value= <%= employees.get(i).getIdEmploye() %> >
                                                        <%= employees.get(i).getNameEmploye() %> 
                                                    </option>
                                                    <% } %>
                                                 
                                                </select>
                                                    <p> <label class="form-label" > Employe </label> </p>
                                            </div>
                                        </div>

                                        <div class="d-flex flex-row align-items-center mb-2">
                                            <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                                            <div class="form-outline flex-fill mb-0">
                                                <%
                                                    String formattedDateTime = pointage.getDate_arrive().toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                                                %>
                                                <input type="datetime-local" class="form-control" name = "date_arrive" value="<%= formattedDateTime %>"/>
                                                <label class="form-label" >Arrival</label>
                                            </div>
                                        </div>

                                        <div class="d-flex flex-row align-items-center mb-2">
                                            <i class="fas fa-user fa-lg me-3 fa-fw"></i>
                                            <div class="form-outline flex-fill mb-0">
                                                <input type="text" class="form-control" name = "remarque" value="<%= pointage.getRemarque() %>"/>
                                                <label class="form-label" >Remarque</label>
                                            </div>
                                        </div>

                                        <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
                                            <button class="btn btn-primary btn-lg" style = "width:100%" type = "submit">
                                                Edit pointage
                                            </button>
                                        </div>
                                    </form>
                                    <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
                                        <a href="home.jsp">
                                            <button class="btn btn-outline-secondary" style = "width:100%">Previous</button>
                                        </a>
                                    </div>

                                </div>

                                <div class="col-md-10 col-lg-6 col-xl-7 d-flex align-items-center order-1 order-lg-2">
                                    <img src="img/emp.jpg"
                                    class="img-fluid" style="width:600px; height:400px; border-radius: 30px; margin-right:auto; margin-left:auto" alt="Sample image">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    </body>
</html>
