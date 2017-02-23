/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sait.dataaccess;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Tien Phan
 */
public class DBUtil {
    private static final EntityManagerFactory emf=Persistence.createEntityManagerFactory("NotesKeeprPU");
    
    public static EntityManagerFactory getEmFactory(){
        return emf;
    }

    public static void close() {
        emf.close();
    }
    
}
