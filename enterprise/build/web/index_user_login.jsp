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
  
           <script src="js/toggleReg.js"></script>
           <script language="javascript">
                function checkReg(){   
                 <% boolean doReg = false; 
                 if(request.getAttribute("registrationState") != null && request.getAttribute("registrationState") == "true") { 
                     doReg=true;
                    } else { 
                     doReg=false;
                    }%>
                    var s=<%=doReg%>;           
                    toggling(s);
                 }
            </script> 
           
    </head>
    <body onload="checkReg()">
        <div class="login-page">
        <div class="form">
    
            <p class="error">
            <% if(request.getAttribute("message") != null) {
                    out.println(request.getAttribute("message")); 
                    out.println("");   
                }        
            %>
            </p>         
             
          <form class="register-form" method="POST" action="NewUser.do">
            <input type="text" name="name" placeholder="name"/>
            <input type="text" name="address" placeholder="address"/>
            <input type="date" name="dob" placeholder="date of birth"/>
            <input class="button" type="submit" name="tbl" value="Register"/>
            </br>         
            <p class="message">Already registered? <a href="#">Sign In</a></p>
          </form>
          <form class="login-form" method="POST" action="Login.do">
            <input type="text" name="username" placeholder="username"/>
            <input type="password" name="password" placeholder="password"/>
             <input class="button" type="submit" name="tbl" value="Log in"/>
            </br>       
            <p class="message">Not registered? <a href="#">Create an account</a></p>
          </form>
        </div>
      </div>
            <script src="js/login_helper.js"></script>
            <script src="js/login.js"></script>
    </body>
</html>
