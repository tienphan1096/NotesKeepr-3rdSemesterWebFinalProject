package sait.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import sait.dataaccess.DBUtil;
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
import sait.dataaccess.SubscriptionRepository;
import sait.domainmodel.Subscription;


public class AccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String test=request.getParameter("testVariable");
        System.out.println("Test variable: "+test);
        
        System.out.println("Requested URL "+request.getRequestURI());
        
        String requestedPath=request.getRequestURI();
        
        if (requestedPath.equals("/FinalProject/Account")){
            String username=(String) request.getSession().getAttribute("username");
            
            if (username!=null && !username.equals("")){
                UserRepository ur = new UserRepository();
                User user=ur.getUser(username);
                if (user!=null){
                    request.setAttribute("username", user.getUsername());
                    request.setAttribute("password", user.getPassword());
                    request.setAttribute("email", user.getEmail());
                    request.setAttribute("firstname", user.getFirstname());
                    request.setAttribute("lastname", user.getLastname());
                    request.setAttribute("phonenumber", user.getPhonenumber());
                    request.setAttribute("gender", user.getGender());
                    
                    getServletContext().getRequestDispatcher("/WEB-INF/account/view.jsp").forward(request, response);
                }
            }else{
                response.sendRedirect("/FinalProject/Login");
            }
            
        }else{
            String action = request.getParameter("action");
            if (action != null) {
                if (action.equals("logout")) {
                    HttpSession session = request.getSession();
                    session.invalidate();
                    request.setAttribute("serverResponse", "You've been successfully logged out!");
                }
            }

            UserRepository ur = new UserRepository();

            String username = (String) request.getSession().getAttribute("username");

            if (username != null && !username.equals("")) {

                User user = ur.getUser(username);

                if (user.isAdmin()) {
                    response.sendRedirect("admin.jsp");
                } else {
                    response.sendRedirect("Notes");
                }

            }else{
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String requestedPath=request.getRequestURI();
        
        if (requestedPath.equals("/FinalProject/Account")){
            String action=request.getParameter("action");
            
            System.out.println("Account action: "+action);
            
            if (action!=null&&!action.equals("")){
                
                if (action.equals("edit")){
                    String username = (String) request.getSession().getAttribute("username");

                    if (username != null && !username.equals("")) {
                        UserRepository ur = new UserRepository();
                        User user = ur.getUser(username);
                        if (user != null) {
                            request.setAttribute("username", user.getUsername());
                            request.setAttribute("password", user.getPassword());
                            request.setAttribute("email", user.getEmail());
                            request.setAttribute("firstname", user.getFirstname());
                            request.setAttribute("lastname", user.getLastname());
                            request.setAttribute("phonenumber", user.getPhonenumber());
                            request.setAttribute("gender", user.getGender());

                            getServletContext().getRequestDispatcher("/WEB-INF/account/edit.jsp").forward(request, response);
                        }
                    }
                }else if (action.equals("save")){
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String email = request.getParameter("email");
                    String firstname = request.getParameter("firstname");
                    String lastname = request.getParameter("lastname");
                    String phonenumber = request.getParameter("phonenumber");
                    
                    System.out.println("Phone: " + request.getParameter("phonenumber"));
                    System.out.println("Gender: " + request.getParameter("gender"));
                    char gender = request.getParameter("gender").charAt(0);

                    User user = new User(username, password, email, firstname, lastname, phonenumber, gender);

                    UserRepository ur = new UserRepository();
                    
                    List<Note> noteList=ur.getUser(username).getNoteList();
                    List<Role> roleList=ur.getUser(username).getRoleList();
                    user.setNoteList(noteList);
                    user.setRoleList(roleList);
                    
                    try {
                        ur.update(user);

                        request.setAttribute("serverResponse", "Info updated succesfully!");
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                        request.setAttribute("serverResponse", "Error updating user, contact admin!");
                    }
                    
                    user=ur.getUser(username);
                    
                    request.setAttribute("username", user.getUsername());
                    request.setAttribute("password", user.getPassword());
                    request.setAttribute("email", user.getEmail());
                    request.setAttribute("firstname", user.getFirstname());
                    request.setAttribute("lastname", user.getLastname());
                    request.setAttribute("phonenumber", user.getPhonenumber());
                    request.setAttribute("gender", user.getGender());
                    
                    getServletContext().getRequestDispatcher("/WEB-INF/account/view.jsp").forward(request, response);
                }else if (action.equals("unsubscribe")){
                    String username = (String) request.getSession().getAttribute("username");
                   
                    UserRepository ur=new UserRepository();
                    User user=ur.getUser(username);
                    
                    Subscription subscription=user.getSubscription();
                    
                    SubscriptionRepository unr=new SubscriptionRepository();
                    unr.delete(subscription);
                    
                    subscription=null;
                    user.setSubscription(subscription);
                    ur.update(user);
                    
                    request.setAttribute("username", user.getUsername());
                    request.setAttribute("password", user.getPassword());
                    request.setAttribute("email", user.getEmail());
                    request.setAttribute("firstname", user.getFirstname());
                    request.setAttribute("lastname", user.getLastname());
                    request.setAttribute("phonenumber", user.getPhonenumber());
                    request.setAttribute("gender", user.getGender());
                    
                    request.setAttribute("serverResponse", "You have been unsubscribed!");
                    
                    getServletContext().getRequestDispatcher("/WEB-INF/account/view.jsp").forward(request, response);
                }else if (action.equals("subscribe")){
                    String username = (String) request.getSession().getAttribute("username");
                   
                    UserRepository ur=new UserRepository();
                    User user=ur.getUser(username);
                    
                    Subscription subscription=new Subscription(user.getUsername());
                    
                    SubscriptionRepository unr=new SubscriptionRepository();
                    unr.insert(subscription);
                    
                    user.setSubscription(subscription);
                    ur.update(user);
                    
                    request.setAttribute("username", user.getUsername());
                    request.setAttribute("password", user.getPassword());
                    request.setAttribute("email", user.getEmail());
                    request.setAttribute("firstname", user.getFirstname());
                    request.setAttribute("lastname", user.getLastname());
                    request.setAttribute("phonenumber", user.getPhonenumber());
                    request.setAttribute("gender", user.getGender());
                    
                    request.setAttribute("serverResponse", "You have been subscribed!");
                    
                    getServletContext().getRequestDispatcher("/WEB-INF/account/view.jsp").forward(request, response);
                }
            }
        }else{
            UserRepository ur = new UserRepository();

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if (username != null && password != null && !username.equals("") && !password.equals("")) {

                try{
                    User user = ur.getUser(username);
                    if (user != null && user.getPassword().equals(password)) {
                        HttpSession session = request.getSession();

                        session.setAttribute("username", username);

                        if (user.isAdmin()) {
                            response.sendRedirect("admin.jsp");
                        } else {
                            response.sendRedirect("Notes");
                        }
                    } else {
                        request.setAttribute("serverResponse", "Invalid login credentials, please try again!");
                        getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                    }
                }catch(NullPointerException e){
                    request.setAttribute("serverResponse", "Invalid login credentials, please try again!");
                    getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("serverResponse", "Please fill in both fields");
                getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
            }
        }

    }
}
