<%-- 
    Document   : index
    Created on : 09-Mar-2016, 16:52:19
    Author     : vilde
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix ="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dashboard</title>
        <link rel="stylesheet" href="css/style.css">
        <%  response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0"); 
            response.addHeader("Pragma", "no-cache"); 
            response.addDateHeader ("Expires", 0);
            %>
    </head>
    <body>
        
        <div class="wrapper">
        <h1>XYZ Drivers Association</h1>
      
        <div class="make-payment-page">          
        <div class="form">      
            
            <h2>New payment</h2>
            </br>
            
            <p>Outstanding charges</p>
            <table>
            <tr>
                    <th></th>
                    <th>Id</th>
                    <th>Amount</th>
                    <th>Note</th>
            </tr>

            <c:forEach var="charge" items="${list}">
                    <tr>
                            <td><input type="radio" name="chargeId" value="${charge.id}" required="true"></td>
                            <td>${charge.id}</td>
                            <td>${charge.amount}</td>
                            <td>${charge.note}</td>
                    </tr>
            </c:forEach>
            </table>
            
            <form id="make-payment-form" method="POST" action="MakePayment.do">
              <input type="hidden" name="action" value="submitpayment" />  
              </br>         

            </form>
            <form id="cancel-make-payment-form" method="POST" action="MakePayment.do">
                <input type="hidden" name="action" value="cancel" />
            </form>
            
            <ul class="choice">
                <li onclick="document.getElementById('cancel-make-payment-form').submit();" class="user-button cancel">Cancel</li>  
                <li onclick="document.getElementById('make-payment-form').submit();" class="user-button submit">Submit Payment</li>
                <div style="clear:both"></div>
            </ul>
          
        </div>
      </div>
        </div>
    </body>
</html>
