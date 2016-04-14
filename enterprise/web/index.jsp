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

        <%  response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
            response.addHeader("Pragma", "no-cache");
            response.addDateHeader("Expires", 0);
        %>

        <script src="js/toggleReg.js"></script>
        <script language="javascript">
            function checkReg() {
            <% boolean doReg = false;
                  if (request.getAttribute("registrationState") != null && request.getAttribute("registrationState") == "true") {
                      doReg = true;
                     } else {
                      doReg = false;
                  }%>
                var s =<%=doReg%>;
                toggling(s);
            }
        </script> 

    </head>
    <body onload="checkReg()">

        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="login-page">
                <div class="form">

                    <p class="error">
                        <% if (request.getAttribute("message") != null) {
                                out.println(request.getAttribute("message"));
                                out.println("");
                            }
                        %>
                    </p>         

                    <form class="register-form" method="POST" action="Register.do">
                        <input type="text" name="name" placeholder="name"/>
                        <input type="text" name="address" placeholder="address"/>
                        <p class="label">Date of birth</p>
                        <input type="date" name="dob" placeholder="date of birth"/>
                        <p class="label">Registration date</p>
                        <input type="date" name="registrationdate" placeholder="registration date"/>
                        <input class="button" type="submit" name="tbl" value="Register"/>
                        </br>         
                        <p class="message">Already registered? <a href="#">Sign In</a></p>
                    </form>
                    <form class="login-form" method="POST" action="Login.do">
                        <input type="text" name="username" placeholder="username"/>
                        <input type="password" name="password" placeholder="password"/>
                        <input class="button" type="submit" name="tbl" value="Log in"/>
                        <label><input type="checkbox" name="admin" value="yes">Admin</label>
                        <p class="message">Not registered? <a href="#">Create an account</a></p>
                    </form>
                </div>
            </div>
        </div>
        <script src="js/login_helper.js"></script>
        <script src="js/login.js"></script>
    </body>
</html>
