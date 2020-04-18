package Notification;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FXMLDocumentController extends ListView<String> implements Initializable {

    public static String username = "";
    JSONArray Team_list;
    JSONArray request_list;
    @FXML
    private ListView<ListViewCell> list;

    ObservableList<ListViewCell> data = FXCollections.observableArrayList();
    @FXML
    private Button btn_refresh;
    @FXML
    private ImageView img_refresh;
    Alert a;

    public FXMLDocumentController() {
        getTeamMates(username);
    }

    public FXMLDocumentController(String us) {

        System.out.println("constructor");
        System.out.println(us);
        username = us;
        getTeamMates(us);
        // username = "nadaomar";

        //  data.add(new ListViewCell("noha", "follow.png"));
    }

    @FXML
    private void RefreshAction(ActionEvent event) {
        System.out.println("Refresh");
        getTeamMates(username);
    }

    class Cell extends ListCell<ListViewCell> {

        HBox hbox = new HBox();
        Label userName = new Label();
        Label spaceLabel = new Label();
        Label spaceLabel2 = new Label();
        ImageView imageview = new ImageView();
        Pane pane = new Pane();
        Button Accept = new Button("Accept");
        Button Reject = new Button("Reject");

        @Override
        protected void updateItem(ListViewCell item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
            setText(null);
            setGraphic(null);

            if (item != null && !empty) {
                userName.setText(item.getTextContent());
                spaceLabel.setText("              ");
                spaceLabel2.setText("    ");
                Image im = new Image(getClass().getResource(item.getFlag()).toExternalForm());
                imageview.setImage(im);
                //setGraphic(imageview);
                //  hbox.getChildren().add(imageview);
                setGraphic(hbox);
                // System.out.println("First list view");
                //   setText("            " + item.getTextContent());

            }
        }

        public Cell() {
            super();
            hbox.getChildren().addAll(imageview, spaceLabel2, userName, pane, Accept, spaceLabel, Reject);
            hbox.setHgrow(pane, Priority.ALWAYS);
            hbox.setAlignment(Pos.CENTER);
            Accept.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String content = getItem().getTextContent();
                    String[] arrOfStr_request = content.split(" sent to you add request ", 2);
                    System.out.println("---");
                    System.out.println(arrOfStr_request.length);
                    if (arrOfStr_request.length == 1) {
                        // this a task request
                        String[] arrOfStr_task = content.split(" wants to add you to a task ", 2);

                        if (arrOfStr_task.length == 1) {
                            //Nothing
                        } else {
                            //Accept this Task
                            Task_Request("Accept", arrOfStr_task[1]);
                            //String task_title = arrOfStr_task[1];

                        }
                    } else {
                        //Accept add request
                        Team_Request("Accept", arrOfStr_request[0]);

                    }
                }
            });
            Reject.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String content = getItem().getTextContent();
                    String[] arrOfStr_request = content.split(" sent to you add request ", 2);
                    if (arrOfStr_request.length == 1) {
                        // this a task request
                        String[] arrOfStr_task = content.split(" wants to add you to a task ", 2);

                        if (arrOfStr_task.length == 1) {
                            //Nothing
                        } else {
                            //Accept this Task
                            Task_Request("Reject", arrOfStr_task[1]);
                            //String task_title = arrOfStr_task[1];

                        }
                    } else {
                        //Accept add request
                        Team_Request("Reject", arrOfStr_request[0]);

                    }
                }
            });
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // img_refresh.setImage(new Image(getClass().getClassLoader().getResource("recourse/icons8-refresh-50.png").toExternalForm()));

        list.setItems(data);
        list.setCellFactory(param -> new Cell());

    }

    public void getTeamMates(String user) {
        if (ServerConnection.ServerCheck) {
            try {
                data.clear();

                //   System.out.println(user);
                JSONObject Jsondata = new JSONObject();
                Jsondata.put("Key", "getRequestNotifications");
                Jsondata.put("username", user);
                String result = ServerConnection.ConnectToServer(Jsondata);
                if (result.equals("ServerError") || result.equals("ErrorWithTheServer")) {
                    a.setAlertType(Alert.AlertType.ERROR);
                    a.setContentText("Error in Connection with the Server please check the connection");
                    a.show();
                    ServerConnection.StartConnection();
                } else {
                    JSONObject teamMates = new JSONObject(result);
                    System.out.println("test2");
                    System.out.println(teamMates);

                    Team_list = teamMates.getJSONArray("team_request");
                    System.out.println("test3");
                    request_list = teamMates.getJSONArray("task_request");
                    System.out.println("test4");

                    for (int i = 0; i < Team_list.length(); i++) {
                        JSONObject t = Team_list.getJSONObject(i);
                        System.out.println(t.get("username_send"));
                        data.add(new ListViewCell(t.getString("username_send") + " sent to you add request ", "follow.png"));
                    }
                    for (int i = 0; i < request_list.length(); i++) {
                        JSONObject t = request_list.getJSONObject(i);
                        System.out.println(t.get("usename_sender"));
                        data.add(new ListViewCell(t.getString("usename_sender") + " wants to add you to a task " + t.getString("task_title"), "calendar.png"));

                    }

                }
            } catch (JSONException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
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

    public int getTaskId(String title) throws JSONException {
        int task_id = 0;

        for (int i = 0; i < request_list.length(); i++) {
            JSONObject t = request_list.getJSONObject(i);
            String task_title = t.getString("task_title");
            if (task_title.equals(title)) {
                task_id = t.getInt("task_id");
                return task_id;
            }

        }
        return task_id;
    }

    public int getSender_id(String name) throws JSONException {
        int sender_id = 0;

        for (int i = 0; i < Team_list.length(); i++) {
            JSONObject t = Team_list.getJSONObject(i);
            String sender = t.getString("username_send");
            if (name.equals(sender)) {
                sender_id = t.getInt("username_id_send");
                return sender_id;
            }

        }
        System.out.println("user_id : " + sender_id);
        return sender_id;
    }

    public int getReciver_id(String name) throws JSONException {
        int reciever_id = 0;

        for (int i = 0; i < Team_list.length(); i++) {
            JSONObject t = Team_list.getJSONObject(i);
            String sender = t.getString("username_recive");
            System.out.println("forllllllllllllllllllllllllllllllllll");
            if (name.equals(sender)) {
                reciever_id = t.getInt("username_id_recive");
                System.out.println("if statmentyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
                return reciever_id;
            }

        }
        return reciever_id;
    }

    public void Team_Request(String stat, String Sender_name) {
        try {

            System.out.println("sender_name  : " + Sender_name);
            System.out.println(username);
            int sender_id = getSender_id(Sender_name);
            int reciver_id = getReciver_id(username);
            JSONObject accept = new JSONObject();
            if (stat.equals("Accept")) {
                accept.put("Key", "AcceptTeamMate");
            } else {
                accept.put("Key", "RejectTeamMate");
            }
            accept.put("username_id_send", sender_id);
            accept.put("username_id_recive", reciver_id);
            ServerConnection.ConnectToServer(accept);
            getTeamMates(username);
        } catch (JSONException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Task_Request(String stat, String task_title) {

        try {
            int task_id = getTaskId(task_title);
            System.out.println(task_title);
            System.out.println(task_id);
            int username_id_collaborative = request_list.getJSONObject(0).getInt("username_id_reciver");
            System.out.println(username_id_collaborative);
            JSONObject acceptingTask = new JSONObject();
            if (stat.equals("Accept")) {
                acceptingTask.put("Key", "AcceptTask");
            } else {
                acceptingTask.put("Key", "RejectTask");
            }
            acceptingTask.put("task_id", task_id);
            acceptingTask.put("username_id_collaborative", username_id_collaborative);

            ServerConnection.ConnectToServer(acceptingTask);
            getTeamMates(username);
        } catch (JSONException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
