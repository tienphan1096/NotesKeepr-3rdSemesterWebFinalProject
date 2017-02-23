<%-- 
    Document   : registration.jsp
    Created on : Dec 2, 2016, 11:02:44 AM
    Author     : Tien Phan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NotesKeepr - Registration</title>
    </head>
    <body>
        <h1>NotesKeepr</h1>
        <h2>Registration</h2>
        
        <form action="Register" method="post">
            <input type="text" name="username" placeholder="Username"/><br/>
            <input type="text" name="password" placeholder="Password"/><br/>
            <input type="text" name="email" placeholder="Email"/><br/>
            <input type="text" name="firstname" placeholder="First name"/><br/>
            <input type="text" name="lastname" placeholder="Last name"/><br/>
            <input type="text" name="phonenumber" placeholder="Phone number"/><br/>
            <select name="gender">
                <option value="M">Male</option>
                <option value="F">Female</option>
                <option value="U">Unknown</option>
                <option value="N">Non-applicable</option>
            </select><br/>
            <input type="submit" name="Register"/>
        </form>
    </body>
</html>