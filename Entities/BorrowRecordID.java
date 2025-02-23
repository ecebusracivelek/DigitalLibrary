
package Entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Entity
@Table(name = "borrow_records")
public class BorrowRecord {
    @EmbeddedId
    private BorrowRecordID id;

    @ManyToOne
    @MapsId("userID") 
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("bookID") 
    @JoinColumn(name = "book_id")
    private Book book;

    @Column(nullable = false)
    private LocalDateTime borrowDate;
    
    private LocalDateTime returnDate;
    
    @Column(nullable = false)
    private boolean isReturned;

    public BorrowRecord() {}

    public BorrowRecord(User user, Book book, LocalDateTime borrowDate) {
        this.id = new BorrowRecordID(user.getUserID(), book.getBookID());
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.isReturned = false;
    }

    public BorrowRecordID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public boolean getIsReturned() {
        return isReturned;
    }

    public void setIsReturned(boolean isReturned) {
        this.isReturned = isReturned;
    }

    public void returnBook(){
        this.isReturned = true;
        this.returnDate = LocalDateTime.now();
    }
    
    public static BorrowRecord createNewBorrow(User user, Book book) {
        return new BorrowRecord(user, book, LocalDateTime.now());
    }
}
