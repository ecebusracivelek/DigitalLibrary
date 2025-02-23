
package project5;

import Entity.User;
import java.io.IOException;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class LoginController{

    @FXML 
    private TextField usernameField;
    
    @FXML 
    private PasswordField passwordField;
    
    @FXML 
    private Label errorLabel;
    
    private LibraryService libraryService;
    
    public void initialize(){
        libraryService = new LibraryService();
        errorLabel.setVisible(false);
    }
    
    @FXML
    private void handleLogin(ActionEvent event){
        try{
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if(username.isEmpty() || password.isEmpty()) {
                showError("Please enter both username and password");
                return;
            }

            User user = libraryService.authenticateUser(username, password);
            if(user != null)
                openMainWindow(user);
            else{
                errorLabel.setText("Invalid username or password");
                errorLabel.setVisible(true);
            }
        }catch(Exception e){
            e.printStackTrace();
            showError("Login error: " + e.getMessage());
        }
    }
    
    private void openMainWindow(User user){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            
            MainController controller = loader.getController();
            controller.initialData(user);
            
            Stage stage = (Stage)usernameField.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
            showError("Error loading main window");
        }
    }
    
    private void showError(String message){
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}
