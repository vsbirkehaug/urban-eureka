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


                <h2>Payment history</h2>
                </br>

                <table class="paymenttable">
                    <tr class="paymenttr">
                        <th class="paymenttdsmall">  Payment id  </th>
                        <th class="paymenttdsmall">  Amount  </th>
                        <th class="paymenttdsmall">  Payment type  </th>
                        <th class="paymenttd">  Note  </th>
                        <th class="paymenttd">  Date  </th>
                        <th class="paymenttd">  Status  </th>
                    </tr>

                    <c:forEach var="payment" items="${list}">
                        <script type="text/javascript">
                            var claims = "${payment.status}";

                            var mycolour = "F3B9C5";

                            if (claims === "DECLINED") {
                                mycolour = "#F3B9C5";
                            }
                            if (claims === "APPROVED") {
                                mycolour = "#C6EBB2";
                            }
                            if (claims === "PENDING") {
                                mycolour = "#FFE6C2";
                            }
                            document.writeln("<tr class=\"paymenttr\" bgcolor=\"" + mycolour + "\">");
                        </script>  
                        <td class="paymenttdsmall">${payment.id}</td>
                        <td class="paymenttdsmall">${payment.amount}</td>
                        <td class="paymenttdsmall">${payment.paymentType}</td>
                        <td class="paymenttd">${payment.note}</td>
                        <td class="paymenttd">${payment.date}</td>
                        <td class="paymenttd">${payment.status}</td>
                        </tr>
                    </c:forEach>
                </table>

                </br>
                </br>
                <form id="routerform" method="POST" action="PageRouter.do">  
                    <input type="hidden" name="action" value="admindashboard" />
                    <ul class="choice">
                        <li onclick="document.getElementById('routerform').submit();" class="user-button dashboard fullwidth">Dashboard</li>  
                        <div style="clear:both"></div>
                    </ul>
                </form>      

            </div>
        </div>
    </body>
</html>
