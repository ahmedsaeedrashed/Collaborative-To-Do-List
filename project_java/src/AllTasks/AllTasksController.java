/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AllTasks;

import ConnectToServer.ServerConnection;
import Dashbord.MainDashBoardController;
import static Graph.GraphController.username;
import Team.ListViewCell;
import Team.TeamMatesFormController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Ahmed Saeed
 */
public class AllTasksController extends ListView<String> implements Initializable {

    @FXML
    private Button addTask;
    @FXML
    private ListView<Task_Details> listview_id;

    public static String username = "";
    Alert a;
    JSONArray Alldata;

    ObservableList<Task_Details> data = FXCollections.observableArrayList();
    @FXML
    private Text todo_count;
    @FXML
    private Text progress_count;
    @FXML
    private Text done_count;
    @FXML
    private Text team_matte_count;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getTasks(username);
        getTeamMates(username);
        listview_id.setItems(data);
        listview_id.setCellFactory(param -> new Cell());

        setTasksCount();
    }

    private void setTasksCount() {

        int todo = 0;
        int done = 0;
        int progress = 0;
        try {
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
                todo = count_res.getInt("todo");
                done = count_res.getInt("done");
                progress = count_res.getInt("inprogress");
            }

            done_count.setText(String.valueOf(done));
            progress_count.setText(String.valueOf(progress));
            todo_count.setText(String.valueOf(todo));

        } catch (JSONException ex) {
            Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    class Cell extends ListCell<Task_Details> {

        HBox hbox = new HBox();

        Label taskName = new Label();

        Label startDate = new Label();
        Label endDate = new Label();
        Label status = new Label();

        Button Accept = new Button("Description");

        ImageView imageView = new ImageView(new Image(getClass().getClassLoader().getResource("recourse/delete.png").toExternalForm()));

        Button Reject = new Button("", imageView);

        @Override
        protected void updateItem(Task_Details item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.

            if (empty || item == null) {
                // System.out.println("if empty");
                taskName.setText("");
                startDate.setText("");
                endDate.setText("");
                status.setText("");

                setGraphic(null);
            }
            if (item != null && !empty) {
                taskName.setText(item.getTitle());
                startDate.setText(item.getStart_date());
                endDate.setText(item.getEnd_date());
                status.setText(item.getStatus());

                setGraphic(hbox);
            }
        }

        public Cell() {
            super();
            hbox.getChildren().addAll(taskName, startDate, endDate, status, Accept, Reject);
            hbox.setStyle("-fx-background-color: #3e3e41;-fx-spacing: 45;");

            hbox.setPadding(new Insets(5, 5, 5, 5));

            taskName.setStyle("-fx-text-fill: #ffffff;-fx-font-size:16px;");
            taskName.setMinWidth(300);
            taskName.setAlignment(Pos.CENTER);

            startDate.setStyle("-fx-text-fill: #ffffff;-fx-font-size:16px;");
            startDate.setMinWidth(100);
            startDate.setAlignment(Pos.CENTER);

            endDate.setStyle("-fx-text-fill: #ffffff;-fx-font-size:16px;");
            endDate.setMinWidth(100);
            endDate.setAlignment(Pos.CENTER);

            status.setStyle("-fx-font-size:16px;-fx-text-fill: #ffffff");
            status.setMinWidth(110);
            status.setAlignment(Pos.CENTER);

            imageView.setFitHeight(20);
            imageView.setFitWidth(20);

            listview_id.setStyle("-fx-background-color: #3e3e41;");

            Reject.setMinWidth(20);
            Reject.setMinHeight(20);
            // Reject.setStyle("-fx-background-color: #3e3e41;");
            Reject.setStyle("-fx-background-color: #3e3e41;");

            Accept.setStyle("-fx-background-color: #3e3e41;-fx-border-color: #ffffff;-fx-text-fill: #ffffff;");
            Accept.setMinWidth(100);
            Accept.setMinHeight(20);

            Accept.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        // Dashbord.MainDashBoardController.username = user;
                        if (getIndex() > Alldata.length() - 1) {
                            UpdateData.UpdateDataController.update = false;
                        }
                        UpdateData.UpdateDataController.details = data.get(getIndex());
                        UpdateData.UpdateDataController.username = username;
                        Parent updateData = FXMLLoader.load(getClass().getResource("/UpdateData/UpdateData.fxml"));
                        Scene dashbord = new Scene(updateData);
                        //This line gets the Stage information

                        Stage window = new Stage();//(Stage) ((Node) event.getSource()).getScene().getWindow();
                        window.initModality(Modality.APPLICATION_MODAL);
                        window.setScene(dashbord);

                        window.setMinWidth(1200);
                        window.setMinHeight(700);

                        /*Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
                        window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
                        window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
                       
                         */ window.showAndWait();
                        getTasks(username);
                        setTasksCount();
                    } catch (IOException ex) {
                        Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            Reject.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    try {
                        if (getIndex() > Alldata.length() - 1) {
                            a = new Alert(Alert.AlertType.ERROR);
                            a.setContentText("You are no allowed to delete this task");
                            a.show();
                        } else {
                            int task_id = data.get(getIndex()).getTask_id();
                            JSONObject delete_task = new JSONObject();
                            delete_task.put("Key", "DeleteTask");
                            delete_task.put("task_id", task_id);
                            String res = ConnectToServer.ServerConnection.ConnectToServer(delete_task);
                            Alert a;
                            if (res.equals("Done")) {
                                a = new Alert(Alert.AlertType.INFORMATION);
                                a.setContentText("Done successfully");
                                a.show();
                                //data.remove(getIndex());
                                getTasks(username);
                            } else {
                                a = new Alert(Alert.AlertType.ERROR);
                                a.setContentText("Faild !!! Check server connection");
                                a.show();

                            }
                        }
                    } catch (JSONException ex) {
                        Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }

    }

    @FXML
    private void goToAddTask(MouseEvent event) {

        try {
            AddTask.AddTaskController.username = username;

            Parent root = FXMLLoader.load(getClass().getResource("/AddTask/AddTask.fxml"));

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setMaxHeight(630);
            stage.setMaxWidth(700);
            stage.setMinHeight(630);
            stage.setMinWidth(700);
            stage.showAndWait();
            getTasks(username);
            getTeamMates(username);
             setTasksCount();

            /*
            root = FXMLLoader.load(getClass().getResource("/AddTask/AddTask.fxml"));

            Scene dashbord = new Scene(root);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            window.initModality(Modality.APPLICATION_MODAL);
Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            window.setScene(dashbord);

            window.setMaxHeight(630);
            window.setMaxWidth(700);
            window.setMinHeight(630);
            window.setMinWidth(700);

            window.show();
//        window.showAndWait();
             */
        } catch (IOException ex) {
            Logger.getLogger(MainDashBoardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getTasks(String username) {
        if (ServerConnection.ServerCheck) {
            try {
                data.clear();
                JSONObject getTask = new JSONObject();
                getTask.put("Key", "getAllTasks");
                getTask.put("username", username);
                String res = ConnectToServer.ServerConnection.ConnectToServer(getTask);

                if (res.equals("ServerError") || res.equals("ErrorWithTheServer")) {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Error in Connection with the Server please check the connection");
                    a.show();
                    ServerConnection.StartConnection();
                } else {

                    JSONObject allTasks = new JSONObject(res);

                    Alldata = allTasks.getJSONArray("data");
                    JSONArray AlldataAssign = allTasks.getJSONArray("TasksAssignToMe");

                    for (int i = 0; i < Alldata.length(); i++) {
                        int username_id = Alldata.getJSONObject(i).getInt("username_id");
                        int task_id = Alldata.getJSONObject(i).getInt("task_id");

                        String title = Alldata.getJSONObject(i).getString("title");
                        String status = Alldata.getJSONObject(i).getString("status");

                        String end_date = Alldata.getJSONObject(i).getString("end_date");
                        String start_date = Alldata.getJSONObject(i).getString("start_date");
                        String background_color = Alldata.getJSONObject(i).getString("background_color");

                        JSONArray items = Alldata.getJSONObject(i).getJSONArray("taskItems");
                        JSONObject comments = Alldata.getJSONObject(i).getJSONObject("taskComments");
                        JSONArray collaborators = Alldata.getJSONObject(i).getJSONArray("taskCollaborators");

                        Task_Details td = new Task_Details(task_id, title, status, start_date, end_date, background_color, username_id, items, comments, collaborators);
                        data.add(td);

                    }
                    for (int i = 0; i < AlldataAssign.length(); i++) {
                        int username_id = AlldataAssign.getJSONObject(i).getInt("username_id");
                        int task_id = AlldataAssign.getJSONObject(i).getInt("task_id");

                        String title = AlldataAssign.getJSONObject(i).getString("title");
                        String status = AlldataAssign.getJSONObject(i).getString("status");

                        String end_date = AlldataAssign.getJSONObject(i).getString("end_date");
                        String start_date = AlldataAssign.getJSONObject(i).getString("start_date");
                        String background_color = AlldataAssign.getJSONObject(i).getString("background_color");

                        JSONArray items = AlldataAssign.getJSONObject(i).getJSONArray("taskItems");
                        JSONObject comments = AlldataAssign.getJSONObject(i).getJSONObject("taskComments");
                        JSONArray collaborators = AlldataAssign.getJSONObject(i).getJSONArray("taskCollaborators");

                        Task_Details td = new Task_Details(task_id, title, status, start_date, end_date, background_color, username_id, items, comments, collaborators);
                        data.add(td);

                    }
                }
            } catch (JSONException ex) {
                Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(AllTasksController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showAlertConnection();
        }
    }

    public void showAlertConnection() {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText("Error in Connection with the Server please check the connection");
        a.show();
        ServerConnection.StartConnection();

    }

    public void getTeamMates(String user) {
        try {
            JSONObject Jsondata = new JSONObject();
            Jsondata.put("Key", "GetTeamMates");
            Jsondata.put("username", user);
            String result = ServerConnection.ConnectToServer(Jsondata);

            if (result.equals("ServerError") || result.equals("ErrorWithTheServer")) {
                a.setAlertType(Alert.AlertType.ERROR);
                a.setContentText("Error in Connection with the Server please check the connection");
                a.show();
                ServerConnection.StartConnection();
            } else {
                JSONObject teamMates = new JSONObject(result);
                JSONArray team = new JSONArray();
                JSONArray statusOnline_Offline = new JSONArray();

                team = teamMates.getJSONArray("username");
                statusOnline_Offline = teamMates.getJSONArray("statusOnline_Offline");
                team_matte_count.setText(String.valueOf(statusOnline_Offline.length()));
            }
        } catch (JSONException ex) {
            Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
