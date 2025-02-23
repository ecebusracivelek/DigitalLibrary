
package project5;

import Entity.BorrowRecord;
import Entity.User;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class MyBooksController{
    @FXML
    private TableView<BorrowRecord> myBooksTable;
    
    @FXML
    private TableColumn<BorrowRecord, String> titleColumn;
    
    @FXML
    private TableColumn<BorrowRecord, String> authorColumn;
    
    @FXML
    private TableColumn<BorrowRecord, String> borrowDateColumn;
    
    @FXML
    private TableColumn<BorrowRecord, String> returnDateColumn;
    
    private User currentUser;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
   
    public void initialize() {
        setupTableColumns();
    }
    
    public void initialData(User user) {
        this.currentUser = user;
        showAllHistory();
    }
    
    private void setupTableColumns() {
        titleColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBook().getBookName()));
            
        authorColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));
            
        borrowDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBorrowDate().format(dateFormatter)));
            
        returnDateColumn.setCellValueFactory(cellData -> {
            LocalDateTime returnDate = cellData.getValue().getReturnDate();
            return new SimpleStringProperty(returnDate != null ? 
                returnDate.format(dateFormatter) : "Not returned");
        });
    }
    
    @FXML
    private void showCurrentlyBorrowed() {
        List<BorrowRecord> currentlyBorrowed = new ArrayList<>();
        for (BorrowRecord record : currentUser.getBorrowHsitory()) {
            if (!record.getIsReturned()) {
                currentlyBorrowed.add(record);
            }
        }
        myBooksTable.getItems().setAll(currentlyBorrowed);
    }
    
    @FXML
    private void showAllHistory() {
        EntityManager em = JPAUtil.getEntityManaqer();
        try {
            TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.borrowHistory WHERE u.userID = :userId", 
                User.class
            );
            query.setParameter("userId", currentUser.getUserID());

            User refreshedUser = query.getSingleResult();
            if (refreshedUser != null) {
                currentUser = refreshedUser;
                myBooksTable.getItems().clear();
                myBooksTable.getItems().addAll(currentUser.getBorrowHsitory());
            }
        } catch (Exception e) {
            System.err.println("Error refreshing borrow history: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    @FXML
    private void handleClose() {
        ((Stage) myBooksTable.getScene().getWindow()).close();
    }
    
    public void refreshData(){
        showAllHistory();
    }
}
