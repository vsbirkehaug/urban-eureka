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
        <link rel="stylesheet" href="css/login.css">
    </head>
    <body>
        <div class="login-page">
        <div class="form">
          <form class="register-form" method="POST" action="UserService.do">
            <input type="text" placeholder="name"/>
            <input type="text" placeholder="address"/>
            <input type="date" placeholder="date of birth"/>
            <input class="button" type="submit" name="tbl" value="Register"/>
            </br>         
            <p class="message">Already registered? <a href="#">Sign In</a></p>
          </form>
          <form class="login-form">
            <input type="text" placeholder="username"/>
            <input type="password" placeholder="password"/>
            <button>login</button>
            <p class="message">Not registered? <a href="#">Create an account</a></p>
          </form>
        </div>
      </div>
            <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
            <script src="javascript/login.js"></script>
    </body>
</html>
