<%@include file="/WEB-INF/header.jsp" %>
        <h1>Your account's information</h1>
        
        <table>
            <tr>
                <th>Username</th><td>${username}</td>
            </tr>
            <tr>
                <th>Password</th><td>${password}</td>
            </tr>
            <tr>
                <th>Email</th><td>${email}</td>
            </tr>
            <tr>
                <th>First name</th><td>${firstname}</td>
            </tr>
            <tr>
                <th>Last name</th><td>${lastname}</td>
            </tr>
            <tr>
                <th>Phone no.</th><td>${phonenumber}</td>
            </tr>
            <tr>
                <th>Gender</th><td>${gender}</td>
            </tr>
        </table>
            
        <form action="Account" method="post">
            <input type="hidden" name="action" value="edit"/>
            <input type="submit" value="Edit info"/>
        </form>
            
                <c:choose>
                    <c:when test="${isSubscribed}">
                        <form action="Account" method="post">
                            <label>If you no longer wish to receive emails about note collaboration, please click "Unsubscribe"</label>
                            <br/>
                            <input type="hidden" name="action" value="unsubscribe"/>
                            <input type="submit" value="Unsubscribe "/>
                        </form>
                    </c:when>
                    
                    <c:when test="${!isSubscribed}">
                        <form action="Account" method="post">
                            <label>If you wish to receive emails about note collaboration, please click "Subscribe"</label>
                            <br/>
                            <input type="hidden" name="action" value="subscribe"/>
                            <input type="submit" value="Subscribe "/>
                        </form>
                    </c:when>
                </c:choose>
            

            
        <div>
            ${serverResponse}
        </div>
    </body>
</html>
