/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.dataaccess;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import sait.domainmodel.Role;
import sait.domainmodel.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/**
 *
 * @author Tien Phan
 */
public class UserRepository {
    public User getUser(String username){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        User user=em.find(User.class, username);
        
        em.close();
        
        if (user.isActivated()) return user;
        else return null;
    }
    
    public User getUnactivatedOrActivatedUser(String username){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();

        User user = em.find(User.class, username);

        em.close();
        
        return user;
    }
    
    public List<User> getAll(){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        
        List<User> userList;
        
        userList=em.createNamedQuery("User.findAll", User.class).getResultList();
        
        int i=0;
        while (i<userList.size()){
            if (!userList.get(i).isActivated()){
                userList.remove(i);
            }else{
                i++;
            }
        }
        
        em.close();
        
        return userList;
    }
    
    public int insert(User user){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans=em.getTransaction();
        
        trans.begin();
        em.persist(user);
        trans.commit();
        
        em.close();
        
        return 1;
    }
    
    public int update(User user){
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        
        trans.begin();
        em.merge(user);
        trans.commit();

        em.close();

        return 1;
    }
    
    public int delete(User user){
        EntityManager em=DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans=em.getTransaction();
        
        trans.begin();
        em.remove(em.merge(user));
        trans.commit();
        
        em.close();
        
        return 1;        
    }
    
//    public int addRoleToUser(Role role, User user) {
//
//        EntityManager em = DBUtil.getEmFactory().createEntityManager();
//        EntityTransaction trans = em.getTransaction();
//
//        List<Role> roleList = user.getRoleList();
//
//        roleList.add(role);
//
//        trans.begin();
//        em.merge(user);
//        trans.commit();
//        em.close();
//
//        return 1;
//
//    }
}
