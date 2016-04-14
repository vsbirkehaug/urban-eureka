<%-- 
    Document   : index
    Created on : 09-Mar-2016, 16:52:19
    Author     : vilde
--%>

<%@page import="java.util.List"%>
<%@page import="model.SimpleMember"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link rel="stylesheet" href="css/style.css">
        <%  response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
            response.addHeader("Pragma", "no-cache");
            response.addDateHeader("Expires", 0);
        %>
    </head>
    <body>

        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="make-claim-page">          
                <div class="form">      

                    <h2>New charge</h2>
                    </br>
                    <form id="make-charge-form" method="POST" action="PageRouter.do">

                        <select class="option-button" name="member">
                            <c:forEach var="simplemember" items="${list}">
                                <option value="${simplemember.id}">${simplemember.id} - ${simplemember.name}</option>
                            </c:forEach>
                        </select>

                        </br>   </br>  

                        <input type="number" step="0.01" min=0 name="amount" placeholder="amount"/>

                        <select class="option-button" name="chargetype">   
                            <c:forEach var="chargetype" items="${chargelist}">
                                <option >${chargetype}</option>
                            </c:forEach> 
                        </select>

                        <p class="greentext">
                            <% if (request.getAttribute("rowschanged") != null) {
                            %>
                            </br>  <%=("Rows changed: " + (String) request.getAttribute("rowschanged"))%> </br>
                            <%
                        }%>
                        </p>

                        <input type="hidden" name="action" value="submitcharge" />  
                        </br>         

                    </form>
                    <form id="cancel-make-charge-form" method="POST" action="PageRouter.do">
                        <input type="hidden" name="action" value="admindashboard" />
                    </form>

                    <ul class="choice">
                        <li onclick="document.getElementById('cancel-make-charge-form').submit();" class="user-button dashboard">Dashboard</li>  
                        <li onclick="document.getElementById('make-charge-form').submit();" class="user-button submit">Submit Charge</li>
                        <div style="clear:both"></div>
                    </ul>

                </div>
            </div>
        </div>
    </body>
</html>
