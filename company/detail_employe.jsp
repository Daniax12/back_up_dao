<%@page import="model.*"%>
<%@page import="java.util.*"%>
<%@page import="etu1757.framework.*"%>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<%  
    Employe employe = (Employe) request.getAttribute("emp");
    byte[] imageData = employe.getBadge().getFile();
    String base64Image = Base64.getEncoder().encodeToString(imageData);
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Detail | Employe </title>
        <style> 
            /* <%@include file="dist/css/bootstrap.min.css"%> */
            /* <%@include file="dist/css/my.css"%> */
            .height-100{
                height:100vh;
            }

            .card{
                width:380px;
                border:none;
            }

            .dots {
                height: 20px;
                width: 20px;
                margin-top:4px;
                margin-left:4px;
                background-color:#dc3545;
                border-radius: 50%;
                border: 2px solid #fff;
                display:flex;
                align-items:center;
                justify-content:center;
                color:#fff;
                font-size:10px;
            }

            .list li{
                display:flex;
                align-items:center;
                justify-content:space-between;
                padding:13px;
                border-top:1px solid #eee;
                cursor:pointer;
            }

            .list li:hover{
                background-color:#dc3545;
                color:#fff;
            }
        </style>
    </head>
    <body>
        <div class="container height-100 d-flex justify-content-center align-items-center">  
            <div class="card text-center">
                <div class="py-4 p-2">           
                    <div>
                        <img src="data:image/jpeg;base64,<%= base64Image %>" alt="NOt found" class="rounded" width="100">
                    </div>
                <div class="mt-3 d-flex flex-row justify-content-center">
                    <h5> <%= employe.getNameEmploye() %> </h5>
                </div>              
                <span> Departement  <%= employe.getIdDepartement() %> </span>                  
                </div>
                
                <div>
                    <ul class="list-unstyled list">
                        <li>
                            <span class="font-weight-bold"> Id </span>
                            <div>
                                <span class="mr-1"> <%= employe.getIdEmploye() %> </span>
                            </div>
                        </li>
                        
                        <li>
                            <span class="font-weight-bold"> Numero </span>
                            <div>
                                <span class="mr-1"><%= employe.getNumero() %></span>
                            </div>
                        </li>
                        
                        <li>
                            <span class="font-weight-bold"> Date d'embauche </span>
                            <div>
                                <span class="mr-1"><%= DateUtil.dateToString(employe.getDateEmbauche(), DatePattern.DD_MM_YYYY) %></span>
                            </div>
                        </li>
                    </ul>
                </div>   
            </div>
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
