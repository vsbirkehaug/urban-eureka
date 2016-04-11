<%-- 
    Document   : index
    Created on : 09-Mar-2016, 16:52:19
    Author     : vilde
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User login</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <div class="login-page">
        <div class="centered-content">
            <h1>Congratulations, you have registered!</h1>

            </br>
                <div class="centered-content-body"
                    <p>Your username is: <%= (String)request.getAttribute("username") %></p>
                    <p>Your password is: <%= (String)request.getAttribute("password") %></p>
                    <p>You will need to remember these details to log in.</p>
                </div>
        </div>
        </div>  
    </body>
</html>
