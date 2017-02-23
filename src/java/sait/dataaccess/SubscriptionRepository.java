/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.dataaccess;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import sait.domainmodel.Subscription;
import sait.domainmodel.User;

/**
 *
 * @author Tien Phan
 */
public class SubscriptionRepository {
    
    public Subscription getSubscription(String username) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        Subscription subscription = em.find(Subscription.class, username);

        em.close();

        return subscription;
    }
    
    public int delete(Subscription subscription) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.remove(em.merge(subscription));
        trans.commit();

        em.close();

        return 1;
    }
    
    public int insert(Subscription subscription) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();

        trans.begin();
        em.persist(subscription);
        trans.commit();

        em.close();

        return 1;
    }
    
}
