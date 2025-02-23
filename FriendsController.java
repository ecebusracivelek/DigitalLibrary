
package project5;

import Entity.BorrowRecord;
import Entity.User;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class FriendsController{

    @FXML
    private TextField searchUserField;
    
    @FXML
    private ListView<User> friendsList;
    
    @FXML
    private TableView<BorrowRecord> friendBooksTable;
    
    @FXML
    private TableColumn<BorrowRecord, String> bookTitleColumn;
    
    @FXML
    private TableColumn<BorrowRecord, String> bookAuthorColumn;
    
    @FXML
    private TableColumn<BorrowRecord, String> borrowDateColumn;
    
    @FXML
    private TableColumn<BorrowRecord, String> returnDateColumn;
    
    @FXML
    private Label messageLabel;
    
    private User currentUser;
    private LibraryService libraryService;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    public void initialize() {
        libraryService = new LibraryService();
        setupFriendsList();
        setupBooksTable();
        
        friendsList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    loadFriendBooks(newValue);
                }
            }
        );
    }
    
    public void initialData(User user) {
        this.currentUser = user;
        refreshFriendsList();
    }
    
    private void setupFriendsList() {
        friendsList.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getUsername());
                }
            }
        });
    }
    
    private void setupBooksTable() {
        bookTitleColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBook().getBookName()));
            
        bookAuthorColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBook().getAuthor()));
            
        borrowDateColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(
                cellData.getValue().getBorrowDate().format(dateFormatter)));
                
        returnDateColumn.setCellValueFactory(cellData -> {
            LocalDateTime returnDate = cellData.getValue().getReturnDate();
            return new SimpleStringProperty(
                returnDate != null ? returnDate.format(dateFormatter) : "Not returned");
        });
    }
    
    @FXML
    private void handleAddFriend() {
        String username = searchUserField.getText().trim();
        
        if (username.isEmpty()) {
            showMessage("Please enter a username");
            return;
        }
        
        if (username.equals(currentUser.getUsername())) {
            showMessage("You cannot add yourself as a friend");
            return;
        }
        
        try {
            User friend = libraryService.findUserByUsername(username);
            if (friend != null) {
                if (currentUser.getFriends().contains(friend)) {
                    showMessage("Already friends with " + username);
                } else {
                    libraryService.addFriend(currentUser, friend);
                    refreshFriendsList();
                    searchUserField.clear();
                    showMessage("Friend added successfully", false);
                }
            } else {
                showMessage("User not found: " + username);
            }
        } catch (Exception e) {
            showMessage("Error adding friend: " + e.getMessage());
        }
    }
    
    private void loadFriendBooks(User friend) {
        try {
            List<BorrowRecord> borrowRecords = friend.getBorrowHsitory();
            friendBooksTable.getItems().setAll(borrowRecords);
        } catch (Exception e) {
            showMessage("Error loading friend's books: " + e.getMessage());
        }
    }
    
    private void refreshFriendsList() {
        friendsList.getItems().clear();
        friendsList.getItems().addAll(currentUser.getFriends());
    }
    
    private void showMessage(String message) {
        showMessage(message, true);
    }
    
    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: " + (isError ? "red" : "green"));
        messageLabel.setVisible(true);
    }
}
