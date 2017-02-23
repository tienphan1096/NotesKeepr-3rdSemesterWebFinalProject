<%@include file="/WEB-INF/header.jsp" %>
        <h1>Manage users</h1>

        <c:choose>
            <c:when test="${selectedUser!=null}">
                
                <h2>Edit user...</h2>
                <form action="?action=save" method="post">
                    <input type="text" name="username" readonly value="${selectedUser.getUsername()}"/><br/>
                    <input type="text" name="password" value="${selectedUser.getPassword()}"/><br/>
                    <input type="email" name="email" value="${selectedUser.getEmail()}"/><br/>
                    <input type="text" name="firstname" value="${selectedUser.getFirstname()}"/><br/>
                    <input type="text" name="lastname" value="${selectedUser.getLastname()}"/><br/>
                    <input type="text" name="phonenumber" value="${selectedUser.getPhonenumber()}"/><br/>
                    <label>Gender</label>
                    <select name="gender">
                        <option value="M" <c:if test="${selectedUser.getGender() eq 'M'}">selected</c:if>>Male</option>
                        <option value="F" <c:if test="${selectedUser.getGender() eq 'F'}">selected</c:if>>Female</option>
                        <option value="U" <c:if test="${selectedUser.getGender() eq 'U'}">selected</c:if>>Unknown</option>
                        <option value="N" <c:if test="${selectedUser.getGender() eq 'N'}">selected</c:if>>Non-applicable</option>

                    </select>
                    <input type="submit" name="submit" value="Save"/>
                </form>
            </c:when>
                
            <c:otherwise>
                
                <table>
                    <tr>
                        <th>Username</th>
                        <th>Password</th>
                        <th>Email</th>
                        <th>First name</th>
                        <th>Last name</th>
                        <th>Phone no.</th>
                        <th>Gender</th>
                    </tr>

                    <c:forEach var="user" items="${userList}" begin="0" end="${userList.size()}" varStatus="status">
                        <tr>
                            <!--<td><input type="radio" name="itemId" value="${status.count-1}"></td>-->
                            <td>${user.getUsername()}</td>
                            <td>${user.getPassword()}</td>
                            <td>${user.getEmail()}</td>
                            <td>${user.getFirstname()}</td>
                            <td>${user.getLastname()}</td>
                            <td>${user.getPhonenumber()}</td>
                            <td>${user.getGender()}</td>
                            <td>
                                <form action="?action=edit" method="post">
                                    <input type="hidden" name="username" value="${user.getUsername()}">
                                    <input type="submit" value="Edit">
                                </form>
                            </td>
                            <td>
                                <form action="?action=delete" method="post">
                                    <input type="hidden" name="username" value="${user.getUsername()}">
                                    <input type="submit" value="Delete">
                                </form>
                            </td>
                            <td>
                                <form action="?action=impersonate" method="post">
                                    <input type="hidden" name="username" value="${user.getUsername()}">
                                    <input type="submit" value="Impersonate">
                                </form>
                            </td>
                            
                            <c:choose>
                                <c:when test="${user.isAdmin()}">
                                    <td>
                                        <form action="?action=demote" method="post">
                                            <input type="hidden" name="username" value="${user.getUsername()}">
                                            <input type="submit" value="Demote">
                                        </form>
                                    </td>
                                </c:when>
                                
                                <c:when test="${!user.isAdmin()}">
                                    <td>
                                        <form action="?action=promote" method="post">
                                            <input type="hidden" name="username" value="${user.getUsername()}">
                                            <input type="submit" value="Promote">
                                        </form>
                                    </td>
                                </c:when>
                            </c:choose>
                            
                            
                        </tr>
                    </c:forEach>
                </table>
                
                <div>${serverResponse}</div>
                
                <h2>Add user</h2>
                <form action="?action=add" method="post">
                    <input type="text" name="username" placeholder="Username"/><br/>
                    <input type="text" name="password" placeholder="Password"/><br/>
                    <input type="email" name="email" placeholder="Email"/><br/>
                    <input type="text" name="firstname" placeholder="First name"/><br/>
                    <input type="text" name="lastname" placeholder="Last name"/><br/>
                    <input type="text" name="phonenumber" placeholder="Phone no."/><br/>
                    <label>Gender</label>
                    <select name="gender">
                        <option value="M">Male</option>
                        <option value="F">Female</option>
                        <option value="U">Unknown</option>
                        <option value="N">Non-applicable</option>
                    </select>
                    <input type="submit" name="submit" value="Add user"/>
                </form>
            </c:otherwise>
        </c:choose>
                
    </body>
</html>
