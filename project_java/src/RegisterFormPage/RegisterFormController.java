/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package RegisterFormPage;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author ALMOSTAFA
 */
public class RegisterFormController implements Initializable {

    @FXML
    private TextField registerUserName;
    @FXML
    private TextField registerEmail;
    @FXML
    private Button registerSignupBtn;
    @FXML
    private RadioButton registerGenderMale;
    @FXML
    private ToggleGroup Gender;
    @FXML
    private RadioButton registerGenderFemale;
    @FXML
    private Button registerLoginBtn;
    @FXML
    private PasswordField registerPassword;
    @FXML
    private Label usernameError;
    @FXML
    private Label PasswordError;
    @FXML
    private Label EmailError;

    Alert a = new Alert(Alert.AlertType.ERROR);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        usernameError.setVisible(true);
        PasswordError.setVisible(true);
        EmailError.setVisible(true);
    }

    @FXML
    private void LoginbtnAction(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/loginform/LoginForm.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.setScene(scene);
        window.setMaxHeight(450);
        window.setMaxWidth(550);
        window.setMinHeight(450);
        window.setMinWidth(550);
        window.show();
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                if (ServerConnection.ServerCheck) {

                    ServerConnection.closeConnection();
                }
                    Platform.exit();
                    System.exit(0);

            }
        });
    }

    @FXML
    private void SignUpAction(ActionEvent event) {
        if (registerUserName.getText().isEmpty() == false && registerPassword.getText().isEmpty() == false && registerEmail.getText().isEmpty() == false) {
            try {
                if (ServerConnection.ServerCheck) {
                    // go to server and check if exist or no ;
                    String username = registerUserName.getText();
                    String email = registerEmail.getText();

                    boolean validEmail = CheckValiedEmail(email);
                    if (!validEmail) {
                        EmailError.setText("This Email is Not Valid ");
                        EmailError.setVisible(true);
                    } else {
                        String isExist = CheckUsername(username);
                        String isMAil = CheckEmail(email);
                        if (isExist.equals("True")) {
                            usernameError.setText("This Username is Altready Exist");
                            usernameError.setVisible(true);
                        } else if (isMAil.equals("True")) {
                            EmailError.setText("This Email is Already Exist");
                            EmailError.setVisible(true);
                        } else {
                            String pass = registerPassword.getText();
                            String gender = "";
                            if (registerGenderFemale.isSelected()) {
                                gender = "F";
                            } else {
                                gender = "M";
                            }

                            JSONObject data = new JSONObject();
                            JSONArray info = new JSONArray();
                            info.put(username);
                            info.put(gender);
                            info.put(email);
                            info.put(pass);
                            data.put("Key", "InsertUser");
                            data.put("data", info);
                            String Received = ServerConnection.ConnectToServer(data);
                            System.out.println(Received);
                            if (Received.equals("True")) {

                                Dashbord.MainDashBoardController.username = username;
                                Parent tableViewParent = FXMLLoader.load(getClass().getResource("/Dashbord/MainDashBoard.fxml"));
                                Scene tableViewScene = new Scene(tableViewParent);
                                //This line gets the Stage information
                                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                window.setScene(tableViewScene);
                                window.setMinWidth(1300);
                                window.setMinHeight(700);
                                window.setMaxWidth(1300);

                                window.setMaxHeight(700);
                                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                                window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
                                window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
                                window.show();

                            } else {
                                a.setContentText("Error canont connect to thr server");
                                a.setHeaderText("Error");
                                a.show();
                                ServerConnection.StartConnection();
                            }
                        }
                    }
                } else {
                    a.setContentText("Please Check Server Connection");
                    a.setHeaderText("Error");
                    a.show();
                    ServerConnection.StartConnection();
                }
            } catch (JSONException ex) {
                Logger.getLogger(RegisterFormController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(RegisterFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {

            if (registerUserName.getText().isEmpty()) {
                usernameError.setText("Enter you username");
            }
            if (registerPassword.getText().isEmpty()) {
                PasswordError.setText("Enter your password");
            }
            if (registerEmail.getText().isEmpty()) {
                EmailError.setText("Enter your email");
            }

        }
    }

    public String CheckUsername(String username) throws JSONException {
        try {
            JSONObject CheckUserName = new JSONObject();
            CheckUserName.put("Key", "CheckUserName");
            CheckUserName.put("data", username);

            String isExist = ServerConnection.ConnectToServer(CheckUserName);

            return isExist;
        } catch (IOException ex) {
            Logger.getLogger(RegisterFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String CheckEmail(String email) throws JSONException {
        try {
            JSONObject CheckEmail = new JSONObject();
            CheckEmail.put("Key", "CheckEmail");
            CheckEmail.put("data", email);
            String isExist = ServerConnection.ConnectToServer(CheckEmail);

            return isExist;
        } catch (IOException ex) {
            Logger.getLogger(RegisterFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean CheckValiedEmail(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    @FXML
    private void goToPassword(KeyEvent event) {
        if (event.getCode().getName().equals("Enter")) {
            registerPassword.requestFocus();
        }

    }

    @FXML
    private void goToEmail(KeyEvent event) {
        if (event.getCode().getName().equals("Enter")) {
            registerEmail.requestFocus();
        }
    }

    @FXML
    private void HideErrorMessage(MouseEvent event) {
        usernameError.setVisible(false);
        PasswordError.setVisible(false);
        EmailError.setVisible(false);
    }

}
