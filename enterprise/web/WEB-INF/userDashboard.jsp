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
        <title>Dashboard</title>
        <link rel="stylesheet" href="css/style.css">

        <%  response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
            response.addHeader("Pragma", "no-cache");
            response.addDateHeader("Expires", 0);
            
        %>
    </head>
    <body>

        <% if (request.getSession().getAttribute("username") == null) {
                request.setAttribute("message", "Timed out. Please log in again.");
                request.getRequestDispatcher("/index_user_login.jsp").forward(request, response);
            }
        %>

        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="user-dashboard-container">    

                <div>

                    <form class="logout" method="POST" action="Logout.do">
                        <input class="logout" type="submit" name="action" value="logout"</input>
                    </form>
                    <p class="helloname"><%= "Hello, " + (String) request.getSession().getAttribute("name") + "!"%></p>  
                </div>
                <div style="clear:both"></div>

                <div class="user-dashboard">
                    <form id="paymentform" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="makepayment" /></form>
                    <form id="routerform" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="makeclaim" /></form>
                    <form id="routerform2" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="paymenthistory" /></form>
                    <form id="routerform3" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="claimhistory" /></form>
                    <ul>                      
                        <li onclick="document.getElementById('routerform').submit();" class="user-button make-claim">Make Claim </li>              
                        <li onclick="document.getElementById('paymentform').submit();" class="user-button make-payment">Make Payment </li>                    
                    </ul>
                    <ul>                               
                        <li onclick="document.getElementById('routerform3').submit();" class="user-button payment-history">Claim History</li>    
                        <li onclick="document.getElementById('routerform2').submit();" class="user-button payment-history">Payment History</li>      
                    </ul>
                </div>
            </div>
        </div> 
    </body>
</html>
