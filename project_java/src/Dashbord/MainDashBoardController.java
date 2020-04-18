/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dashbord;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Ahmed Saeed
 */
public class MainDashBoardController implements Initializable {

    @FXML
    private BorderPane BorderPane;
    @FXML
    private Button BtnDashBoard;
    @FXML
    private Button BtnNotification;
    @FXML
    private Button BtnTeamMate;
    @FXML
    private Button BtnGraph;
    @FXML
    private Button BtnLogout;

    public static String username = "";
    @FXML
    private ImageView imageview0;
    @FXML
    private Text txt_username;
    Alert a = new Alert(Alert.AlertType.ERROR);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Parent root;
        try {
            AllTasks.AllTasksController.username = username;
            System.out.println("dash borad : " + username);
            root = FXMLLoader.load(getClass().getResource("/AllTasks/AllTasks.fxml"));
            BorderPane.setCenter(root);
        } catch (IOException ex) {
            showAlertConnection();
            ServerConnection.StartConnection();
        }
        txt_username.setText(username);

    }

    @FXML
    private void goToDashboard(MouseEvent event) {
        if (ServerConnection.ServerCheck) {
            Parent root;
            try {
                AllTasks.AllTasksController.username = username;

                root = FXMLLoader.load(getClass().getResource("/AllTasks/AllTasks.fxml"));
                BorderPane.setCenter(root);
            } catch (IOException ex) {
                showAlertConnection();
            }
        } else {
            showAlertConnection();
            ServerConnection.StartConnection();
        }
    }

    @FXML
    private void goToNotification(MouseEvent event) {
        if (ServerConnection.ServerCheck) {
            Parent root;
            try {
                Notification.FXMLDocumentController.username = username;
                root = FXMLLoader.load(getClass().getResource("/Notification/FXMLDocument.fxml"));
                BorderPane.setCenter(root);
            } catch (IOException ex) {
                showAlertConnection();
            }
        } else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Error in Connection with the Server please check the connection");
            a.show();
            ServerConnection.StartConnection();
        }
    }

    @FXML
    private void goToTeamMate(MouseEvent event) {
        if (ServerConnection.ServerCheck) {
            Parent root;
            try {
                Team.TeamMatesFormController.username = username;
                root = FXMLLoader.load(getClass().getResource("/Team/TeamMatesForm.fxml"));
                BorderPane.setCenter(root);
            } catch (IOException ex) {
                showAlertConnection();
            }

        } else {
            showAlertConnection();
            ServerConnection.StartConnection();
        }
    }

    @FXML
    private void goToGraph(MouseEvent event) {
        if (ServerConnection.ServerCheck) {
            Parent root;
            try {
                Graph.GraphController.username = username;
                root = FXMLLoader.load(getClass().getResource("/Graph/Graph.fxml"));
                BorderPane.setCenter(root);
            } catch (IOException ex) {
                showAlertConnection();
                //Logger.getLogger(MainDashBoardController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showAlertConnection();
            ServerConnection.StartConnection();

        }
    }

    private void goToLogin(MouseEvent event) {
        try {

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

        } catch (IOException ex) {
            Logger.getLogger(MainDashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String ChooseImage(String username) {
        try {
            JSONObject data = new JSONObject();
            data.put("Key", "getGender");
            data.put("username", username);
            String res = ServerConnection.ConnectToServer(data);
            return res;
        } catch (JSONException ex) {
            Logger.getLogger(MainDashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainDashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    @FXML
    private void LogOutBtn(ActionEvent event) {
        if (ServerConnection.ServerCheck) {
            ServerConnection.closeConnection();
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/loginform/LoginForm.fxml"));
                Scene scene = new Scene(root);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.setScene(scene);
                window.setMaxHeight(450);
                window.setMaxWidth(550);
                window.setMinHeight(450);
                window.setMinWidth(550);
                Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
                window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
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
            } catch (IOException ex) {
                Logger.getLogger(MainDashBoardController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public void showAlertConnection() {
            a.setAlertType(Alert.AlertType.ERROR);
            a.setContentText("Error in Connection with the Server please check the connection");
            a.show();
            ServerConnection.StartConnection();
        
    }
}
