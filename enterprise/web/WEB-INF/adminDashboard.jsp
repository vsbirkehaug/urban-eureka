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

            try {
                if (request.getSession().getAttribute("username") == null || (String) request.getSession().getAttribute("role") != "admin") {
                    request.setAttribute("message", "Timed out. Please log in again.");
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }

        %>
    </head>
    <body>

        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="user-dashboard-container">    

                <div>
                    <h2 class="admin" >ADMIN DASHBOARD</h2>

                    <form class="logout" method="POST" action="PageRouter.do">
                        <input class="logout" type="submit" name="action" value="logout"</input>
                    </form>
                    <p class="helloname"><%= "Hello, " + (String) request.getSession().getAttribute("username") + "!"%></p>  
                </div>
                <div style="clear:both"></div>

                <div class="user-dashboard">
                    <form id="handleclaim" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="adminhandleclaims" /></form>
                    <form id="routerform" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="adminmakecharge" /></form>
                    <form id="routerform2" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="adminhandlecharges" /></form>
                    <form id="routerform3" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="adminclaimhistory" /></form>
                    <form id="routerform4" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="adminpaymenthistory" /></form>
                    <form id="routerform5" method="POST" action="PageRouter.do"><input type="hidden" name="action" value="adminmemberlist" /></form>
                    <ul>                      
                        <li onclick="document.getElementById('handleclaim').submit();" class="user-button make-claim">Pending Claims </li>              
                        <li onclick="document.getElementById('routerform').submit();" class="user-button make-payment">Make Charge</li>                    
                    </ul>
                    <ul> 
                        <li onclick="document.getElementById('routerform2').submit();" class="user-button make-claim">Pending Charge Payments </li>         
                    </ul>
                    <ul>
                        <li onclick="document.getElementById('routerform4').submit();" class="user-button payment-history">Payment History</li>                
                        <li onclick="document.getElementById('routerform3').submit();" class="user-button claim-history">Claim History</li>       
                    </ul>
                     <ul>
                        <li onclick="document.getElementById('routerform5').submit();" class="user-button claim-history">Member List</li>                   
                    </ul>
                    
                </div>
            </div>
        </div> 
    </body>
</html>
