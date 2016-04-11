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
    </head>
    <body>
        
            <% if(request.getSession().getAttribute("username") == null) {
                    request.setAttribute("message", "Timed out. Please log in again.");     
                    request.getRequestDispatcher("/index_user_login.jsp").forward(request, response);
                }        
            %>
        
        <div class="user-dashboard-container">    
            <h1><%= (String)request.getAttribute("message") %></h1>

            <div class="user-dashboard">
                <form id="routerform" method="POST" action="PageRouter.do">
                    <ul>                      
                        <li onclick="document.getElementById('routerform').submit();" class="user-button make-claim">Make Claim <input type="hidden" name="action" value="makeclaim" /></li>              
                        <li value="makepayment" class="user-button make-payment">Make Payment</li>                    
                    </ul>
                    <ul>
                        <li class="user-button claim-history">Claim History</li>                
                        <li class="user-button payment-history">Payment History</li>
                    </ul>
                </form>
            </div>
        </div>
            
    </body>
</html>
