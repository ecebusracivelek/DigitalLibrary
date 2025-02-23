
package Entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Column;

@Embeddable
public class BorrowRecordID implements Serializable {
    @Column(name = "user_id")
    private Long userID;

    @Column(name = "book_id")
    private Long bookID;

    public BorrowRecordID() {}

    public BorrowRecordID(Long userID, Long bookID) {
        this.userID = userID;
        this.bookID = bookID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserId(Long userID) {
        this.userID = userID;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorrowRecordID that = (BorrowRecordID) o;
        return Objects.equals(userID, that.userID) && Objects.equals(bookID, that.bookID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, bookID);
    }
}
