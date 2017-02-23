/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import sait.dataaccess.NoteRepository;
import sait.domainmodel.Note;

/**
 *
 * @author Tien Phan
 */
public class AjaxServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action.equals("get")){
            String noteIDString=request.getParameter("noteID");
            int noteID = Integer.parseInt(noteIDString);
            
            NoteRepository nr=new NoteRepository();
            Note note=nr.getNote(noteID);
            
            String content=note.getContents();
            
            request.setAttribute("content", content);
            
            getServletContext().getRequestDispatcher("/WEB-INF/ajax.jsp").forward(request, response);
            
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action.equals("save")) {
            String content = request.getParameter("content");

            System.out.println("Note content for AJAX saving: " + content);

            String noteIDString = request.getParameter("noteID");
            int noteID = Integer.parseInt(noteIDString);

            NoteRepository nr = new NoteRepository();

            Note note = nr.getNote(noteID);
            note.setContents(content);

            nr.update(note);
        }
    }
}
