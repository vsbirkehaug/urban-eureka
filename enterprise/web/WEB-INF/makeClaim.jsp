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
        <div class="make-claim-page">
        <div class="form">      
            
          <form class="make-claim-form" method="POST" action="MakeClaim.do">
            <p class="label"
            <input type="date" name="date" placeholder="date"/>
            <input type="text" name="rationale" placeholder="rationale"/>
            <input type="number" step="0.01" min=0 name="amount" placeholder="amount"/>
            <input type="hidden" name="status" placeholder="pending"/>
            <input class="button" type="submit" name="makeclaim" value="Submit"/>
            </br>         
          
          </form>
          
        </div>
      </div>
    </body>
</html>
