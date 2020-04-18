/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Collaborative;

import ConnectToServer.ServerConnection;
import Team.TeamMatesFormController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author ALMOSTAFA
 */
public class CollaborativeController implements Initializable {

    @FXML
    private AnchorPane CollaborativePage;
    @FXML
    private ListView<String> ListViewTeamMate;
    @FXML
    private ListView<String> ListViewCollaborative;
    @FXML
    private Text TextError;
    @FXML
    private ImageView image_View_right;
    @FXML
    private ImageView image_view_left;
    @FXML
    private Button AddBtn;
    @FXML
    private Button RemoveBtn;
    @FXML
    private Button SaveBtn;

    ObservableList<String> itemsTeamMate;
    ObservableList<String> itemsCollaborators;
    public static String username;
    Alert a = new Alert(Alert.AlertType.ERROR);
    //  public static List<String> Team_list;

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        itemsTeamMate = FXCollections.observableArrayList();
        itemsCollaborators = FXCollections.observableArrayList();

        getTeamMates(username);
        ListViewTeamMate.setItems(itemsTeamMate);

        //set your collaborative 
        List<String> Team_list = Collaborative.CollaborativeTeam.Team_list;
        if (!(Team_list.isEmpty())) {
            for (int i = 0; i < Team_list.size(); i++) {
                itemsCollaborators.add(Team_list.get(i));
                System.out.println(Team_list.get(i));
            }
            ListViewCollaborative.setItems(itemsCollaborators);
        }

        image_View_right.setImage(new Image(getClass().getClassLoader().getResource("recourse/right.png").toExternalForm()));
        image_view_left.setImage(new Image(getClass().getClassLoader().getResource("recourse/left.png").toExternalForm()));
        RemoveBtn.setGraphic(image_view_left);
        AddBtn.setGraphic(image_View_right);

        ListViewTeamMate.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                TextError.setVisible(false);
            }
        });
        if (itemsCollaborators.isEmpty()) {
            ListViewCollaborative.setPlaceholder(new Label("No Content In List"));
        }
    }

    @FXML
    private void AddBtnAction(ActionEvent event) {
        System.out.println("right");

        ObservableList selectedIndices = ListViewTeamMate.getSelectionModel().getSelectedIndices();

        for (Object o : selectedIndices) {

            System.out.println(itemsTeamMate.get(o.hashCode()));

            int x = 1;
            for (String k : itemsCollaborators) {
                if (itemsTeamMate.get(o.hashCode()).equals(k)) {
                    System.out.println("k = " + k);
                    x = 0;
                    break;
                }
            }
            if (x == 1) {
                // ListViewCollaborative.getItems().add(itemsTeamMate.get(o.hashCode()));
                itemsCollaborators.add(itemsTeamMate.get(o.hashCode()));
            } else {
                TextError.setVisible(true);
            }
        }
        ListViewCollaborative.setItems(itemsCollaborators);
    }

    @FXML
    private void RemoveBtnAction(ActionEvent event) {
        System.out.println("left");

        int selectedIndices = ListViewCollaborative.getSelectionModel().getSelectedIndex();
        System.out.println("selected index = " + selectedIndices);
        if (selectedIndices != -1) {
            ListViewCollaborative.getItems().remove(selectedIndices);
            System.out.println(itemsCollaborators.size());
            //   itemsCollaborators.remove(selectedIndices);
        }/*
        for (Object o : selectedIndices) {
            ListViewCollaborative.getItems().remove(o.hashCode());
            itemsCollaborators.remove(o.hashCode());
        }
         */
    }

    @FXML
    private void SaveBtnAction(ActionEvent event) {
        // System.out.println(CollaborativeTeam.myList.length);
        //  CollaborativeTeam.team =  new String[0];
        CollaborativeTeam.Team_list.clear();
        CollaborativeTeam.username = username;
        for (int i = 0; i < itemsCollaborators.size(); i++) {
            String name = itemsCollaborators.get(i);
            CollaborativeTeam.Team_list.add(name);

        }
        for (int i = 0; i < CollaborativeTeam.Team_list.size(); i++) {
            System.out.println(CollaborativeTeam.Team_list.get(i));
        }

        // closeing window
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    public void getTeamMates(String user) {
        if (ServerConnection.ServerCheck) {
            try {
                itemsTeamMate.clear();

                //   System.out.println(user);
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
                    //    System.out.println(result);
                    JSONObject teamMates = new JSONObject(result);
                    JSONArray team = new JSONArray();
                    JSONArray statusOnline_Offline = new JSONArray();
                    JSONArray statusWaiting_Accept = new JSONArray();

                    team = teamMates.getJSONArray("username");
                    statusOnline_Offline = teamMates.getJSONArray("statusOnline_Offline");
                    statusWaiting_Accept = teamMates.getJSONArray("statusWaiting_Accept");

                    for (int i = 0; i < team.length(); i++) {
                        String name = team.getString(i);
                        if (statusWaiting_Accept.getInt(i) == 2) {

                            itemsTeamMate.add(name);

                        }
                    }
                    if (itemsTeamMate.isEmpty()) {
                        ListViewTeamMate.setPlaceholder(new Label("No Content In List"));
                    }
                }

            } catch (JSONException ex) {
                Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
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

    /*
    public void getTeamMates(String user) {
        try {

            JSONObject Jsondata = new JSONObject();
            Jsondata.put("Key", "GetTeamMates");
            System.out.println(user);
            Jsondata.put("username", user);
            String result = ServerConnection.ConnectToServer(Jsondata);

            //    System.out.println(result);
            JSONObject teamMates = new JSONObject(result);
            JSONArray team = new JSONArray();
            JSONArray status = new JSONArray();

            team = teamMates.getJSONArray("username_id_recive");
            status = teamMates.getJSONArray("status");

            for (int i = 0; i < team.length(); i++) {
                String name = team.getString(i);
                if (status.getInt(i) != 1) {
                    itemsTeamMate.add(name);
                }
            }
            if (itemsTeamMate.isEmpty()) {
                ListViewTeamMate.setPlaceholder(new Label("No Content In List"));
            }

        } catch (JSONException ex) {
            Logger.getLogger(TeamMatesFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CollaborativeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     */
}
