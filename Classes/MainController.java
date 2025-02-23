
package project5;

import Entity.Book;
import Entity.BorrowRecord;
import Entity.User;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.collections.ObservableList;

public class MainController{

    @FXML 
    private TextField searchField;
    
    @FXML
    private TableView<Book> booksTable;
    
    @FXML 
    private TableColumn<Book, String> titleColumn;
    
    @FXML 
    private TableColumn<Book, String> authorColumn;
    
    @FXML
    private TableColumn<Book, String> statusColumn;
    
    @FXML 
    private Label infoLabel;
    
    private LibraryService libraryService = new LibraryService();
    private User currentUser;
    
    public void initialData(User user){
        this.currentUser = user;
        setUpTableColumn();
        loadAllBooks();
    }
    
    private void setUpTableColumn(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        statusColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            boolean isBorrowed = book.getBorrowRecords().stream().anyMatch(record -> !record.getIsReturned() && record.getUser().equals(currentUser));
            return new SimpleStringProperty(isBorrowed ? "Borrowed" : "Avaialble");
        });
    }
    
    @FXML
    private void handleSearch(){
        String searchTerm = searchField.getText();
        List<Book> results = libraryService.searchBooks(searchTerm);
        booksTable.getItems().setAll(results);
    }
    
    @FXML
    private void handleBorrow(){
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if(selectedBook != null){
            try{
                libraryService.borrowBook(currentUser, selectedBook);
                selectedBook.getBorrowRecords().add(new BorrowRecord(currentUser, selectedBook, LocalDateTime.now()));
                booksTable.refresh();
                infoLabel.setText("Book borrowed successfully");
            }catch(Exception e){
                showError("Error borrowing book: " + e.getMessage());
            }
        }
    }
    
    @FXML
    private void handleReturn(){
        Book selectedBook = booksTable.getSelectionModel().getSelectedItem();
        if(selectedBook != null){
            try{
                if(libraryService.returnBook(currentUser, selectedBook)){
                    selectedBook.getBorrowRecords().stream()
                    .filter(record -> !record.getIsReturned() && record.getUser().equals(currentUser))
                    .findFirst()
                    .ifPresent(record -> { 
                        record.returnBook(); 
                        booksTable.refresh();
                    });

                    infoLabel.setText("Book returned successfully");
                }else {
                    infoLabel.setText("Could not return the book. It might already be returned.");
                }
            }catch(Exception e) {
                showError("Error returning book: " + e.getMessage());
                e.printStackTrace(); 
            }
        }
    }
    
    @FXML
    private void showMyBooks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MyBooks.fxml"));
            Parent root = loader.load();

            MyBooksController controller = loader.getController();
            controller.initialData(currentUser);

            Stage myBooksStage = new Stage();
            myBooksStage.setTitle("My Books");
            myBooksStage.setScene(new Scene(root));
            myBooksStage.setUserData(controller);
            myBooksStage.show();
        } catch (IOException e) {
            showError("Error showing my books: " + e.getMessage());
        }
    }
    
    @FXML
    private void showFriends(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Friends.fxml"));
            Parent root = loader.load();
            
            FriendsController controller = loader.getController();
            controller.initialData(currentUser);
            
            Stage friendStage = new Stage();
            friendStage.setTitle("Friend");
            friendStage.setScene(new Scene(root));
            friendStage.show();
        }catch(IOException e){
            showError("Error opening friends window");
        }
    }
    
    @FXML
    private void handleLogout(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) searchField.getScene().getWindow();
            stage.setScene(new Scene(root));
        }catch(IOException e){
            showError("Error returning to login");
        }
    }
    
    private void showError(String message){
        infoLabel.setText(message);
        infoLabel.setStyle("-fx-text-fill: red;");
    }
    
    private void loadAllBooks(){
        booksTable.getItems().addAll(libraryService.searchBooks(""));
    }
}

