package server_todo_prpject;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import db.go;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.*;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ServerGUIBase extends AnchorPane implements Runnable {

    protected static TableColumn users;
    protected static TableView<tab_data> table_view;

    protected static ObservableList<tab_data> data
            = FXCollections.observableArrayList();

    public static class tab_data {

        private final SimpleStringProperty firstName;

        private tab_data(String anew) {
            this.firstName = new SimpleStringProperty(anew);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }
    }
    protected final AnchorPane anchorPane;
    protected final JFXToggleButton On_Off_Server;
    protected final AnchorPane anchorPane0;
    protected final CategoryAxis Y_id;
    protected final NumberAxis X_id;
    protected final BarChart barChart;
    protected final AnchorPane anchorPane1;
    protected static JFXTextField Online_count;
    protected final Label label;
    protected static ListView online_users;

    public static Dictionary geek = new Hashtable();
    public static int index = 0;

    public ServerGUIBase() {

        Thread th = new Thread(this);
        th.start();

        anchorPane = new AnchorPane();
        On_Off_Server = new JFXToggleButton();
        anchorPane0 = new AnchorPane();
        Y_id = new CategoryAxis();
        X_id = new NumberAxis();
        barChart = new BarChart(Y_id, X_id);
        anchorPane1 = new AnchorPane();
        Online_count = new JFXTextField();
        label = new Label();
        online_users = new ListView();
        table_view = new TableView<tab_data>();
        users = new TableColumn("Users");
        users.setCellValueFactory(new PropertyValueFactory<tab_data, String>("firstName"));
        table_view.setItems(data);
        //table_view.getColumns().addAll(users);

        setId("AnchorPane");
        setPrefHeight(395.0);
        setPrefWidth(700.0);

        AnchorPane.setBottomAnchor(anchorPane, -3.0);
        AnchorPane.setLeftAnchor(anchorPane, 0.0);
        AnchorPane.setRightAnchor(anchorPane, 0.0);
        anchorPane.setLayoutY(328.0);
        anchorPane.setPrefHeight(70.0);
        anchorPane.setPrefWidth(700.0);

        On_Off_Server.setLayoutX(326.0);
        On_Off_Server.setLayoutY(26.0);
        On_Off_Server.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

            }
        });

        anchorPane0.setLayoutX(311.0);
        anchorPane0.setLayoutY(46.0);
        anchorPane0.setPrefHeight(282.0);
        anchorPane0.setPrefWidth(375.0);

        Y_id.setId("x_id");
        Y_id.setLabel("status");
        Y_id.setSide(javafx.geometry.Side.BOTTOM);

        X_id.setId("y_id");
        X_id.setLabel("numbers");
        X_id.setSide(javafx.geometry.Side.LEFT);
        barChart.setId("graph_id");
        barChart.setLayoutX(3.0);
        barChart.setPrefHeight(282.0);
        barChart.setPrefWidth(369.0);
        barChart.setTitle("Tasks");

        anchorPane1.setLayoutX(14.0);
        anchorPane1.setLayoutY(59.0);
        anchorPane1.setPrefHeight(291.0);
        anchorPane1.setPrefWidth(297.0);

        Online_count.setLayoutX(7.0);
        Online_count.setLayoutY(40.0);

        label.setLayoutX(7.0);
        label.setLayoutY(14.0);
        label.setPrefHeight(26.0);
        label.setPrefWidth(167.0);
        label.setText("Online Clients :");

        online_users.setLayoutX(7.0);
        online_users.setLayoutY(91.0);
        online_users.setPrefHeight(200.0);
        online_users.setPrefWidth(121.0);

        table_view.setLayoutX(97.0);
        table_view.setLayoutY(91.0);
        table_view.setPrefHeight(200.0);
        table_view.setPrefWidth(200.0);

        users.setPrefWidth(75.0);
        users.setText("C1");

        anchorPane.getChildren().add(On_Off_Server);
        getChildren().add(anchorPane);
        anchorPane0.getChildren().add(barChart);
        getChildren().add(anchorPane0);
        anchorPane1.getChildren().add(Online_count);
        anchorPane1.getChildren().add(label);
        anchorPane1.getChildren().add(online_users);
        table_view.getColumns().add(users);
        anchorPane1.getChildren().add(table_view);
        getChildren().add(anchorPane1);
        updateGraph();

    }

    @Override
    public void run() {
        try {

            ServerSocket ss = new ServerSocket(5005);

            // running infinite loop for getting
            // client request
            while (true) {

                Socket s = null;
                try {
                    // socket object to receive incoming client requests
                    s = ss.accept();
                    
                    System.out.println("A new client is connected : " + s);

                    // obtaining input and out streams
                    DataInputStream dis = new DataInputStream(s.getInputStream());
                    DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                    System.out.println("Assigning new thread for this client");

                    // create a new thread object
                    Thread t = new ClientHandler(s, dis, dos);

                    // Invoking the start() method
                    t.start();
                    //  Online(ClientHandler.clientsVector.size());
                } catch (Exception e) {
                    s.close();
                    e.printStackTrace();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerGUIBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /* public static void Online(int num) {
        Online_count.setText("# " + num);
    }*/
    public void updateGraph() {
        XYChart.Series data = new XYChart.Series<>();
        int done = go.getStatueCount("done", "server");
        int todo = go.getStatueCount("todo", "server");
        int inprogress = go.getStatueCount("inprogress", "server");
        data.getData().add(new XYChart.Data("done", done));
        data.getData().add(new XYChart.Data("inprogress", inprogress));
        data.getData().add(new XYChart.Data("todo", todo));

        barChart.getData().addAll(data);
    }

    public static void onlineUser(String user) {
        geek.put(user, index);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //  online_users.getItems().add(user);
                data.add(new tab_data(user));

            }
        });
        index++;
    }

    public static void offlineUser(String user) {
        // int id = (int) geek.get(user);
        //System.out.println("id = " + id);
        //  online_users.getSelectionModel().;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //online_users.getItems().remove(id);
                for (int i = 0; i < data.size(); i++) {
                    tab_data item = table_view.getItems().get(i);

                    // this gives the value in the selected cell:
                    String data2 = (String) users.getCellObservableValue(item).getValue();
                    if (data2.equals(user)) {
                        System.out.println(data2);
                        data.remove(i);

                    }

                }
            }
        });
        System.out.println("test1");
        geek.remove(user);
        System.out.println("test2");
    }

}
