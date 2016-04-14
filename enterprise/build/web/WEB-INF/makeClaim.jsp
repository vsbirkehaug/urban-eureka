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
            response.addDateHeader ("Expires", 0);
            %>
    </head>
    <body>
        
        <div class="wrapper">
        <h1>XYZ Drivers Association</h1>
      
        <div class="make-claim-page">          
        <div class="form">      
            
            <h2>New claim</h2>
            </br>
            <form id="make-claim-form" method="POST" action="PageRouter.do">
              <p class="label">Claim date</p>
              <input type="date" name="date" placeholder="date"/>
              <input type="text" name="rationale" placeholder="rationale"/>
              <input type="number" step="0.01" min=0 name="amount" placeholder="amount"/>
              <input type="hidden" name="action" value="insertclaim" />  
              </br>         

            </form>
            <form id="cancel-make-claim-form" method="POST" action="PageRouter.do">
                <input type="hidden" name="action" value="dashboard" />
            </form>
            
            <ul class="choice">
                <li onclick="document.getElementById('cancel-make-claim-form').submit();" class="user-button dashboard">Dashboard</li>  
                <li onclick="document.getElementById('make-claim-form').submit();" class="user-button submit">Submit Claim</li>
                <div style="clear:both"></div>
            </ul>
          
        </div>
      </div>
        </div>
    </body>
</html>
