<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.*"%>

<!DOCTYPE html>
<html>
    <head>
        <title> Home </title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style> 
            /* <%@include file="dist/css/bootstrap.min.css"%> */
        </style>
    </head>
    <body>
        <div class="d-flex flex-row justify-content-between" style="width: 50%;">
            <div> 
                <a class="btn btn-primary" href="list_emp.go"> Employes </a>
            </div>
            <div> 
                <a class="btn btn-primary" href="emp_formulaire.go"> Insert employe</a>
            </div>
            <div> 
                <a class="btn btn-primary" href="dep_formulaire.go"> Insert departement</a>
            </div>

            <div> 
                <a class="btn btn-primary" href="pointage_formulaire.go"> Insert pointage</a>
            </div>

            <div> 
                <a class="btn btn-primary" href="list_pointage.go"> Liste pointage</a>
            </div>
        </div>
    </body>
</html>
