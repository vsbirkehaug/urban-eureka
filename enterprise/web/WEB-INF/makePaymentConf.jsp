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
        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="make-claim-page">
                <div class="form">      


                    <h2>Payment confirmation</h2>
                    </br>
                    <p><%= session.getAttribute("paymentmessage")%></p>
                    </br>
                    <% if (session.getAttribute("paymentid") != null) {
                        %>
                        <p>Your payment id: <%= session.getAttribute("paymentid")%></p
                        <p>Payment amount: £<%= session.getAttribute("paymentamount")%></p>
                        <p>Payment type: <%= session.getAttribute("paymenttype")%></p>
                        </br>
                        <p>Note: <%= session.getAttribute("chargenote")%></p>
                        <p>Charge status: <%= session.getAttribute("chargestatus")%></p>
                    <%
                        }%>
                    </br>

                    <form id="cancel-make-claim-form" method="POST" action="PageRouter.do">
                        <input type="hidden" name="action" value="dashboard" />
                    </form>

                    <form id="routerform" method="POST" action="PageRouter.do">  
                        <input type="hidden" name="action" value="makepayment" />
                    </form>

                    <ul class="choice">
                        <li onclick="document.getElementById('cancel-make-claim-form').submit();" class="user-button dashboard">Dashboard</li>  
                        <li onclick="document.getElementById('routerform').submit();" class="user-button submit">Make payment</li>
                        <div style="clear:both"></div>
                    </ul>

                </div>
            </div>
        </div>
    </body>
</html>
