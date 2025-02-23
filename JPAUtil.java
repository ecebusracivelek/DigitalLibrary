
package project5;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("LibraryPU");
    
    public static EntityManager getEntityManaqer(){
        return emf.createEntityManager();
    }
    
    public static void closeEntityManagerFactory(){
        emf.close();
    }
}
