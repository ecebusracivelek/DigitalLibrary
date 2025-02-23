
package Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<BorrowRecord> borrowHistory = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "friend_of_id")
    private List<User> friends = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "friend_of_id")
    private User friendOf;
    
    public User(){}
    
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public Long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<BorrowRecord> getBorrowHsitory() {
        return borrowHistory;
    }

    public void setBorrowHsitory(List<BorrowRecord> borrowHsitory) {
        this.borrowHistory = borrowHsitory;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }    

    public User getFriendOf() {
        return friendOf;
    }

    public void setFriendOf(User friendOf) {
        this.friendOf = friendOf;
    }
    
    public void addFriend(User friend) {
        if (!this.friends.contains(friend)) {
           this.friends.add(friend);
           friend.setFriendOf(this);
        }
    }

    public void removeFriend(User friend) {
        if(this.friends.contains(friend)){
            this.friends.remove(friend);
            friend.setFriendOf(null);
        }
    }
    
    public List<Book> getpreviouslyBorrowedBooks(){
        Set<Book> uniqueBooks = new HashSet<>();
        for(BorrowRecord record: this.borrowHistory){
            uniqueBooks.add(record.getBook());
        }
        return new ArrayList<>(uniqueBooks);
    }
    
    public List<Book> getFriendsBorrowedBooks(){
        Set<Book> uniqueBooks = new HashSet<>();
        for(User friend: this.friends){
            for(Book book: friend.getpreviouslyBorrowedBooks()){
                uniqueBooks.add(book);
            }
        }
        return new ArrayList<>(uniqueBooks);
    }
}

