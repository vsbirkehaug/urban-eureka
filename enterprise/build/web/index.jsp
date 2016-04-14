<%-- 
    Document   : index
    Created on : 09-Mar-2016, 16:52:19
    Author     : vilde
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="services.AddressLookup" %>
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
        <script language="javascript">
            function getAddress() {
                <%
                  String lookupaddress = "";
                  if (request.getAttribute("lookupaddress") != null) {
                      lookupaddress = (String)request.getAttribute("lookupaddress");
                     } else {
                      lookupaddress = "";
                  }             
                %>

            }
        </script> 
   <script language="javascript">
            function getAddressFirst() {
                    document.getElementById('register-form').submit();
            }
        </script> 
        
          <script language="javascript">
            function start() {
                    checkReg(); 
                    getAddress();
            }
        </script> 
    </head>
    <body onload="start();">

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
                    

                    <form id="register-form" class="register-form" method="POST" action="PageRouter.do">
                        <input type="text" name="name" placeholder="name"/>
                        
                        <input id="postcode" type="text" name="postcode" placeholder="postcode"/>
                        <input id="houseno" type="text" name="houseno" placeholder="house no"/>
                        
                        <input type="submit" name="action" value="getaddress"/>
                        
                        <input id="fulladdress" type="text" name="address" value="<%=lookupaddress%>"/>
                                           
                        <p class="label">Date of birth</p>
                        <input type="date" name="dob" placeholder="date of birth"/>
                        <p class="label">Registration date</p>
                        <input type="date" name="registrationdate" placeholder="registration date"/>
                        <input id="registersubmit" class="button" type="submit" name="action" value="register"/>
                        </br>         
                        <p class="message">Already registered? <a href="#">Sign In</a></p>
                    </form>
                    
                    
                    <form class="login-form" method="POST" action="PageRouter.do">
                        <input type="text" name="username" placeholder="username"/>
                        <input type="password" name="password" placeholder="password"/>
                        <input class="button" type="submit" name="action" value="Login"/>
                        <label><input type="checkbox" name="admin" value="yes">Admin</label>
                        <p class="message">Not registered? <a href="#">Create an account</a></p>
                    </form>
                </div>
            </div>
        </div>
                    
                    <form id="getaddress" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="getaddress"/></form>
                    
        <script src="js/login_helper.js"></script>
        <script src="js/login.js"></script>
    </body>
</html>
