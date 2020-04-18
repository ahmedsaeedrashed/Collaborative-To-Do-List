/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AddTask;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author ALMOSTAFA
 */
public class AddTaskController implements Initializable {

    @FXML
    private TextField TaskName;
    @FXML
    private DatePicker DeadLine;
    @FXML
    private ToggleGroup status;
    @FXML
    private Button AddTask;
    @FXML
    private ListView<String> AllItemsListView;
    @FXML
    private TextField AdditemsFeild;
    @FXML
    private Button AddItem;
    @FXML
    private Button DeleteItem;
    @FXML
    private Button addCollaborative;
    @FXML
    private Text TaskNameError;
    @FXML
    private Text DateError;
    @FXML
    private DatePicker SartDate;
    Date date = new Date();
    public static String username;

    Alert alert;
    @FXML
    private ColorPicker task_color;
    @FXML
    private RadioButton todo_id;
    @FXML
    private RadioButton inprogress_id;
    @FXML
    private RadioButton done_id;
    Alert a = new Alert(Alert.AlertType.ERROR);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Collaborative.CollaborativeTeam.Team_list.clear();
    }

    @FXML
    private void TaskNameOnClick(MouseEvent event) {
        ClearTextError();
    }

    @FXML
    private void DeadLineOnClick(MouseEvent event) {
        ClearTextError();
    }

    @FXML
    private void AddTaskAction(ActionEvent event) throws IOException {
        if (TaskName.getText().trim().isEmpty() == false && SartDate.getValue() != null && DeadLine.getValue() != null) {
            if (ServerConnection.ServerCheck) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    String SatrtDate = SartDate.getValue().toString();
                    String DeadDate = DeadLine.getValue().toString();

                    Date d1 = null;
                    Date d2 = null;

                    d1 = dateFormat.parse(SatrtDate);
                    d2 = dateFormat.parse(DeadDate);

                    if (d1.compareTo(d2) < 0) {
                        //Save Task

                        JSONArray items_list = new JSONArray();
                        JSONArray collaboratives = new JSONArray();

                        if (AllItemsListView.getItems().isEmpty() == false) {
                            ObservableList list = AllItemsListView.getItems();
                            for (Object o : list) {
                                items_list.put(o.toString());
                            }
                        }
                        if (!(Collaborative.CollaborativeTeam.Team_list.isEmpty())) {
                            List<String> Team_list = Collaborative.CollaborativeTeam.Team_list;
                            for (int i = 0; i < Team_list.size(); i++) {
                                collaboratives.put(Team_list.get(i));
                            }
                        }
                        String TaskTitle = TaskName.getText();
                        Color TaskColor = task_color.getValue();
                        String statue = "todo";
                        if (todo_id.isSelected()) {
                            statue = "todo";
                        } else if (inprogress_id.isSelected()) {
                            statue = "inprogress";
                        } else if (done_id.isSelected()) {
                            statue = "done";
                        }
                        JSONObject addTask = new JSONObject();
                        JSONArray data = new JSONArray();
                        addTask.put("Key", "InsertTasks");
                        data.put(TaskTitle);
                        data.put(statue);
                        data.put(SatrtDate);
                        data.put(DeadDate);
                        data.put(TaskColor);
                        data.put(username);
                        addTask.put("data", data);
                        addTask.put("Items", items_list);
                        addTask.put("Collaboratives", collaboratives);
                        String result = ServerConnection.ConnectToServer(addTask);
                        if (result.equals("True")) {
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setContentText(" Task Is Added Successfuly ");
                            alert.setHeaderText("Message");
                            alert.setTitle("Message");
                            alert.show();

                            Collaborative.CollaborativeTeam.Team_list.clear();
                            // closeing window
                            final Node source = (Node) event.getSource();
                            final Stage stage = (Stage) source.getScene().getWindow();
                            stage.close();

                        } else if (result.equals("True")) {
                            alert = new Alert(Alert.AlertType.ERROR);
                            alert.setContentText("Cannot Connect To the Server ,Please Check the connection");
                            alert.setHeaderText("ERROR");
                            alert.setTitle("Message");
                            alert.show();
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
                }
            } else {
                showAlertConnection();
            }
        } else {
            DateError.setVisible(true);
            TaskNameError.setVisible(true);

        }
    }

    public void showAlertConnection() {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setContentText("Error in Connection with the Server please check the connection");
        a.show();
        ServerConnection.StartConnection();

    }

    @FXML
    private void AddItemAction(ActionEvent event) {
        String s = AdditemsFeild.getText();
        if (s.trim().isEmpty() == false) {
            AllItemsListView.getItems().add(s);
            AdditemsFeild.setText("");
        }
    }

    @FXML
    private void DeleteItemAction(ActionEvent event) {
        ObservableList selectedIndices = AllItemsListView.getSelectionModel().getSelectedIndices();
        for (Object o : selectedIndices) {

            AllItemsListView.getItems().remove(o.hashCode());
        }
    }

    @FXML
    private void AddCollaborativeAction(ActionEvent event) {
        if (ServerConnection.ServerCheck) {
            try {
                Collaborative.CollaborativeController.username = username;
                Parent root = FXMLLoader.load(getClass().getResource("/Collaborative/Collaborative.fxml"));
 
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(AddTaskController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            showAlertConnection();
        }
    }

    @FXML
    private void StartDateOnClick(MouseEvent event) {
        ClearTextError();
    }

    private void ClearTextError() {
        DateError.setVisible(false);
        TaskNameError.setVisible(false);
    }

}
