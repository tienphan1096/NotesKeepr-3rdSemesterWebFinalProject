/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.dataaccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import sait.domainmodel.Activation;

/**
 *
 * @author Tien Phan
 */
public class ActivationRepository {
    
    public Activation getActivation(String uuid) {
        
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        
        try{
            Activation activation = em.createNamedQuery("Activation.findByUuid", Activation.class).setParameter("uuid", uuid).getResultList().get(0);
          
            return activation;  
        }catch (IndexOutOfBoundsException e){
            return null;
        }finally{
            em.close();
        }
    }
    
    public int insert(Activation activation){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.persist(activation);
        trans.commit();

        em.close();

        return 1;
    }
    
    public int update(Activation activation) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.merge(activation);
        trans.commit();

        em.close();

        return 1;
    }
    
    public int delete(Activation activation) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.remove(em.merge(activation));
        trans.commit();

        em.close();

        return 1;
    }
}
