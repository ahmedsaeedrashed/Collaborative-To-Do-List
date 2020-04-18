/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Ramzy
 */
public class GraphController implements Initializable {

    public static String username;
    Alert a;
    @FXML
    private AreaChart<?, ?> areaChart;

    @FXML
    private Label createdTaskCount;

    @FXML
    private Label AssignedTaskCount;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MyTasks();
    }

    public void MyTasks() {
        try {

            System.out.println(username);
            JSONObject tasks_res = new JSONObject();
            tasks_res.put("Key", "GetTasksCount");
            tasks_res.put("username", username);
            String res = ServerConnection.ConnectToServer(tasks_res);
            System.out.println(res);
            if (res.equals("ServerError") || res.equals("ErrorWithTheServer")) {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("Error in Connection with the Server please check the connection");
                a.show();
                ServerConnection.StartConnection();
            } else {
                JSONObject count_res = new JSONObject(res);
                int todo = count_res.getInt("todo");
                System.out.println(todo);
                int done = count_res.getInt("done");
                System.out.println(done);
                int prog = count_res.getInt("inprogress");
                int count1 = done + prog + todo;
                createdTaskCount.setText(String.valueOf(count1));
                XYChart.Series createdTasksSeries = new XYChart.Series<>(); //Make a new XYChart object
                //Add Data
                createdTasksSeries.getData().add(new XYChart.Data("ToDo", todo));
                createdTasksSeries.getData().add(new XYChart.Data("InProgress", prog));
                createdTasksSeries.getData().add(new XYChart.Data("Done", done));

                JSONObject Assigntasks_res = new JSONObject();
                Assigntasks_res.put("Key", "GetTasksAssignToMeCount");
                Assigntasks_res.put("username", username);
                String assignTask = ServerConnection.ConnectToServer(Assigntasks_res);
                if (assignTask.equals("ServerError") || assignTask.equals("ErrorWithTheServer")) {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Error in Connection with the Server please check the connection");
                    a.show();
                    ServerConnection.StartConnection();
                } else {
                    JSONObject count_tasks = new JSONObject(assignTask);

                    int Assigntodo = count_tasks.getInt("todo");
                    System.out.println(todo);
                    int Assigndone = count_tasks.getInt("done");
                    System.out.println(done);
                    int Assignprog = count_tasks.getInt("inprogress");

                    XYChart.Series assiedTasksSeries = new XYChart.Series<>(); //Make a new XYChart object
                    //Add Data
                    assiedTasksSeries.getData().add(new XYChart.Data("ToDo", Assigntodo));
                    assiedTasksSeries.getData().add(new XYChart.Data("InProgress", Assignprog));
                    assiedTasksSeries.getData().add(new XYChart.Data("Done", Assigndone));
                    int count2 = Assigntodo + Assigndone + Assignprog;
                    AssignedTaskCount.setText(String.valueOf(count2));
                    areaChart.getData().addAll(createdTasksSeries, assiedTasksSeries);
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GraphController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
