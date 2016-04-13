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
        <title>Payment history</title>
        <link rel="stylesheet" href="css/style.css">

        <%  response.addHeader("Cache-Control", "no-cache,no-store,private,must-revalidate,max-stale=0,post-check=0,pre-check=0");
            response.addHeader("Pragma", "no-cache");
            response.addDateHeader("Expires", 0);
        %>
    </head>
    <body>
        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="list-page">


                <h2>Claim history</h2>
                </br>

                <table class="paymenttable">
                    <tr class="paymenttr">
                        <th class="paymenttd">  Claim id  </th>
                        <th class="paymenttd">  Rationale  </th>
                        <th class="paymenttd">  Amount  </th>
                        <th class="paymenttd">  Status  </th>
                        <th class="paymenttd">  Date  </th>

                    </tr>

                    <c:forEach var="claim" items="${list}">
                        <tr class="paymenttr">
                            <td class="paymenttd">${claim.id}</td>
                            <td class="paymenttd">${claim.rationale}</td>
                            <td class="paymenttd">${claim.amount}</td>
                            <td class="paymenttd">${claim.status}</td>p
                            <td class="paymenttd">${claim.date}</td>
                        </tr>
                    </c:forEach>
                </table>

                </br>
                </br>
                <form id="routerform" method="POST" action="PageRouter.do">  
                    <input type="hidden" name="action" value="dashboard" />
                    <ul class="choice">
                        <li onclick="document.getElementById('routerform').submit();" class="user-button cancel fullwidth">Dashboard</li>  
                        <div style="clear:both"></div>
                    </ul>
                </form>      

            </div>
        </div>
    </body>
</html>
