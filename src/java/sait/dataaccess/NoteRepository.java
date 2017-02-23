/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.dataaccess;

import sait.domainmodel.Note;
import sait.domainmodel.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Tien Phan
 */
public class NoteRepository {

    public int insert(Note note, User owner) {
        
        owner.getNoteList().add(note);      
              
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans=em.getTransaction();
        
        trans.begin();
        em.merge(owner);
        em.persist(note);
        trans.commit();
        em.close();
        
        return 1;
    }
    
    public int delete(Note note){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        User owner=note.getOwner();
        owner.getNoteList().remove(note);
        
        em.getTransaction().begin();
        em.merge(owner);
        em.remove(em.merge(note));
        em.getTransaction().commit();
        
        em.close();
        
        return 1;
        
    }

    public Note getNote(int noteID){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        Note note;
        note=em.find(Note.class, noteID);
        
        em.close();
        
        return note;
    }

    public int update(Note note) {
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        em.getTransaction().begin();
        em.merge(note);
        em.getTransaction().commit();
        
        em.close();
        
        return 1;
    }

}
