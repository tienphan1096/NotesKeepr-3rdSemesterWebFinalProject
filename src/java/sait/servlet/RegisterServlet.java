/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import sait.businesslogic.WebMailService;
import sait.dataaccess.ActivationRepository;
import sait.dataaccess.SubscriptionRepository;
import sait.dataaccess.UserRepository;
import sait.domainmodel.Activation;
import sait.domainmodel.Subscription;
import sait.domainmodel.User;
import sait.utilities.GlobalVariables;

/**
 *
 * @author Tien Phan
 */
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        
        getServletContext().getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String email=request.getParameter("email");
        String firstname=request.getParameter("firstname");
        String lastname=request.getParameter("lastname");
        String phonenumber=request.getParameter("phonenumber");
        String genderString=request.getParameter("gender");
        
        if (username!=null&&
                password!=null&&
                email!=null&&
                firstname!=null&&
                lastname!=null&&
                phonenumber!=null&&
                genderString!=null&&
                !username.equals("")&&
                !password.equals("")&&
                !email.equals("")&&
                !firstname.equals("")&&
                !lastname.equals("")&&
                !phonenumber.equals("")&&
                !genderString.equals("")){
            
            char gender=genderString.charAt(0);
            
            UserRepository ur=new UserRepository();
            
            User user=new User(username, password, email, firstname, lastname, phonenumber, gender);
            
            ur.insert(user);
            
            //Subscribe user
            user=ur.getUnactivatedOrActivatedUser(username);
            SubscriptionRepository sr = new SubscriptionRepository();
            Subscription subscription = new Subscription(username);
            sr.insert(subscription);
            
            user.setSubscription(subscription);
            ur.update(user);
            
            //Start activation process
            String uuid=UUID.randomUUID().toString();
            Activation activation=new Activation(username, uuid);
            ActivationRepository ar=new ActivationRepository();
            ar.insert(activation);
            
            user = ur.getUnactivatedOrActivatedUser(username);
            user.setActivation(activation);
            
            ur.update(user);
            
            sendRegistrationEmail(activation);
            
            request.setAttribute("serverResponse", "Thank you for signing up! Please check your email to activate the newly created account!");
            
            getServletContext().getRequestDispatcher("/WEB-INF/login.jsp").forward(request, response);
        }
        
    }

    private void sendRegistrationEmail(Activation activation) {
        
        String username=activation.getUsername();
        String uuid=activation.getUuid();
        
        UserRepository ur=new UserRepository();
        User user=ur.getUnactivatedOrActivatedUser(username);
        
        String link=GlobalVariables.PROJECT_PATH + "activate?uuid=" +uuid;
        
        HashMap<String, String> emailParameters=new HashMap<>();
        emailParameters.put("username", username);
        emailParameters.put("firstname", user.getFirstname());
        emailParameters.put("lastname", user.getLastname());
        emailParameters.put("uuid", uuid);
        emailParameters.put("link", link);
        
        String template=getServletContext().getRealPath("/WEB-INF/emailtemplates/registrationEmail.html");
        
        try {
            WebMailService.sendMail(user.getEmail(), "NotesKeepr - Account activation for "+username, template, emailParameters);
        } catch (IOException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NamingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
