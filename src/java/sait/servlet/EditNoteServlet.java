/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import sait.dataaccess.NoteRepository;
import sait.dataaccess.UserRepository;
import sait.domainmodel.Note;
import sait.domainmodel.User;

/**
 *
 * @author Tien Phan
 */
public class EditNoteServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String noteIDString=request.getParameter("noteID");
        int noteID=Integer.parseInt(noteIDString);
        
        NoteRepository nr=new NoteRepository();
        Note note=nr.getNote(noteID);
        
        request.setAttribute("editNote", true);
        request.setAttribute("noteid", noteID);
        
        getServletContext().getRequestDispatcher("/WEB-INF/notes/editNote.jsp").forward(request, response);
        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action != null && !action.equals("")) {
            if (action.equals("save")) {
                String username = (String) request.getSession().getAttribute("username");
                String noteIDString = request.getParameter("noteID");
                int noteID = Integer.parseInt(noteIDString);

                UserRepository ur = new UserRepository();
                User user = ur.getUser(username);

                NoteRepository nr = new NoteRepository();
                Note note = nr.getNote(noteID);

                if (user.getUsername().equals(note.getOwner().getUsername()) || user.getUsername().equals(note.getUserList().get(0).getUsername())) {
                    String newContent = request.getParameter("content");
                    
                    note.setContents(newContent);
                    
                    try{
                        nr.update(note);
                    }catch(RollbackException|ConstraintViolationException e){
                        System.out.println("The exception was fine, it's just an empty note. Write something in it!");
                    }
                }
            }
        }
    }


}
