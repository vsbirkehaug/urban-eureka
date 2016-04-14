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


                <h2>Member details</h2>
                </br>

                <table class="paymenttable">
                    <th class="paymenttdsmall">  Id  </th>
                    <th class="paymenttdsmall">  Username  </th>
                    <th class="paymenttdsmall">  Name  </th>
                    <th class="paymenttd">  Address  </th>
                    <th class="paymenttd">  DOR  </th>
                    <th class="paymenttd">  DOR  </th>
                    <th class="paymenttdsmall">  Status  </th>
                    </tr>

                    <c:forEach var="member" items="${list}">
                        <script type="text/javascript">
                            var claims = "${member.status}";

                            var mycolour = "FFFFFF";

                            if (claims === "SUSPENDED") {
                                mycolour = "#F3B9C5";
                            }
                            if (claims === "ACTIVE") {
                                mycolour = "#C6EBB2";
                            }
                            if (claims === "REGISTERED") {
                                mycolour = "#FFE6C2";
                            }
                            document.writeln("<tr class=\"paymenttr\" bgcolor=\"" + mycolour + "\">");
                        </script>  
                        <td class="paymenttdsmall">${member.id}</td>
                        <td class="paymenttdsmall">${member.username}</td>
                        <td class="paymenttdsmall">${member.name}</td>
                        <td class="paymenttd">${member.address}</td>
                        <td class="paymenttd">${member.dob}</td>
                        <td class="paymenttd">${member.dor}</td>
                        <td class="paymenttdsmall">${member.status}</td>
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
