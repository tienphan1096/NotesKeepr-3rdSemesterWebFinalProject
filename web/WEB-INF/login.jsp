<%-- 
    Document   : login
    Created on : Nov 7, 2016, 2:12:32 PM
    Author     : Tien Phan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NotesKeepr - Log In</title>
    </head>
    <body>
        <h1>NotesKeepr</h1>
        
        <form action="Login" method="post">
            <input type="text" name="username" placeholder="Username"/>
            <input type="password" name="password" placeholder="Password"/>
            <input type="submit" value="Submit" name="submit"/>
        </form>
        
        <div>${serverResponse}</div>
        
        <a href="Register">First time user? Register here!</a><br/>
        <a href="ForgotPassword">Forgot password? Click here!</a>
    </body>
</html>
