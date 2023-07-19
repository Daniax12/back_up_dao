<%@page import="model.*"%>
<%@page import="java.util.*"%>
<%@page import="etu1757.framework.*"%>

<% 
    List<Pointage> all = (List<Pointage>) request.getAttribute("pointages");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Liste | Pointage </title>
        <style> 
            /* <%@include file="dist/css/bootstrap.min.css"%> */
        </style>
    </head>
    <body>
        <div class="d-flex flex-column" style="width: 60%; margin-left: auto; margin-right: auto; margin-top: 100px;">
            <div class="border border-2 text-center" style="padding: 2% 2% 2% 2%;">
                <h2> List of all pointages </h2>
            </div>
            <table class="table table-stripped">
                <thead>
                    <td class="text-center"> Name </td>
                    <td class="text-center"> Date arrive </td>
                    <td class="text-center"> Remarque </td>
                    <td></td>
                    <td></td>
                </thead>
                <% for(int i = 0; i < all.size(); i++){ %>
                    <tbody>
                        <td> <%= all.get(i).getEmploye_pointage() %> </td>
                        <td class="text-center">  <%= all.get(i).getDate_arrive() %> </td>
                        <td class="text-center">  <%= all.get(i).getRemarque() %> </td>
                        <td class="text-center"> <a href="edit_pointage.go?id_pointage=<%= all.get(i).getId_pointage() %>"> Edit </a> </td>
                    </tbody>
                <% } %>
            </table>
        </div>
        <div class="d-flex justify-content-center mx-4 mb-3 mb-lg-4">
            <a href="home.jsp">
                <button class="btn btn-outline-secondary">
                    Return
                </button>
            </a>
        </div>
    </body>
</html>
