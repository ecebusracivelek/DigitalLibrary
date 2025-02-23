
package project5;

import Entity.Book;
import Entity.BorrowRecord;
import Entity.User;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class LibraryService {
    public User authenticateUser(String username, String password){
        EntityManager em = JPAUtil.getEntityManaqer();
        try{
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username AND u.password = :password", User.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            try{
                User user = query.getSingleResult();
            
            //if(user != null && user.getPassword().equals(password))
                return user;
            }catch(NoResultException e){
                return null;
            }
        }finally{
            em.close();
        }
    }
    
    public List<Book> searchBooks(String searchTerm){
        EntityManager em = JPAUtil.getEntityManaqer();
        try{
            String jpql = "SELECT b FROM Book b WHERE LOWER(b.bookName) LIKE LOWER(:term)" +
                    "OR LOWER(b.author) LIKE LOWER(:term)";
            TypedQuery<Book> query = em.createQuery(jpql, Book.class);
            query.setParameter("term", "%" + searchTerm + "%");
            return query.getResultList();
        }finally{
            em.close();
        }
    }
    
    public void borrowBook(User user, Book book){
        EntityManager em = JPAUtil.getEntityManaqer();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            BorrowRecord record = BorrowRecord.createNewBorrow(user, book);
            em.persist(record);
            user.getBorrowHsitory().add(record);
            em.merge(user);
            tx.commit();
        }catch(Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }
    
    public boolean returnBook(User user, Book book){
        EntityManager em = JPAUtil.getEntityManaqer();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            
            String jpql = "SELECT br FROM BorrowRecord br WHERE br.user = :user AND br.book = :book AND br.isReturned = false";
            TypedQuery<BorrowRecord> query = em.createQuery(jpql, BorrowRecord.class);
            query.setParameter("user", user);
            query.setParameter("book", book);
            
            try {
                BorrowRecord record = query.getSingleResult();
                if(record != null) {
                    record.returnBook();
                    em.merge(record);
                    tx.commit();
                    return true;
                }
                return false;
            } catch(NoResultException e) {
                if(tx.isActive()) {
                    tx.rollback();
                }
                return false;
            }
        } catch(Exception e) {
            if(tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    public void addFriend(User user, User friend){
        EntityManager em = JPAUtil.getEntityManaqer();
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            user.addFriend(friend);
            em.merge(user);
            tx.commit();
        }catch(Exception e){
            if(tx != null && tx.isActive()){
                tx.rollback();
            }
            throw e;
        }finally{
            em.close();
        }
    }
    
    public User findUserByUsername(String username){
        EntityManager em = JPAUtil.getEntityManaqer();
        try{
            TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        }catch(NoResultException e){
            return null;
        }finally{
            em.close();
        }
    }
}
