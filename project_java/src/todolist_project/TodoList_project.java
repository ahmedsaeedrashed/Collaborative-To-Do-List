/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package todolist_project;

//import AddTask.AddTaskBase;
//import ConnectToServer.ServerConnection;
//import Dashbord.MainDashBoardBase;
//import RegisterFormPage.RegisterFormBase;
//import java.io.IOException;
import ConnectToServer.ServerConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
//import javafx.application.Platform;
//import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

//import javafx.stage.WindowEvent;
//import loginform.LoginFormBase;

/**
 *
 * @author ALMOSTAFA
 */
public class TodoList_project extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
       
        Parent root = FXMLLoader.load(getClass().getResource("/loginform/LoginForm.fxml"));
        Scene scene = new Scene(root);
       // scene.setRoot(root);
        stage.setScene(scene);
        stage.setMaxHeight(450);
        stage.setMaxWidth(550);
        stage.setMinHeight(450);
        stage.setMinWidth(550);
        
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                if (ServerConnection.ServerCheck) {

                    ServerConnection.closeConnection();
                }
                    Platform.exit();
                    System.exit(0);

            }
        });
        ServerConnection.StartConnection();
     
    }

    
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
