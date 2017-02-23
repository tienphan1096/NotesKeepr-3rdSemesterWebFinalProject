/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.dataaccess;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.transaction.Transaction;
import sait.domainmodel.Role;
import sait.domainmodel.User;

/**
 *
 * @author Tien Phan
 */
public class RoleRepository {
    
    public RoleRepository(){
    }
    
    public Role getRole(int roleID) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        Role role = em.find(Role.class, roleID);

        em.close();

        return role;
    }

    public int update(Role role){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        trans.begin();
        em.merge(role);
        trans.commit();

        em.close();

        return 1;
    }    
}
