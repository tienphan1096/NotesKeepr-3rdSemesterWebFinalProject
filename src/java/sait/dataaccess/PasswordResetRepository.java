/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tien Phan
 */

package sait.dataaccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import sait.domainmodel.PasswordReset;

public class PasswordResetRepository {
    
    public PasswordReset getPasswordReset(String username){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        PasswordReset passwordReset=em.find(PasswordReset.class, username);
        
        em.close();
        
        return passwordReset;
    }
    
    public PasswordReset getPasswordResetWithUuid(String uuid){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        try{
            PasswordReset passwordReset = em.createNamedQuery("PasswordReset.findByUuid", PasswordReset.class).setParameter("uuid", uuid).getResultList().get(0);
            return passwordReset;
        }catch(IndexOutOfBoundsException e){
            return null;
        }
    }
    
    public int insert(PasswordReset passwordReset) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.persist(passwordReset);
        trans.commit();

        em.close();

        return 1;
    }
    
    public int delete(PasswordReset passwordReset) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.remove(em.merge(passwordReset));
        trans.commit();

        em.close();

        return 1;
    }
    
}
