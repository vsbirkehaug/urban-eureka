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
            response.addDateHeader ("Expires", 0);
            %>
    </head>
    <body>
        <div class="wrapper">
        <h1>XYZ Drivers Association</h1>
                                                              
        <div class="login-page">
        <div class="centered-content">
            <h2>Congratulations, you have registered!</h2>

            </br>
                <div class="centered-content-body"
                    <p>Your username is: <%= (String)request.getAttribute("username") %></p>
                    <p>Your password is: <%= (String)request.getAttribute("password") %></p>
                    <p>You will need to remember these details to log in.</p>
                    
                    <form id="routerform" method="POST" action="PageRouter.do">
                    <ul>                      
                        <li onclick="document.getElementById('routerform').submit();" class="user-button">Go to login <input type="hidden" name="action" value="login" /></li>                                          
                    </ul>
                    </form>
                </div>
        </div>
        </div>  
        </div> 
    </body>
</html>
