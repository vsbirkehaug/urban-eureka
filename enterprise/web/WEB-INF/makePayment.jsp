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

    <script>
        function initialiseflag()
        {
            var flag=0;
        }
    </script>
</head>
<body onload="initialiseflag();">

<%!
    public void executeTest() {

        java.util.Date d = new java.util.Date();
        System.out.println(d.toString()); }
%>


<div class="wrapper">
    <h1>XYZ Drivers Association</h1>

    <div class="make-payment-page">
        <div class="form">

            <h2>New payment</h2>
            </br>

            <form id="make-payment-form" method="POST" action="MakePayment.do">
                <p>Outstanding charges</p>
                <table class="responsetable">
                    <tr class="chargerowheader">
                        <th class="chargetd">&lt;select charge&gt;</th>
                        <th class="chargetd">  Amount  </th>
                        <th class="chargetd">  Note  </th>
                    </tr>

                    <c:forEach var="charge" items="${list}">
                        <tr class="chargerow">
                            <td class="chargetd"><div display="inline"><input onclick="check()" display="inline" type="radio" name="chargeId" value="${charge.id}" required="true"></div></td>
                            <td class="chargetd">${charge.amount}</td>
                            <td class="chargetd">${charge.note}</td>
                        </tr>
                    </c:forEach>
                </table>


                </br>
                </br>

                <select class="option-button" name="paymenttype">
                    <option class="option-button" value="CARD">CARD</option>
                    <option class="option-button" value="CASH">CASH</option>
                </select>

                <input type="hidden" name="action" value="submitpayment" />

            </form>
            <form id="cancel-make-payment-form" method="POST" action="MakePayment.do">
                <input type="hidden" name="action" value="cancel" />
            </form>

            <ul class="choice">
                <li onclick="document.getElementById('cancel-make-payment-form').submit();" class="user-button cancel">Cancel</li>
                <li id="submitbutton" onclick="submitform();" class="user-button submit">Submit Payment</li>
                <div style="clear:both"></div>
            </ul>

        </div>
    </div>
</div>

<script> function check(){flag=1;} </script>
<script> function submitform() { if(flag===1) { document.getElementById('make-payment-form').submit(); } } </script>
</body>
</html>
