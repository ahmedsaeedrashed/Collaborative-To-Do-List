/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UpdateData;

import AddTask.AddTaskController;
import AllTasks.Task_Details;
import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import updateCollaborative.*;

/**
 * FXML Controller class
 *
 * @author Ahmed Saeed
 */
public class UpdateDataController implements Initializable {

    @FXML
    private TextField TaskNameUpdate;
    @FXML
    private DatePicker StartDateUpdate;
    @FXML
    private DatePicker EndDateUpdate;

    @FXML
    private ListView<String> AllItemsListUpdate;
    @FXML
    private TextField AdditemsUpdate;
    @FXML
    private Button AddItemBtn;
    @FXML
    private Button DeleteItemBtn;
    @FXML
    private ListView<String> listOfComments;

    @FXML
    private TextArea commentTextFeild;
    @FXML
    private Button sendBtnComment;
    @FXML
    private HBox ContainerOfAllCollaborative;
    @FXML
    private Button updateCollaborative;
    @FXML
    private Button UpdateTask;
    @FXML
    private Text taskNameError;
    @FXML
    private Text startDateError;
    @FXML
    private Text endDateError;
 
    public static Task_Details details;
    @FXML
    private ColorPicker background_id;
    @FXML
    private AnchorPane DetailsBord;

    public static String username;
    @FXML
    private ToggleGroup selectStatus;
    @FXML
    private RadioButton todo_id;
    @FXML
    private RadioButton progress_id;
    @FXML
    private RadioButton done_id;
    public static boolean update =true ;
    Alert a = new Alert(Alert.AlertType.ERROR);

    @Override
    public void initialize(URL url, ResourceBundle rb) {

      //  DetailsBord.setStyle("-fx-background-color: " + details.getBackground_color() + ";");
        try {
            if (ServerConnection.ServerCheck) {
                //set titile
                TaskNameUpdate.setText(details.getTitle());
                // set date
                StartDateUpdate.setValue(LocalDate.parse(details.getStart_date()));
                EndDateUpdate.setValue(LocalDate.parse(details.getEnd_date()));
                //set items of task
                for (int i = 0; i < details.getTaskItems().length(); i++) {
                    try {
                        AllItemsListUpdate.getItems().add(details.getTaskItems().getString(i));
                    } catch (JSONException ex) {
                        Logger.getLogger(UpdateDataController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // set comments 
                JSONObject co = details.getTaskComments();
                JSONArray comment_creator = co.getJSONArray("Creator");
                JSONArray comment = co.getJSONArray("comment");

                for (int i = 0; i < comment.length(); i++) {
                 
                    listOfComments.getItems().add(comment_creator.getString(i) + "   \n" + comment.getString(i));
                 
                
                }
                //background color
                String stat = details.getStatus();
                System.out.println("stattt :"+stat);
                if (stat.equals("todo")) {
                    todo_id.setSelected(true);
                }else if(stat.equals("inprogress"))
                {
                    progress_id.setSelected(true);
                }else{
                    done_id.setSelected(true);
                }
                background_id.setValue(Color.web(details.getBackground_color()));

                //hbox.getChildren().addAll();
                for (int i = 0; i < details.getTaskCollaborators().length(); i++) {
                    Label name = new Label(details.getTaskCollaborators().getString(i));
                    name.setStyle("-fx-font-size:16px;-fx-text-fill: #ffffff");
                    name.setMinWidth(50);
                    name.setTextAlignment(TextAlignment.CENTER);
                    ImageView img = new ImageView(new Image("/recourse/male.png"));
                    img.setFitHeight(50);
                    img.setFitWidth(50);
                    VBox v = new VBox(img, name);
                    ContainerOfAllCollaborative.getChildren().add(v);
                    // nodes[i] = FXMLLoader.load(getClass().getResource("collaborativeShape.fxml"));
                    //        ContainerOfAllCollaborative.getChildren().add(nodes[i]);

                }
            } else {
                showAlertConnection();
            }
        } catch (JSONException ex) {
            showAlertConnection();
            //Logger.getLogger(UpdateDataController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void showAlertConnection() {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText("Error in Connection with the Server please check the connection");
        a.show();
        ServerConnection.StartConnection();

    }

    @FXML
    private void SelectTaskName(MouseEvent event) {

        taskNameError.setVisible(false);
        startDateError.setVisible(false);
        endDateError.setVisible(false);
    }

    @FXML
    private void SelectStartDate(MouseEvent event) {
        taskNameError.setVisible(false);
        startDateError.setVisible(false);
        endDateError.setVisible(false);
    }

    @FXML
    private void SelectEndDate(MouseEvent event) {
        taskNameError.setVisible(false);
        startDateError.setVisible(false);
        endDateError.setVisible(false);
    }

    @FXML
    private void SelectAddItem(MouseEvent event) {
        String s = AdditemsUpdate.getText();
        if (s.trim().isEmpty() == false) {
            AllItemsListUpdate.getItems().add(s);
            AdditemsUpdate.setText("");
        }

    }

    @FXML
    private void selectDeleteItem(MouseEvent event) {
        ObservableList selectedIndices = AllItemsListUpdate.getSelectionModel().getSelectedIndices();
        for (Object o : selectedIndices) {
            AllItemsListUpdate.getItems().remove(o.hashCode());
        }
    }

    @FXML
    private void SelectCollaborativeUpdate(MouseEvent event) {
        try {
            if (ServerConnection.ServerCheck) {
                UpdateCollaborativeTeam.Team_list = details.getTaskCollaborators();
                CollaborativeController.username = username;
                System.out.println("user name : "+username);
                Parent root = FXMLLoader.load(getClass().getResource("/updateCollaborative/Collaborative.fxml"));

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } else {
                showAlertConnection();
            }
        } catch (IOException ex) {
            Logger.getLogger(AddTaskController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void sendComment(MouseEvent event) {

        if (commentTextFeild.getText().trim().isEmpty() == false) {
            if (ServerConnection.ServerCheck) {
                try {
                    // System.out.println("send");
                    listOfComments.getItems().add("" + username + " \n" + commentTextFeild.getText().trim());
                    
                    JSONObject Comment = new JSONObject();
                    Comment.put("Key", "insertNewComment");
                    Comment.put("comment", commentTextFeild.getText().trim());
                    Comment.put("creator", username);
                    Comment.put("task_id", details.getTask_id());
                    String result = ServerConnection.ConnectToServer(Comment);
                    if (result.equals("ServerError") || result.equals("ErrorWithTheServer")) {
                        a.setAlertType(Alert.AlertType.ERROR);
                        a.setContentText("Error in Connection with the Server please check the connection");
                        a.show();
                        ServerConnection.StartConnection();
                    } else {

                    }
                    commentTextFeild.setText("");
                } catch (JSONException ex) {
                    Logger.getLogger(UpdateDataController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UpdateDataController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                showAlertConnection();
            }
        }
    }
    Alert alert;

    @FXML
    private void SelectUpdate(ActionEvent event) {
        if (TaskNameUpdate.getText().trim().isEmpty() == false && StartDateUpdate.getValue() != null && EndDateUpdate.getValue() != null) {
            try {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String SatrtDate = StartDateUpdate.getValue().toString();
                String DeadDate = EndDateUpdate.getValue().toString();

                Date d1 = null;
                Date d2 = null;

                d1 = dateFormat.parse(SatrtDate);
                d2 = dateFormat.parse(DeadDate);

                if (d1.compareTo(d2) < 0) {
                    //Save Task

                    System.out.println("Saving Task");
                    JSONArray items_list = new JSONArray();
                    JSONArray collaboratives = new JSONArray();

                    if (AllItemsListUpdate.getItems().isEmpty() == false) {
                        ObservableList list = AllItemsListUpdate.getItems();
                        for (Object o : list) {
                            System.out.println("o :" + o);
                            items_list.put(o.toString());
                        }
                    }
                    if (!(Collaborative.CollaborativeTeam.Team_list.isEmpty())) {
                        List<String> Team_list = Collaborative.CollaborativeTeam.Team_list;
                        for (int i = 0; i < Team_list.size(); i++) {
                            System.out.println(Team_list.get(i));
                            collaboratives.put(Team_list.get(i));
                        }
                    }
                    String TaskTitle = TaskNameUpdate.getText();
                    Color TaskColor = background_id.getValue();
                    System.out.println(TaskColor);
                    String statue = "todo";
                    if (todo_id.isSelected()) {
                        statue = "todo";
                    } else if (progress_id.isSelected()) {
                        statue = "inprogress";
                    } else if (done_id.isSelected()) {
                        statue = "done";
                    }
                    JSONObject addTask = new JSONObject();
                    JSONArray data = new JSONArray();
                    addTask.put("Key", "UpdateTasks");
                    data.put(TaskTitle);
                    data.put(statue);
                    data.put(SatrtDate);
                    data.put(DeadDate);
                    data.put(TaskColor);
                    data.put(username);
                    addTask.put("task_id", details.getTask_id());
                    addTask.put("data", data);
                    addTask.put("Items", items_list);
                    
                    addTask.put("Notification", UpdateCollaborativeTeam.Send_notification);
                    addTask.put("RemoveCollaborative",UpdateCollaborativeTeam.Remove_collaboritry);
                    String result = ServerConnection.ConnectToServer(addTask);
                    if (result.equals("True")) {
                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setContentText(" Task Is Updated Successfuly ");
                        alert.setHeaderText("Message");
                        alert.setTitle("Message");
                        alert.show();

                        Collaborative.CollaborativeTeam.Team_list.clear();
                        // closeing window
                        final Node source = (Node) event.getSource();
                        final Stage stage = (Stage) source.getScene().getWindow();
                        stage.close();

                    } else {
                        // System.out.println("errrrrrrrrrrrrrrrrrrrrror");
                    }

                } else {
                    alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText(" DeadLine Date is Wrong !!");
                    alert.setHeaderText("Warning Message");
                    alert.setTitle("Warning");
                    alert.show();
                }
            } catch (ParseException ex) {
                Logger.getLogger(AddTaskController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(AddTaskController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(UpdateDataController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            startDateError.setVisible(true);
            endDateError.setVisible(true);
            taskNameError.setVisible(true);
        }
    }

}
