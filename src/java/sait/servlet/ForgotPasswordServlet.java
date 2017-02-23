/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sait.businesslogic.WebMailService;
import sait.dataaccess.PasswordResetRepository;
import sait.dataaccess.UserRepository;
import sait.domainmodel.PasswordReset;
import sait.domainmodel.User;
import sait.utilities.GlobalVariables;

/**
 *
 * @author Tien Phan
 */
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action=request.getParameter("action");
        if (action!=null){
            String uuid=request.getParameter("uuid");
            PasswordResetRepository prr=new PasswordResetRepository();
            PasswordReset passwordReset=prr.getPasswordResetWithUuid(uuid);
            
            if (passwordReset!=null){
                request.setAttribute("requestNewPassword", true);
                request.setAttribute("username", passwordReset.getUsername());
                request.setAttribute("uuid", passwordReset.getUuid());
                getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
            }else{
                request.setAttribute("serverResponse", "Please restart the password recovery process by enter your email");
                getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
            }


        }else{
            getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action=request.getParameter("action");
        
        if (action!=null){
            if (action.equals("submitEmail")){
                startResetPasswordProcess(request, response);
            }else if (action.equals("saveNewPassword")){
                saveNewPassword(request, response);
            }
        }
    }
    
    private User getUserWithEmail(String email){
        UserRepository ur = new UserRepository();
        List<User> userList = ur.getAll();

        for (User user : userList) {
            if (user.getEmail().equals(email)) return user;
        }
        
        return null;
    }

    private void sendResetPasswordEmail(User user, PasswordReset passwordReset) {
        
        String uuid=passwordReset.getUuid();
        String username=user.getUsername();
        
        String link=GlobalVariables.PROJECT_PATH + "ForgotPassword?action=reset&uuid=" +uuid;
        
        HashMap<String, String> emailParameters=new HashMap<>();
        emailParameters.put("username", username);
        emailParameters.put("firstname", user.getFirstname());
        emailParameters.put("lastname", user.getLastname());
        emailParameters.put("link", link);
        
        String template=getServletContext().getRealPath("/WEB-INF/emailtemplates/resetPasswordEmail.html");
        
        try {
            WebMailService.sendMail(user.getEmail(), "Reset password for your NotesKeepr account", template, emailParameters);
        } catch (IOException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startResetPasswordProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");

        User user = getUserWithEmail(email);

        if (user != null && user.getPasswordReset() == null) {

            String uuid = UUID.randomUUID().toString();

            PasswordReset passwordReset = new PasswordReset(user.getUsername(), uuid);
            PasswordResetRepository prr = new PasswordResetRepository();
            prr.insert(passwordReset);

            user.setPasswordReset(passwordReset);
            UserRepository ur = new UserRepository();
            ur.update(user);

            sendResetPasswordEmail(user, passwordReset);

            request.setAttribute("serverResponse", "Please check your email to start resetting your password!");
            getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
        } else if (user.getPasswordReset() != null) {
            request.setAttribute("serverResponse", "A password recovery email has been sent to this account.");
            getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
        } else {
            request.setAttribute("serverResponse", "This email has not been registered with us, please try again!");
            getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
        }
    }

    private void saveNewPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid=request.getParameter("uuid");
        
        PasswordResetRepository prr = new PasswordResetRepository();
        PasswordReset passwordReset = prr.getPasswordResetWithUuid(uuid);
        
        String password = request.getParameter("password");
        
        if (password!=null && !password.equals("")){


            UserRepository ur = new UserRepository();
            User user = ur.getUser(passwordReset.getUsername());
            
            user.setPassword(password);
            user.setPasswordReset(null);
            ur.update(user);
            
            prr.delete(passwordReset);
            
            request.setAttribute("serverResponse", "New password saved! Please try logging in.");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }else{
            request.setAttribute("serverResponse", "Please enter a new password");
            request.setAttribute("requestNewPassword", true);
            request.setAttribute("username", passwordReset.getUsername());
            request.setAttribute("uuid", passwordReset.getUuid());

            getServletContext().getRequestDispatcher("/WEB-INF/forgotpassword.jsp").forward(request, response);
        }
    }
}
