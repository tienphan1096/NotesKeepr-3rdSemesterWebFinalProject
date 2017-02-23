/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sait.businesslogic.WebMailService;
import sait.dataaccess.ActivationRepository;
import sait.dataaccess.UserRepository;
import sait.domainmodel.Activation;
import sait.domainmodel.User;
import sait.utilities.GlobalVariables;

/**
 *
 * @author Tien Phan
 */
public class ActivateServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String uuid=request.getParameter("uuid");
        
        ActivationRepository ar=new ActivationRepository();
        Activation activation= ar.getActivation(uuid);
        
        if (activation!=null){
            
            ar.delete(activation);
            
            UserRepository ur=new UserRepository();
            User user=ur.getUnactivatedOrActivatedUser(activation.getUsername());
            user.setActivation(null);
            ur.update(user);
            
            sendWelcomeEmail(activation);
            
            request.setAttribute("serverResponse", "Thank you for signing up! Your account is now active.");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }else{
            request.setAttribute("serverResponse", "There was an error activating your account. Please try again!");
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    private void sendWelcomeEmail(Activation activation) {
        
        String username = activation.getUsername();

        UserRepository ur = new UserRepository();
        User user = ur.getUser(username);

        HashMap<String, String> emailParameters = new HashMap<>();
        emailParameters.put("firstname", user.getFirstname());
        emailParameters.put("lastname", user.getLastname());

        String template = getServletContext().getRealPath("/WEB-INF/emailtemplates/welcomeEmail.html");

        try {
            WebMailService.sendMail(user.getEmail(), "NotesKeepr - Welcome to NotesKeeper, " + user.getFirstname(), template, emailParameters);
        } catch (IOException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
