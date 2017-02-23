<%@page import="sait.domainmodel.User"%>
<%@page import="sait.dataaccess.UserRepository"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
    <head>
        <title>NotesKeepr</title>
        
        <c:choose>
            <c:when test="${editNote}">
        <!-- Firebase -->
        <script src="https://www.gstatic.com/firebasejs/3.3.0/firebase.js"></script>

        <!-- CodeMirror -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.17.0/codemirror.js"></script>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.17.0/codemirror.css" />

        <!-- Firepad -->
        <link rel="stylesheet" href="https://cdn.firebase.com/libs/firepad/1.4.0/firepad.css" />
        <script src="https://cdn.firebase.com/libs/firepad/1.4.0/firepad.min.js"></script>
        
        <style>
            html { height: 100%; }
            body { margin: 0; height: 100%; position: relative; }
            /* Height / width / positioning can be customized for your use case.
            For demo purposes, we make firepad fill the entire browser. */
            #firepad-container {
                width: 50%;
                height: 50%;
                border: 2px solid #c9c9c9;
                border-radius: 5px;
                margin: 0 auto;
                box-shadow: 5px 5px 5px #c9c9c9;
            }
        </style>
        
    </head>
    <body onload="init()">
            </c:when>
        
            <c:when test="${!editNote}">
    </head>
    <body>
            </c:when>
        </c:choose>
        
        <%
            
            boolean isUser=false;
            boolean isAdmin=false;
            boolean isSubscribed=false;
            
            String username=(String) request.getSession().getAttribute("username");
            if (username!=null && !username.equals("")){
                isUser=true;
                User user = new UserRepository().getUser(username);
                isAdmin=user.isAdmin();
                isSubscribed=user.isSubscribed();
            }
            
            request.setAttribute("isUser", isUser);
            request.setAttribute("isAdmin", isAdmin);
            request.setAttribute("isSubscribed", isSubscribed);
            request.setAttribute("username", username);

        %>
        
        <c:choose>
            <c:when test="${isUser==true}">
                <a style="margin-right: 10px" href="/FinalProject/Notes">NotesKeepr - My Notes</a>
<!--                <a style="margin-right: 10px" href="/FinalProject/Notes?">Shared Notes</a>-->


                <c:choose>
                    <c:when test="${isAdmin==true}">
                        <a style="margin-right: 10px" href="/FinalProject/admin.jsp">Menu</a>
                        <a style="margin-right: 10px" href="/FinalProject/admin/users">User manager</a>
                    </c:when>
                </c:choose>

                <a style="margin-right: 10px" href="/FinalProject/Account">Your account</a>
                <a style="margin-right: 10px" href="/FinalProject/Login?action=logout">Log out</a>
            </c:when>
        </c:choose>
