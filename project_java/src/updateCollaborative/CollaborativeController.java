/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package updateCollaborative;

import Collaborative.*;
import ConnectToServer.ServerConnection;
import Team.TeamMatesFormController;
import java.io.IOException;
import java.net.URL;
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

    public JSONArray coll_team = new JSONArray();
    public JSONArray Remove_collaboritry = new JSONArray();
    public JSONArray Send_notification = new JSONArray();
    //  public static List<String> Team_list;

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initiazle Collaborative Controller ");
        System.out.println("Username " + username);
        itemsTeamMate = FXCollections.observableArrayList();
        itemsCollaborators = FXCollections.observableArrayList();

        getTeamMates(username);
        ListViewTeamMate.setItems(itemsTeamMate);

        //set your collaborative 
        JSONArray Team_list = updateCollaborative.UpdateCollaborativeTeam.Team_list;
        for (int i = 0; i < Team_list.length(); i++) {
            try {
                coll_team.put(Team_list.get(i));
                System.out.println("team : " + Team_list.get(i));
            } catch (JSONException ex) {
                Logger.getLogger(CollaborativeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (Team_list.length() != 0) {
            for (int i = 0; i < Team_list.length(); i++) {
                try {
                    itemsCollaborators.add(Team_list.getString(i));
                } catch (JSONException ex) {
                    Logger.getLogger(CollaborativeController.class.getName()).log(Level.SEVERE, null, ex);
                }

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
    private void AddBtnAction(ActionEvent event) throws JSONException {

        ObservableList selectedIndices = ListViewTeamMate.getSelectionModel().getSelectedIndices();

        for (Object o : selectedIndices) {

            int x = 1;
            for (String k : itemsCollaborators) {
                if (itemsTeamMate.get(o.hashCode()).equals(k)) {
                    x = 0;
                    break;
                }
            }
            if (x == 1) {
                boolean flag = false;
                for (int i = 0; i < coll_team.length(); i++) {
                    //   System.out.println("selected :" + itemsTeamMate.get(o.hashCode()));
                    if (coll_team.getString(i).equals(itemsTeamMate.get(o.hashCode()))) {
                        flag = true;
                        for (int j = 0; j < Remove_collaboritry.length(); j++) {
                            if (Remove_collaboritry.getString(j).equals(itemsTeamMate.get(o.hashCode()))) {
                                System.out.println("Remove from remove : " + Remove_collaboritry.getString(j));
                                Remove_collaboritry.remove(j);
                                break;
                            }
                        }

                    }
                }
                if (flag == false) {
                    Send_notification.put(itemsTeamMate.get(o.hashCode()));
                    System.out.println("send :" + itemsTeamMate.get(o.hashCode()));
                }

                itemsCollaborators.add(itemsTeamMate.get(o.hashCode()));
            } else {
                TextError.setVisible(true);
            }
        }
        ListViewCollaborative.setItems(itemsCollaborators);
    }

    @FXML
    private void RemoveBtnAction(ActionEvent event) throws JSONException {
        // System.out.println("left");

        int selectedIndices = ListViewCollaborative.getSelectionModel().getSelectedIndex();
        //System.out.println("selected index = " + selectedIndices);
        if (selectedIndices != -1) {
            boolean flag = false;
            for (int i = 0; i < coll_team.length(); i++) {
                if (coll_team.getString(i).equals(itemsCollaborators.get(selectedIndices))) {
                    flag = true;
                    System.out.println("add  to remove  : " + itemsCollaborators.get(selectedIndices));
                    Remove_collaboritry.put(itemsCollaborators.get(selectedIndices));
                }
            }
            if (flag == false) {
                for (int j = 0; j < Send_notification.length(); j++) {
                    System.out.println(flag);
                    if (Send_notification.getString(j).equals(itemsTeamMate.get(selectedIndices))) {
                        System.out.println("remove from notification : " + Send_notification.get(j));
                        Send_notification.remove(j);
                    }
                }
            }
            ListViewCollaborative.getItems().remove(selectedIndices);
            System.out.println(itemsCollaborators.size());
        }

    }

    @FXML
    private void SaveBtnAction(ActionEvent event) throws JSONException {

        int startingLength = UpdateCollaborativeTeam.Team_list.length();
        int startingLength2 = UpdateCollaborativeTeam.Send_notification.length();
        int startingLength3 = UpdateCollaborativeTeam.Remove_collaboritry.length();

        for (int i = startingLength - 1; i >= 0; i--) {
            UpdateCollaborativeTeam.Team_list.remove(i);
        }
        for (int i = startingLength2 - 1; i >= 0; i--) {
            UpdateCollaborativeTeam.Send_notification.remove(i);
        }
        for (int i = startingLength3 - 1; i >= 0; i--) {
            UpdateCollaborativeTeam.Remove_collaboritry.remove(i);
        }

        for (int i = 0; i < itemsCollaborators.size(); i++) {
            String name = itemsCollaborators.get(i);
            UpdateCollaborativeTeam.Team_list.put(name);
        }
        
        //UpdateCollaborativeTeam.Team_list = coll_team;
        
        UpdateCollaborativeTeam.Send_notification = Send_notification;
        UpdateCollaborativeTeam.Remove_collaboritry = Remove_collaboritry;
        
        System.out.println("remove");
        for (int i = 0; i < Remove_collaboritry.length(); i++) {
            System.out.println(Remove_collaboritry.get(i));
        }
        System.out.println("team");
        for (int i = 0; i < coll_team.length(); i++) {
            System.out.println(coll_team.getString(i));
        }
        System.out.println("Send");
        for (int i = 0; i < Send_notification.length(); i++) {
            System.out.println(Send_notification.getString(i));
        }
        System.out.println("new coll");
        for (int i = 0; i < itemsCollaborators.size(); i++) {
            System.out.println(itemsCollaborators.get(i));
        }
        // closeing window
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

    }

    public void getTeamMates(String user) {
        try {
            itemsTeamMate.clear();

            //   System.out.println(user);
            JSONObject Jsondata = new JSONObject();
            Jsondata.put("Key", "GetTeamMates");
            Jsondata.put("username", user);
            String result = ServerConnection.ConnectToServer(Jsondata);

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

        } catch (JSONException ex) {
            Logger.getLogger(TeamMatesFormController.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(TeamMatesFormController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
