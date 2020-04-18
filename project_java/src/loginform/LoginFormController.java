/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginform;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Ahmed Saeed
 */
public class LoginFormController implements Initializable {

    @FXML
    private PasswordField loginPassword;
    @FXML
    private Button BtnLogin;
    @FXML
    private TextField loginUserName;
    @FXML
    private Button BtnSingup;
    @FXML
    private Text username_error;
    @FXML
    private Text password_error;
    Alert a = new Alert(Alert.AlertType.ERROR);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void hideErrorPassword(MouseEvent event) {
        username_error.setVisible(false);
        password_error.setVisible(false);
    }

    @FXML
    private void goToDashBoard(MouseEvent event) {
       
        if (loginUserName.getText().isEmpty() == false && loginPassword.getText().isEmpty() == false) {

            if (ServerConnection.ServerCheck) {
                try {
                    String user = loginUserName.getText();
                    String pass = loginPassword.getText();

                    JSONObject data = new JSONObject();
                    JSONArray info = new JSONArray();
                    info.put(user);
                    info.put(pass);
                    data.put("Key", "Login");
                    data.put("data", info);
                    String Received = ServerConnection.ConnectToServer(data);
                    if (Received.equals("True")) {

                        Dashbord.MainDashBoardController.username = user;
                        Parent myDashBord = FXMLLoader.load(getClass().getResource("/Dashbord/MainDashBoard.fxml"));
                        Scene dashbord = new Scene(myDashBord);
                        //This line gets the Stage information
                        Stage window = (Stage) BtnLogin.getScene().getWindow();
                        window.setScene(dashbord);

                        window.setMinWidth(1300);
                        window.setMinHeight(700);
                        window.setMaxWidth(1300);

                        window.setMaxHeight(700);
                        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
                        window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
                        window.show();
                        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent e) {
                                if (ServerConnection.ServerCheck) {

                                    ServerConnection.closeConnection();
                                    Platform.exit();
                                    System.exit(0);

                                } else {
                                    Platform.exit();
                                    System.exit(0);
                                }
                            }
                        });

                    } else if (Received.equals("False")) {
                        loginUserName.setText("");
                        loginPassword.setText("");
                        loginUserName.requestFocus();
                        a.setContentText("username or password incorrect");
                        a.setHeaderText("Error");
                        a.show();

                    } else {
                        a.setContentText("Server has been closed ");
                        a.setHeaderText("Error");
                        a.show();
                        ServerConnection.StartConnection();

                    }
                } catch (JSONException ex) {
                    Logger.getLogger(LoginFormController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(LoginFormController.class.getName()).log(Level.SEVERE, null, ex);
                }

                //else
            } else {
                //server is close
                a.setContentText("Server Error");
                a.setHeaderText("Error");
                a.show();
                ServerConnection.StartConnection();

            }

        } else {

            if (loginUserName.getText().isEmpty()) {
                username_error.setVisible(true);
                System.out.println("error found in user name");
            }
            if (loginPassword.getText().isEmpty()) {
                password_error.setVisible(true);
                System.out.println("error found in password");
            }

        }

    
    }

    @FXML
    private void hideErrorUsername(MouseEvent event) {

        username_error.setVisible(false);
        password_error.setVisible(false);
    }

    @FXML
    private void goToRegister(MouseEvent event) {
        try {
            Parent myRegisterPage = FXMLLoader.load(getClass().getResource("/RegisterFormPage/RegisterForm.fxml"));
            Scene register = new Scene(myRegisterPage);
            //This line gets the Stage information
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(register);
            window.setMaxHeight(550);
            window.setMaxWidth(550);
            window.setMinHeight(600);
            window.setMinWidth(550);

            window.setTitle("Collaborative To-Do List");
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
            window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
            window.show();

            window.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    if (ServerConnection.ServerCheck) {

                        ServerConnection.closeConnection();
                        Platform.exit();
                        System.exit(0);

                    } else {
                        Platform.exit();
                        System.exit(0);
                    }
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(LoginFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void goToLoginBtn(KeyEvent event) {
         
    }

    @FXML
    private void goToPassword(KeyEvent event) {
        if (event.getCode().getName().equals("Enter")) {
            loginPassword.requestFocus();
        }
    }

    

}
