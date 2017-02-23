/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.businesslogic;

import sait.dataaccess.UserRepository;
import sait.domainmodel.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Tien Phan
 */
public class AccountService {
    //return true if personate successfully.
    public boolean impersonate(HttpServletRequest request, String username){

        HttpSession session = request.getSession();
        String currentUsername = (String) session.getAttribute("username");

        if (!username.equals(currentUsername)) {

            session.invalidate();

            session = request.getSession();

            session.setAttribute("username", username);
            
            //now send the notification email
            UserRepository ur=new UserRepository();
            User user=ur.getUser(username);
            String usersEmail=user.getEmail();
            
            String template= request.getServletContext().getRealPath("/WEB-INF/emailtemplates/helpemailtemplate.html");
            
            HashMap<String, String> hashMap=new HashMap<>();
            hashMap.put("firstname", user.getFirstname());
            hashMap.put("username", user.getUsername());
            
            try {
                WebMailService.sendMail(usersEmail, "NotesKeepr Notifications - Help is on the way!", template, hashMap);
            } catch (IOException ex) {
                Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(AccountService.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            return true;
        } else {
            return false;
        }
    }
}
