
package Test;

import Entity.Book;
import Entity.BorrowRecord;
import Entity.User;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Test {
    private static EntityManagerFactory emf;
    private static EntityManager em;
    
    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("LibraryPU");
        em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();

            User user1 = new User("john_doe", "password123");
            User user2 = new User("jane_smith", "securepass");
            User user3 = new User("alice_jones", "alicepass");
            User user4 = new User("bob_williams", "bobpass");
            User user5 = new User("charlie_brown", "charliepass");
            User user6 = new User("david_miller", "davidpass");
            User user7 = new User("emma_white", "emmapass");
            User user8 = new User("frank_green", "frankpass");

            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.persist(user4);
            em.persist(user5);
            em.persist(user6);
            em.persist(user7);
            em.persist(user8);

            Book book1 = new Book("Java Programming", "John Author", "A comprehensive guide to Java programming.");
            Book book2 = new Book("Data Structures", "Jane Author", "A textbook on data structures for computer science.");
            Book book3 = new Book("Introduction to Algorithms", "Thomas H. Cormen", "An in-depth look at algorithms.");
            Book book4 = new Book("Clean Code", "Robert C. Martin", "Best practices in software development and writing clean code.");
            Book book5 = new Book("Design Patterns", "Erich Gamma", "The 23 design patterns every programmer should know.");
            Book book6 = new Book("Effective Java", "Joshua Bloch", "Essential programming techniques for Java developers.");
            Book book7 = new Book("Operating System Concepts", "Abraham Silberschatz", "A detailed guide to operating systems.");
            Book book8 = new Book("Computer Networks", "Andrew S. Tanenbaum", "A textbook on computer networks and their principles.");

            em.persist(book1);
            em.persist(book2);
            em.persist(book3);
            em.persist(book4);
            em.persist(book5);
            em.persist(book6);
            em.persist(book7);
            em.persist(book8);

            BorrowRecord borrowRecord1 = new BorrowRecord(user1, book1, LocalDateTime.now());
            BorrowRecord borrowRecord2 = new BorrowRecord(user2, book2, LocalDateTime.now());
            BorrowRecord borrowRecord3 = new BorrowRecord(user3, book3, LocalDateTime.now());
            BorrowRecord borrowRecord4 = new BorrowRecord(user4, book4, LocalDateTime.now());
            BorrowRecord borrowRecord5 = new BorrowRecord(user5, book5, LocalDateTime.now());
            BorrowRecord borrowRecord6 = new BorrowRecord(user6, book6, LocalDateTime.now());
            BorrowRecord borrowRecord7 = new BorrowRecord(user7, book7, LocalDateTime.now());
            BorrowRecord borrowRecord8 = new BorrowRecord(user8, book8, LocalDateTime.now());

            em.persist(borrowRecord1);
            em.persist(borrowRecord2);
            em.persist(borrowRecord3);
            em.persist(borrowRecord4);
            em.persist(borrowRecord5);
            em.persist(borrowRecord6);
            em.persist(borrowRecord7);
            em.persist(borrowRecord8);

            user1.getBorrowHsitory().add(borrowRecord1);
            user2.getBorrowHsitory().add(borrowRecord2);
            user3.getBorrowHsitory().add(borrowRecord3);
            user4.getBorrowHsitory().add(borrowRecord4);
            user5.getBorrowHsitory().add(borrowRecord5);
            user6.getBorrowHsitory().add(borrowRecord6);
            user7.getBorrowHsitory().add(borrowRecord7);
            user8.getBorrowHsitory().add(borrowRecord8);

            user1.addFriend(user2);
            user2.addFriend(user1); 

            em.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
            emf.close();
        }
    }
}
