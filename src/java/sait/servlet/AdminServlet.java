package sait.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import sait.businesslogic.AccountService;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import sait.dataaccess.UserRepository;
import sait.domainmodel.Note;
import sait.domainmodel.Role;
import sait.domainmodel.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sait.dataaccess.RoleRepository;

/**
 *
 * @author Tien Phan
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session=request.getSession();
        String username = (String) session.getAttribute("username");
        if (username!=null){
            
            UserRepository ur = new UserRepository();
            User user = ur.getUser(username);

            if (user.isAdmin()) {
                setRequestUserList(request);

                getServletContext().getRequestDispatcher("/WEB-INF/admin/users.jsp").forward(request, response);
            } else {
                response.sendRedirect("/Assignment4-NotesKeepr-Email/Notes");
            }
        }else{
            response.sendRedirect("/Assignment4-NotesKeepr-Email");
        }
    }
    
    public void setRequestUserList(HttpServletRequest request){
        UserRepository ur = new UserRepository();
        
        List<User> userList = ur.getAll();

        request.setAttribute("userList", userList);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action=request.getParameter("action");
        
        boolean forwardRequesetResponse = true;
        
        if (action!=null&&!action.equals("")){
            if (action.equals("edit")) {
                String username=request.getParameter("username");
                
                if (username!=null&!username.equals("")){
                    UserRepository ur=new UserRepository();
                    
                    User selectedUser=ur.getUser(username);
                    
                    request.setAttribute("selectedUser", selectedUser);
                }else{
                    request.setAttribute("serverResponse", "An invalid user has been selected, contact admin!");
                }
                
                forwardRequesetResponse=true;
            }else if (action.equals("save")){
                
                String username=request.getParameter("username");
                String password=request.getParameter("password");
                String email=request.getParameter("email");
                String firstname=request.getParameter("firstname");
                String lastname=request.getParameter("lastname");
                String phonenumber=request.getParameter("phonenumber");
                char gender=request.getParameter("gender").charAt(0);
                
                User newUser=new User(username, password, email, firstname, lastname, phonenumber, gender);
                
                UserRepository ur=new UserRepository();
                
                List<Note> noteList = ur.getUser(username).getNoteList();
                List<Role> roleList = ur.getUser(username).getRoleList();
                newUser.setNoteList(noteList);
                newUser.setRoleList(roleList);
                
                try{
                    ur.update(newUser);
                    request.setAttribute("serverResponse", "All changes have been saved for user!");
                }catch(Exception e){
                    e.printStackTrace();
                    request.setAttribute("serverResponse", "Error updating user, contact admin!");
                }
                
                forwardRequesetResponse=true;
                
            }else if (action.equals("add")){
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String email = request.getParameter("email");
                String firstname = request.getParameter("firstname");
                String lastname = request.getParameter("lastname");
                String phonenumber = request.getParameter("phonenumber");
                char gender = request.getParameter("gender").charAt(0);
                
                User newUser=new User(username, password, email, firstname, lastname, phonenumber, gender);
                
                UserRepository ur=new UserRepository();
                try{
                    ur.insert(newUser);
                    request.setAttribute("serverResponse", "New user added successfully!");
                }catch (Exception e){
                    request.setAttribute("serverResponse", "Error adding new user, contact admin!");
                }
                
                forwardRequesetResponse=true;
                
            }else if (action.equals("delete")){
                String username=request.getParameter("username");
                
                UserRepository ur=new UserRepository();
                User user=ur.getUser(username);
                
                if (!user.isAdmin()){
                    ur.delete(user);
                    request.setAttribute("serverResponse", "User deleted successfully!");
                }else{
                    request.setAttribute("serverResponse", "Error: cannot delete admin!");
                }
                
                forwardRequesetResponse=true;
                
            }else if (action.equals("impersonate")){
                
                String username = request.getParameter("username");
                
                AccountService as=new AccountService();
                if (as.impersonate(request, username)){
                    forwardRequesetResponse = false;
                }else{
                    forwardRequesetResponse = true;
                    
                    request.setAttribute("serverResponse", "Please choose another user other than yourself to impersonate.");
                    
                }
            }else if (action.equals("promote")){
                String usernameToPromote=request.getParameter("username");
                
                UserRepository ur=new UserRepository();
                User userToPromote=ur.getUser(usernameToPromote);
                
                RoleRepository rr=new RoleRepository();
                Role newRole=rr.getRole(1);
                
                addRoleToUser(newRole, userToPromote);
                addUserToRole(userToPromote, newRole);
                
                forwardRequesetResponse = true;
                
            }else if (action.equals("demote")){
                String usernameToDemote=request.getParameter("username");
                
                HttpSession session=request.getSession();
                String username=(String) session.getAttribute("username");
                
                System.out.println("Demote: "+username+" "+usernameToDemote);
                
                if (!username.equals(usernameToDemote)){
                    UserRepository ur = new UserRepository();
                    User userToDemote = ur.getUser(usernameToDemote);

                    Role role = userToDemote.getRoleList().get(0);

                    removeRoleFromUser(role, userToDemote);
                    removeUserFromRole(userToDemote, role);
                    
                }else{
                    request.setAttribute("serverResponse", "Please choose another user than yourself to demote.");
                }
                    
                forwardRequesetResponse = true;
            }
            
            if (forwardRequesetResponse){
                setRequestUserList(request);
                getServletContext().getRequestDispatcher("/WEB-INF/admin/users.jsp").forward(request, response);
            }else{
                response.sendRedirect("/FinalProject/Login");
            }            
        }
    }

    private void addRoleToUser(Role newRole, User userToPromote) {
        List<Role> roleList=userToPromote.getRoleList();
        roleList.add(newRole);
        
        UserRepository ur=new UserRepository();
        ur.update(userToPromote);
    }

    private void addUserToRole(User userToPromote, Role newRole) {
        List<User> userList=newRole.getUserList();
        userList.add(userToPromote);
        
        RoleRepository rr=new RoleRepository();
        rr.update(newRole);
    }

    private void removeRoleFromUser(Role role, User user) {
        user.getRoleList().remove(role);
        
        UserRepository ur = new UserRepository();
        ur.update(user);
    }

    private void removeUserFromRole(User user, Role role) {
        role.getUserList().remove(user);
        
        RoleRepository rr = new RoleRepository();
        rr.update(role);
    }
}
