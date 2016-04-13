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
            response.addDateHeader("Expires", 0);
        %>

        <script>
            function initialiseflag()
            {
                var flag = 0;
            }
        </script>
    </head>
    <body onload="initialiseflag();">

        <div class="wrapper">
            <h1>XYZ Drivers Association</h1>

            <div class="list-page">


                <h2>Handle pending claims</h2>
                </br>

                <form id="claim-status-form" method="POST" action="PageRouter.do">
                    <table class="responsetable">
                        <tr class="chargerowheader">
                            <th class="chargetd">&lt;select&gt;</th>                               
                            <th class="chargetd">  Member  </th>
                            <th class="chargetd">  Amount  </th>
                            <th class="chargetd">  Rationale  </th>    
                            <th class="chargetd">  Approved claims this year  </th>
                            <th class="chargetd">  Declined claims this year </th>
                            <th class="chargetd">  Pending claims this year </th>
                   
                        </tr>


                        <c:forEach var="claim" items="${list}">
                            <tr class="chargerow">
                                <td class="chargetd"><div display="inline"><input onclick="check()" display="inline" type="radio" name="claimId" value="${claim.id}" required="true"></div></td>
                                <td class="chargetd">${claim.name}</td>
                                <td class="chargetd">${claim.amount}</td>
                                <td class="chargetd">${claim.rationale}</td>         
                                <td class="chargetd">${claim.approved}</td>
                                <td class="chargetd">${claim.declined}</td>
                                <td class="chargetd">${claim.pending}</td>             
                            </tr>
                        </c:forEach>

                    </table>

                    </br>
                    </br>

                    <input type="hidden" name="action" value="submitclaimchange" />
                    <input id="claimstatus" type="hidden" name="status" value="APPROVED" />        


                </form>
                <form id="cancel-form" method="POST" action="PageRouter.do">
                    <input type="hidden" name="action" value="admindashboard" />
                </form>
                <p class="greentext">
                    <% if (request.getAttribute("rowschanged") != null) {
                    %>
                    <%=("Rows changed: " + (String) request.getAttribute("rowschanged"))%> </br></br>
                    <%
                        }%>
                </p>
                <ul class="choice">
                    <li onclick="document.getElementById('cancel-form').submit();" class="user-button dashboard">Dashboard</li>
                    <li id="submitbutton" onclick="submitformDeclined();" class="user-button cancel">Set status DECLINED</li>
                    <li id="submitbutton2" onclick="submitformApproved();" class="user-button submit">Set status APPROVED</li>

                    <div style="clear:both"></div>
                </ul>


            </div>
        </div>

        <script> function check() {
                flag = 1;
            }</script>

        <script> function submitformApproved() {
                if (flag === 1) {
                    document.getElementById('claimstatus').value = "APPROVED";
                    document.getElementById('claim-status-form').submit();
                }
            }</script>

        <script> function submitformDeclined() {
                if (flag === 1) {
                    document.getElementById('claimstatus').value = "DECLINED";
                    document.getElementById('claim-status-form').submit();
                }
            }</script>
    </body>
</html>
