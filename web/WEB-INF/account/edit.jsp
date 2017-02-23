<%@include file="/WEB-INF/header.jsp" %>
        <h1>Editing info...</h1>
        
        <form action="Account" method="post">
            <table>
                <tr>
                    <th>Username</th>
                    <td>
                        <input type="text" readonly value="${username}" name="username"/>
                    </td>
                </tr>
                <tr>
                    <th>Password</th>
                    <td>
                        <input type="text" value="${password}" name="password"/>
                    </td>
                </tr>
                <tr>
                    <th>Email</th>
                    <td>
                        <input type="text" value="${email}" name="email"/>
                    </td>
                </tr>
                <tr>
                    <th>First name</th>
                    <td>
                        <input type="text" value="${firstname}" name="firstname"/>
                    </td>
                </tr>
                <tr>
                    <th>Last name</th>
                    <td>
                        <input type="text" value="${lastname}" name="lastname"/>
                    </td>
                </tr>
                <tr>
                    <th>Phone no.</th>
                    <td>
                        <input type="text" value="${phonenumber}" name="phonenumber"/>
                    </td>
                </tr>
                <tr>
                    <th>Gender</th>
                    <td>
                        <input type="text" value="${gender}" name="gender"/>
                    </td>
                </tr>
            </table>

            <input type="hidden" name="action" value="save"/>
            <input type="submit" value="Save"/>
        </form>
    </body>
</html>