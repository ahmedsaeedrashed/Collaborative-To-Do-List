/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Team;

import ConnectToServer.ServerConnection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author ALMOSTAFA
 */
public class TeamMatesFormController implements Initializable {

    public static String username = "";
    public int index;
    @FXML
    private Button Add_id;

    TextInputDialog td = new TextInputDialog();
    Alert a = new Alert(AlertType.ERROR);
    @FXML
    private Button delete_btn;
    @FXML
    private Label Waiting_error;
    @FXML
    private ListView<ListViewCell> List_Team_mates;
    @FXML
    private ListView<ListViewCell> List_Waiting;
    @FXML
    private Button deleteTeam_btn;
    @FXML
    private Label Team_error;

    /**
     * Initializes the controller class.
     */
    public TeamMatesFormController() {
    }
    ObservableList<ListViewCell> data = FXCollections.observableArrayList();
    ObservableList<ListViewCell> Waiting_list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getTeamMates(username);

        List_Team_mates.setCellFactory(new Callback<ListView<ListViewCell>, ListCell<ListViewCell>>() {
            @Override
            public ListCell<ListViewCell> call(ListView<ListViewCell> studentListView) {
                ListCell<ListViewCell> cell = new ListCell<ListViewCell>() {

                    @Override
                    protected void updateItem(ListViewCell obj, boolean empty) {
                        super.updateItem(obj, empty);
                        if (empty || obj == null) {
                            setText("");
                            setGraphic(null);
                        } else {
                            setText("            " + obj.getTextContent());
                            Image im = new Image(getClass().getResource(obj.getFlag()).toExternalForm());
                            ImageView imageview = new ImageView(im);
                            setGraphic(imageview);

                        }
                    }
                };
                return cell;
            }
        });

        List_Waiting.setCellFactory(new Callback<ListView<ListViewCell>, ListCell<ListViewCell>>() {
            @Override
            public ListCell<ListViewCell> call(ListView<ListViewCell> studentListView) {
                ListCell<ListViewCell> cell = new ListCell<ListViewCell>() {

                    @Override
                    protected void updateItem(ListViewCell obj, boolean empty) {
                        super.updateItem(obj, empty);
                        if (empty || obj == null) {
                            setText("");
                            setGraphic(null);
                        } else {
                            setText("            " + obj.getTextContent());
                            Image im = new Image(getClass().getResource(obj.getFlag()).toExternalForm());
                            ImageView imageview = new ImageView(im);
                            setGraphic(imageview);

                        }
                    }
                };
                return cell;
            }
        });

        List_Team_mates.setItems(data);
        List_Waiting.setItems(Waiting_list);
        List_Waiting.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ListViewCell>() {
            @Override
            public void changed(ObservableValue<? extends ListViewCell> observable, ListViewCell oldValue, ListViewCell newValue) {
                Waiting_error.setVisible(false);

            }
        });
    }

    @FXML
    private void Add_btn(ActionEvent event) throws JSONException {
        Waiting_error.setVisible(false);
        td.setHeaderText("Enter Email ");
        td.showAndWait();
        System.out.println((td.getEditor().getText()));
        String email = td.getEditor().getText();
        System.out.println("email :" + email);
        if (!email.equals("")) {
            boolean validEmail = CheckValiedEmail(email);
            if (validEmail) {
                try {
                    JSONObject checkjson = new JSONObject();
                    checkjson.put("Key", "CheckEmail");
                    checkjson.put("data", email);
                    String res = ServerConnection.ConnectToServer(checkjson);
                    if (res.equals("False")) {
                        a.setAlertType(AlertType.ERROR);
                        a.setContentText("This Email is not Exist");
                        a.show();

                    } else {

                        JSONObject add_team = new JSONObject();
                        add_team.put("Key", "Add_Team");
                        add_team.put("from", username);
                        add_team.put("to", email);
                        String return_res = ServerConnection.ConnectToServer(add_team);
                        //do Action
                        if (return_res.equals("Error")) {
                            a.setAlertType(AlertType.ERROR);
                            a.setContentText("You Have sent a request to this Email Befor");
                            a.show();

                        } else {
                            System.out.println("done");
                            a.setAlertType(AlertType.INFORMATION);
                            a.setContentText("Requset Done");
                            a.show();

                            // update listView
                            getTeamMates(username);
                        }

                    }
                } catch (IOException ex) {
                    Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                a.setAlertType(AlertType.ERROR);
                a.setContentText("This Email is not Valid");
                a.show();
            }
        }

    }

    public boolean CheckValiedEmail(String email) {
        String regex = "^(.+)@(.+)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    public void getTeamMates(String user) {
        try {
            data.clear();
            Waiting_list.clear();
            JSONObject Jsondata = new JSONObject();
            Jsondata.put("Key", "GetTeamMates");
            Jsondata.put("username", user);
            String result = ServerConnection.ConnectToServer(Jsondata);

            if (result.equals("ServerError") || result.equals("ErrorWithTheServer")) {
                a.setAlertType(AlertType.ERROR);
                a.setContentText("Error in Connection with the Server please check the connection");
                a.show();
                ServerConnection.StartConnection();
            } else {
                JSONObject teamMates = new JSONObject(result);
                JSONArray team = new JSONArray();
                JSONArray statusOnline_Offline = new JSONArray();
                JSONArray statusWaiting_Accept = new JSONArray();

                team = teamMates.getJSONArray("username");
                statusOnline_Offline = teamMates.getJSONArray("statusOnline_Offline");
                statusWaiting_Accept = teamMates.getJSONArray("statusWaiting_Accept");

                for (int i = 0; i < team.length(); i++) {
                    String name = team.getString(i);
                    if (statusWaiting_Accept.getInt(i) == 1) {
                        //waiting
                        Waiting_list.add(new ListViewCell(name, "icons8-wait-25.png"));
                    } else if (statusWaiting_Accept.getInt(i) == 2) {
                        if (statusOnline_Offline.getInt(i) == 1) {
                            data.add(new ListViewCell(name, "icons8-online-25.png"));
                        } else {
                            data.add(new ListViewCell(name, "icons8-offline-25.png"));
                        }

                    }
                }
                if (Waiting_list.isEmpty()) {
                    List_Waiting.setPlaceholder(new Label("No Content In List"));
                }
                if (data.isEmpty()) {
                    List_Team_mates.setPlaceholder(new Label("No Content In List"));
                }
            }
        } catch (JSONException ex) {
            Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void delete_Request_Action(ActionEvent event) throws JSONException {
        index = List_Waiting.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Waiting_error.setVisible(true);
            Waiting_error.setText("Select Someone First ");
        } else {
            try {
                String team_name = Waiting_list.get(index).getTextContent();
                System.out.println(team_name);
                JSONObject deleteTeamRequest = new JSONObject();
                deleteTeamRequest.put("Key", "DeleteTeamRequest");
                deleteTeamRequest.put("sender", username);
                deleteTeamRequest.put("receiver", team_name);
                ServerConnection.ConnectToServer(deleteTeamRequest);
                data.clear();
                Waiting_list.clear();

                getTeamMates(username);
            } catch (IOException ex) {
                Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @FXML
    private void delete_Team_Action(ActionEvent event) throws JSONException {
        index = List_Team_mates.getSelectionModel().getSelectedIndex();
        if (index == -1) {
            Team_error.setVisible(true);
            Team_error.setText("Select Someone First ");
        } else {
            try {
                String team_name = data.get(index).getTextContent();
                System.out.println(team_name);
                JSONObject deleteTeamMates = new JSONObject();
                deleteTeamMates.put("Key", "checkCollaboritivity");
                deleteTeamMates.put("username1", username);
                deleteTeamMates.put("username2", team_name);
                String return_data = ServerConnection.ConnectToServer(deleteTeamMates);
                System.out.println("/////");
                System.out.println(return_data);
                if (return_data.equals("True")) {
                    /*
                    a.setAlertType(AlertType.ERROR);
                    a.setContentText("You Cannot Remove this team mate ");
                    a.show();*/

                    Alert alert = new Alert(AlertType.CONFIRMATION, "there ara tasks , Delete  ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        deleteTeamMates.put("Key", "DeleteTeamMates");
                        deleteTeamMates.put("username1", username);
                        deleteTeamMates.put("username2", team_name);
                        ServerConnection.ConnectToServer(deleteTeamMates);
                        a.setAlertType(AlertType.CONFIRMATION);
                        a.setContentText("Success");
                        a.show();
                        data.clear();
                        Waiting_list.clear();
                        //update
                        getTeamMates(username);
                    }
                } else {
                    a.setAlertType(AlertType.CONFIRMATION);
                    a.setContentText("Success");
                    a.show();
                    data.clear();
                    Waiting_list.clear();
                    //update
                    getTeamMates(username);
                }
            } catch (IOException ex) {
                Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
