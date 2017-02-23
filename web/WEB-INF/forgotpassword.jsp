<%-- 
    Document   : forgotpassword
    Created on : Dec 11, 2016, 1:36:46 AM
    Author     : Tien Phan
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NotesKeepr - Password Reset</title>
    </head>
    <body>
        <h1>Password Reset</h1>
        
        <c:choose>
            <c:when test="${requestNewPassword}">
                <p>Please enter a new password in the textbox below for your account "${username}"</p>

                <form action="ForgotPassword?action=saveNewPassword" method="post">
                    <input type="text" name="password" placeholder="New password">
                    <input type="text" name="uuid" hidden value="${uuid}">
                    <input type="submit" value="Submit">
                </form>
            </c:when>
            <c:otherwise>
                <p>Please enter your email in the textbox below to reset your password</p>

                <form action="ForgotPassword?action=submitEmail" method="post">
                    <input type="text" name="email" placeholder="Email">
                    <input type="submit" value="Submit">
                </form>
            </c:otherwise>
        </c:choose>
        
        ${serverResponse}
        
    </body>
</html>
