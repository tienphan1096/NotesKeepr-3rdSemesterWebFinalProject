/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.businesslogic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Tien Phan
 */
public class WebMailService {
    public static void sendMail(String to, String subject, String template, HashMap<String, String> contents) throws IOException, NamingException, MessagingException{
        
        String body=new String(Files.readAllBytes(Paths.get(template)));
        
        for (String key:contents.keySet()){
            body=body.replace("{{"+key+"}}", contents.get(key));
        }
        
        sendMail(to, subject, body, true);
        
    }
    
    public static void sendMail(String to, String subject, String body, boolean isHTML) throws NamingException, AddressException, MessagingException{
        
        Context env= (Context) new InitialContext().lookup("java:comp/env");
        String username=(String) env.lookup("webmail-username");
        String password=(String) env.lookup("webmail-password");
        
        Properties props=new Properties();
        props.put("mail.transport.protocol", "smtps");
        props.put("mail.smtps.host", "smtp.gmail.com");
        props.put("mail.smtps.port", 465);
        props.put("mail.smtps.auth", "true");
        props.put("mail.stmps.quitwait", "false");
        Session session=Session.getDefaultInstance(props);
        session.setDebug(true);
        
        Message message=new MimeMessage(session);
        
        Address fromAddress=new InternetAddress("cprg253tien@gmail.com");//the actual email address is declared in the web.xml!!!
        Address toAddress=new InternetAddress(to);
        message.setFrom(fromAddress);
        message.setRecipient(Message.RecipientType.TO, toAddress);
        message.setSubject(subject);
        if (isHTML){
            message.setContent(body, "text/html");
        }else{
            message.setText(body);
        }
        
        Transport transport = session.getTransport();
        transport.connect(username, password);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();        
        
    }
}
