<%@include file="/WEB-INF/header.jsp" %>
        <h1>Your notes</h1>
        
        <c:choose>
            <c:when test="${selectedNote != null}">
                <h3>Edit Note</h3>
                <form action="Notes" method="post">
                    <label>Note ID: </label><input type="text" name="noteID" readonly value="${selectedNote.noteid}"/><br>
                    <label>Date Created: </label><input type="text" name="dateCreated" readonly value="${selectedNote.datecreated}"/><br>
                    
                    <c:choose>
                        <c:when test="${selectedNote.getOwner().getUsername().equals(username)}">
                            <label>Collaborator: </label>
                            <select name="collaborator">
                                <option value="">-- Undefined --</option>

                                <c:forEach var="systemUser" items="${systemUserList}">
                                    <c:choose>
                                        <c:when test="${!systemUser.getUsername().equals(username)}">
                                            <option value="${systemUser.getUsername()}" 
                                                    <c:choose>
                                                        <c:when test="${selectedNote.getUserList().size()>0 && selectedNote.getUserList().get(0).getUsername().equals(systemUser.getUsername())}">selected</c:when>
                                                    </c:choose>>
                                                ${systemUser.getUsername()}
                                            </option>
                                        </c:when>
                                    </c:choose>

                                </c:forEach>
                            </select>
                        </c:when>
                    </c:choose>
                    


                    <input type="submit" value="Save">
                    <input type="hidden" name="action" value="save">
                </form>
            </c:when>
                
            <c:otherwise>
                <h2>Add a note</h2>
                <form action="Notes?action=add" method="post">
                    <input type="submit" value="Add note"/>
                </form>
            </c:otherwise>
        </c:choose>

        
        <table>
            <tr>
                <th>Note ID</th>
                <th>Date created</th>
                <th>Contents</th>
            </tr>
            
            <c:forEach var="note" items="${usersNoteList}">
                <tr>
                    <td>${note.noteid}</td>
                    <td>${note.datecreated}</td>
                    <td>${note.contents}</td>
                    <td><a href="EditNote?noteID=${note.noteid}#${note.noteid}">Edit</a></td>
                    <td>
                        <form action="Notes" method="post">
                            <input type="submit" value="Delete"/>
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="noteID" value="${note.noteid}">
                        </form>
                    </td>
                    <td>
                        <form action="Notes" method="get">
                            <input type="submit" value="Share"/>
                            <input type="hidden" name="action" value="view">
                            <input type="hidden" name="noteID" value="${note.noteid}">
                        </form>
                    </td>
                </tr>
                
            </c:forEach>
        </table>
                
        <h1>Shared notes</h1>
        
        <table>
            <tr>
                <th>Note ID</th>
                <th>Date created</th>
                <th>Contents</th>
                <th>Owner</th>
            </tr>
            
            <c:forEach var="note" items="${collabNoteList}">
                <tr>
                    <td>${note.noteid}</td>
                    <td>${note.datecreated}</td>
                    <td>${note.contents}</td>
                    
                    <td>${note.getOwner().getUsername()}</td>
                    
                    <td><a href="EditNote?noteID=${note.noteid}#${note.noteid}">Edit</a></td>
                </tr>
            </c:forEach>
        </table>
        
    </body>
</html>
