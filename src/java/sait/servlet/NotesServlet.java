package sait.servlet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import sait.dataaccess.NoteRepository;
import sait.dataaccess.UserRepository;
import sait.domainmodel.Note;
import sait.domainmodel.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

/**
 *
 * @author Tien Phan
 */
public class NotesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        
        if (username!=null && !username.equals("")){
            String action = request.getParameter("action");
            if (action != null && !action.equals("")) {
                if (action.equals("view")) {
                    int noteID = Integer.parseInt(request.getParameter("noteID"));

                    NoteRepository nr = new NoteRepository();
                    Note selectedNote = nr.getNote(noteID);

                    if (selectedNote.getOwner().getUsername().equals(username)
                            //or collaborator can view the note too, but only when there's a collaborator.
                            || (selectedNote.getUserList().size()>0 && selectedNote.getUserList().get(0).getUsername().equals(username))) {
                        request.setAttribute("selectedNote", selectedNote);
                        
                        UserRepository ur=new UserRepository();
                        List<User> systemUserList=ur.getAll();
                        
                        request.setAttribute("systemUserList", systemUserList);
                    }
                }
            }

            UserRepository ur = new UserRepository();
            User user = ur.getUser(username);
            List<Note> usersNoteList = user.getNoteList();
            List<Note> collabNoteList = user.getCollabNoteList();

            request.setAttribute("usersNoteList", usersNoteList);
            request.setAttribute("collabNoteList", collabNoteList);

            getServletContext().getRequestDispatcher("/WEB-INF/notes.jsp").forward(request, response);
        }else{
            response.sendRedirect("");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action=request.getParameter("action");
        if (action!=null&&!action.equals("")){
            NoteRepository nr=new NoteRepository();
            UserRepository ur=new UserRepository();
            
            HttpSession session = request.getSession();

            String username = (String) session.getAttribute("username");
            User owner = ur.getUser(username);
            
            if (action.equals("add")){
                
                Note note=new Note(0, new Date(), "");
                note.setOwner(owner);
                
                nr.insert(note,owner);
                
                //Update owner so the new note is in the list, along with its ID.
                owner=ur.getUser(owner.getUsername());
                List<Note> noteList=owner.getNoteList();
                Note newNote=noteList.get(noteList.size()-1);
                
                response.sendRedirect("EditNote?noteID="+newNote.getNoteid()+"#"+newNote.getNoteid());
            }else if (action.equals("delete")){
                
                int noteID=Integer.parseInt(request.getParameter("noteID"));
                Note note=nr.getNote(noteID);
                if (note.getOwner().getUsername().equals(username)){
                    nr.delete(note);
                    
                    List<User> collabList=note.getUserList();
                    if (collabList.size()>0){
                        User collab=collabList.get(0);
                        collab.getCollabNoteList().remove(note);
                        
                        ur.update(collab);
                    }
                }

                
                response.sendRedirect("Notes");                
            }else if (action.equals("save")){
                
                int noteID = Integer.parseInt(request.getParameter("noteID"));
                Note note = nr.getNote(noteID);
                
                String collaboratorUsername = request.getParameter("collaborator");
                
                if (note.getOwner().getUsername().equals(username)) updateNoteForOwner(request, response);

            }
        }
    }

    private boolean isNewCollaboratorDifferentFromOld(Note note, String newCollaboratorUsername) {
        List<User> collabList=note.getUserList();
        
        if (newCollaboratorUsername.equals("")){

            if (collabList.size()==0) return false;
            else return true;
        }else{
            //Valid new collaborator's usrame
            //if old collab list doesn't contain anyone -> true
            //if old collab list contains a different person -> true
            
            if (collabList.size()>0 && collabList.get(0).getUsername().equals(newCollaboratorUsername)){
                return false;
            }else if (collabList.size()>0 && !collabList.get(0).getUsername().equals(newCollaboratorUsername)){
                return true;
            }else if (collabList.size()==0 && !newCollaboratorUsername.equals("")){
                return true;
            }else{
                throw new UnknownError("Cannot determine if new collab is different than old collab. Check isNewCollaboratorDifferentFromOld()");
            }
        }
    }
    
    private void removeNoteFromCollaboratorsNoteList(Note note){
        UserRepository ur=new UserRepository();
        
        User oldCollaborator = note.getUserList().get(0);
        List<Note> oldCollaboratorsNoteList = oldCollaborator.getCollabNoteList();
        oldCollaboratorsNoteList.remove(note);
        ur.update(oldCollaborator);
    }
    
    private void clearNotesCollaboratorList(Note note){
        NoteRepository nr=new NoteRepository();
        
        List<User> collabList = note.getUserList();
        collabList.clear();
        nr.update(note);
    }
    
    private void addNoteToCollaboratorsNoteList(Note note, User collaborator){
        UserRepository ur=new UserRepository();
        
        List<Note> collaboratorsNoteList = collaborator.getCollabNoteList();
        collaboratorsNoteList.add(note);
        ur.update(collaborator);
    }
    
    private void addNewCollaboratorToNotesCollaboratorList(Note note, User collaborator){
        List<User> collabList = note.getUserList();
        collabList.clear();
        collabList.add(collaborator);
    }

    private void updateNoteForOwner(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        HttpSession session = request.getSession();
        
        UserRepository ur=new UserRepository();
        NoteRepository nr=new NoteRepository();
        
        String ownername = (String) session.getAttribute("username");
        
        String collaboratorUsername = request.getParameter("collaborator");

        if (collaboratorUsername != null) {

            int noteID = Integer.parseInt(request.getParameter("noteID"));
            Note note = nr.getNote(noteID);

            if ((collaboratorUsername != null) && 
                    (!collaboratorUsername.equals(note.getOwner().getUsername())) && 
                    (isNewCollaboratorDifferentFromOld(note, collaboratorUsername))) {

                if (collaboratorUsername.equals("")) {

                    removeNoteFromCollaboratorsNoteList(note);
                    clearNotesCollaboratorList(note);
                } else {
                    User collaborator = ur.getUser(collaboratorUsername);

                    if (note.getUserList().size() > 0) {
                        removeNoteFromCollaboratorsNoteList(note);
                    } else {
                        // do nothing
                    }

                    addNoteToCollaboratorsNoteList(note, collaborator);
                    addNewCollaboratorToNotesCollaboratorList(note, collaborator);
                    
                    sendNotificationEmail(note, collaborator);
                }
            }

            if (note.getOwner().getUsername().equals(ownername)) {
                nr.update(note);
            }

            response.sendRedirect("Notes");
        } else {
            //TO DO figure out how to display error message
            response.sendRedirect("Notes");
        }
    }

    private void sendNotificationEmail(Note note, User collaborator) {
        
        if (collaborator.isSubscribed()){
            HashMap<String, String> emailParameters = new HashMap<>();
            emailParameters.put("ownerFirstName", note.getOwner().getFirstname());
            emailParameters.put("ownerLastName", note.getOwner().getLastname());
            emailParameters.put("firstname", collaborator.getFirstname());

            String template = getServletContext().getRealPath("/WEB-INF/emailtemplates/notificationEmail.html");

            try {
                WebMailService.sendMail(collaborator.getEmail(),
                        note.getOwner().getFirstname() + " " + note.getOwner().getLastname() + " has invited you to collaborate",
                        template,
                        emailParameters);
            } catch (IOException ex) {
                Logger.getLogger(NotesServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NamingException ex) {
                Logger.getLogger(NotesServlet.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(NotesServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

    }
}
