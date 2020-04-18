/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_todo_prpject;

import db.go;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ALMOSTAFA
 */
class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    static Vector<ClientHandler> clientsVector = new Vector<ClientHandler>();

    // Constructor 
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos) {
        this.s = s;
        this.dis = dis;
        this.dos = dos;

        clientsVector.add(this);
        // ServerGUIBase.Online(clientsVector.size());
        System.out.println("number of clients : " + clientsVector.size());
    }

    @Override
    public void run() {
        try {
            String received;
            String toreturn;
            String username = "";
            while (true) {
                try {

                    // receive the answer from client
                    received = dis.readUTF();

                    if (received.equals("Exit")) {

                        System.out.println("Client " + this.s + " sends exit...");
                        System.out.println("Closing this connection.");

                        this.s.close();
                        System.out.println(this.clientsVector.indexOf(this));
                        System.out.println("**************");
                        System.out.println(username);
                        ServerGUIBase.offlineUser(username);
                        String off = "UPDATE users SET status=0 WHERE user_name='" + username + "'";
                        System.out.println("off = " + off);
                        go.runQ(off);
                        System.out.println("test3");

                        System.out.println("test4");
                        this.clientsVector.remove(this);
                        System.out.println("test5");

                        System.out.println("Connection closed");
                        break;

                    } else {

                        JSONObject obj = new JSONObject(received);
                        String key = obj.getString("Key");
                        if (key.equals("CheckUserName")) {
                            String checkUsername = obj.getString("data");
                            boolean isLogged = db.go.checkUsernameIsExist(checkUsername);
                            if (isLogged) {
                                dos.writeUTF("True");
                            } else {
                                username = checkUsername;
                                String on = "UPDATE users SET status=1 WHERE user_name='" + checkUsername + "'";
                                System.out.println("statment on = " + on);
                                go.runQ(on);
                                dos.writeUTF("False");
                                ServerGUIBase.onlineUser(checkUsername);

                            }
                        } else if (key.equals("CheckEmail")) {
                            String checkEmail = obj.getString("data");
                            boolean isLogged = db.go.checkEmailIsExist(checkEmail);
                            if (isLogged) {  //Integer.toString(db.go.GetUserId(checkEmail, "mail"))
                                dos.writeUTF("True");
                            } else {
                                dos.writeUTF("False");

                            }
                        } else if (key.equals("Add_Team")) {
                            String from = obj.getString("from");
                            String to = obj.getString("to");
                            int user_from = db.go.GetUserId(from, "user_name");
                            int user_to = db.go.GetUserId(to, "mail");
                            String sqlstmt = "insert into team_mate (username_id_send, username_id_recive, status) values (" + user_from + "," + user_to + "," + "1)";
                            int res = db.go.runQ(sqlstmt);
                            if (res >= 1) {
                                dos.writeUTF("Done");
                                //  System.out.println("// true");
                            } else {
                                //  System.out.println("// false");
                                dos.writeUTF("Error");
                            }
                        } else if (key.equals("getGender")) {
                            System.out.println("getgender method");
                            String user = obj.getString("username");
                            String Result = db.go.getGender(user);
                            System.out.println("nc,sncsdncksdnkl");
                            System.out.println(Result);
                            dos.writeUTF(Result);

                        } else if (key.equals("Login")) {
                            JSONArray data = (JSONArray) obj.get("data");
                            username = data.getString(0);
                            String password = data.getString(1);
                            // System.out.println(password);
                            boolean isLogged = db.go.checkUserAndPass(username, password);
                            if (isLogged) {
                                ServerGUIBase.onlineUser(username);
                                String on = "UPDATE users SET status=1 WHERE user_name='" + username + "'";
                                System.out.println("statmentoioioioioioio on = " + on);
                                go.runQ(on);
                                dos.writeUTF("True");
                            } else {
                                dos.writeUTF("False");
                            }
                        } else if (key.equals("GetTeamMates")) {
                            String data = obj.getString("username");
                            System.out.println("username = " + data);
                            int user_id = go.GetUserId(data, "user_name");
                            System.out.println("username = " + user_id);
                            JSONObject Result_data = db.go.getTeamMates(user_id);
                            System.out.println(Result_data);
                            dos.writeUTF(Result_data.toString());

                        } else if (key.equals("InsertUser")) {
                            System.out.println("");
                            JSONArray data = (JSONArray) obj.get("data");
                            String statment = "insert into users (user_name , gender , mail , password, status) values ('" + data.getString(0) + "','" + data.getString(1) + "','" + data.getString(2) + "','" + data.getString(3) + "',1)";
                            System.out.println(statment);
                            int res = db.go.runQ(statment);
                            if (res >= 1) {
                                dos.writeUTF("True");
                            } else {
                                dos.writeUTF("False");
                            }
                        } else if (key.equals("InsertTasks")) {
                            JSONArray data = (JSONArray) obj.get("data");
                            String user_name = data.getString(5);
                            int userID = go.GetUserId(username, "user_name");
                            String statment = "insert into tasks (  title , status , start_date , end_date , background_color , username_id) values ('" + data.getString(0) + "','" + data.getString(1) + "','" + data.getString(2) + "','" + data.getString(3) + "','" + data.getString(4) + "'," + userID + ")";
                            System.out.println(statment);
                            int res = db.go.runQ(statment);

                            int task_id = Integer.parseInt(go.getMaxNumber("tasks", "task_id"));

                            JSONArray data_items = (JSONArray) obj.get("Items");
                            for (int i = 0; i < data_items.length(); i++) {

                                String Item_statment = "insert into items (item, task_id) values ('" + data_items.getString(i) + "'," + task_id + ")";
                                db.go.runQ(Item_statment);

                            }
                            JSONArray data_collaborative = (JSONArray) obj.get("Collaboratives");
                            System.out.println("task id " + task_id);
                            for (int i = 0; i < data_collaborative.length(); i++) {
                                int collaborative_id = go.GetUserId(data_collaborative.getString(i), "user_name");
                                String colll_statment = "insert into add_task_notification (task_id, usename_id_sender, username_id_reciver, status) values (" + task_id + "," + userID + "," + collaborative_id + ",1 )";
                                db.go.runQ(colll_statment);
                            }

                            if (res >= 1) {
                                dos.writeUTF("True");
                            } else {
                                dos.writeUTF("False");
                            }
                        } else if (key.equals("UpdateTasks")) {
                            int task_id = obj.getInt("task_id");
                            JSONArray data = (JSONArray) obj.get("data");
                            String user_name = data.getString(5);
                            int userID = go.GetUserId(username, "user_name");
                            String statment = "UPDATE tasks SET title='" + data.getString(0) + "', status='" + data.getString(1) + "', start_date='" + data.getString(2) + "', end_date='" + data.getString(3) + "', background_color='" + data.getString(4) + "' WHERE task_id=" + task_id;
                            System.out.println(statment);
                            int res = db.go.runQ(statment);

                            // int task_id = Integer.parseInt(go.getMaxNumber("tasks", "task_id"));
                            //  String delete_collaborative = "delete * from collaborators where task_id = "+task_id; 
                            String delete_items = "delete * from items where task_id = " + task_id;
                            db.go.runQ(delete_items);

                            JSONArray data_items = (JSONArray) obj.get("Items");
                            for (int i = 0; i < data_items.length(); i++) {

                                String Item_statment = "insert into items (item, task_id) values ('" + data_items.getString(i) + "'," + task_id + ")";
                                db.go.runQ(Item_statment);

                            }
                            JSONArray data_collaborative = (JSONArray) obj.get("Notification");
                            System.out.println("task id " + task_id);
                            for (int i = 0; i < data_collaborative.length(); i++) {
                                int collaborative_id = go.GetUserId(data_collaborative.getString(i), "user_name");
                                String colll_statment = "insert into add_task_notification (task_id, usename_id_sender, username_id_reciver, status) values (" + task_id + "," + userID + "," + collaborative_id + ",1 )";
                                db.go.runQ(colll_statment);
                            }
                            JSONArray delete_collaborative = (JSONArray) obj.get("RemoveCollaborative");
                            System.out.println("task id " + task_id);
                            System.out.println("delete colll");
                            System.out.println(delete_collaborative.length());
                            for (int i = 0; i < delete_collaborative.length(); i++) {
                                int collaborative_id = go.GetUserId(delete_collaborative.getString(i), "user_name");
                                String colll_statment = "delete from collaborators where taske_id=" + task_id + " and  username_id_reciver=" + collaborative_id;
                                System.out.println(colll_statment);
                                db.go.runQ(colll_statment);
                            }

                            dos.writeUTF("True");

                        } else if (key.equals("GettingTasks")) {

                            String username1 = obj.getString("username_id");
                            int user_id = go.GetUserId(username1, "user_name");

                            String statment = "select * from tasks where username_id = " + user_id;
                            ResultSet res = db.go.getResultSet(statment);
                            JSONArray idRES = new JSONArray();
                            JSONArray titleRES = new JSONArray();
                            JSONObject dataRes = new JSONObject();
                            while (res.next()) {
                                idRES.put(res.getInt("task_id"));
                                titleRES.put(res.getString("title"));
                            }
                            dataRes.put("id", idRES);
                            dataRes.put("title", titleRES);
                            dos.writeUTF(dataRes.toString());
                        } else if (key.equals("DeleteTeamRequest")) {
                            //System.out.println("hi GettingTasks");
                            String user_sender = obj.getString("sender");
                            int user_id = go.GetUserId(user_sender, "user_name");
                            String user_reciver = obj.getString("receiver");
                            int reciver_id = go.GetUserId(user_reciver, "user_name");
                            String statment = "delete from team_mate where  username_id_send=" + user_id + " and username_id_recive=" + reciver_id;
                            System.out.println(statment);
                            db.go.runQ(statment);
                            dos.writeUTF("Done");
                        } else if (key.equals("GetTasksCount")) {
                            System.out.println("GetTasksCount");
                            String user = obj.getString("username");
                            System.out.println(user);
                            int done = go.getStatueCount("done", user);
                            System.out.println("done : " + done);
                            int todo = go.getStatueCount("todo", user);
                            System.out.println("");
                            int inprogress = go.getStatueCount("inprogress", user);
                            JSONObject all = new JSONObject();
                            all.put("todo", todo);
                            all.put("done", done);
                            all.put("inprogress", inprogress);
                            System.out.println(all);
                            dos.writeUTF(all.toString());
                        } else if (key.equals("checkCollaboritivity")) {
                            String user1 = obj.getString("username1");
                            String user2 = obj.getString("username2");
                            int id1 = go.GetUserId(user1, "user_name");
                            int id2 = go.GetUserId(user2, "user_name");
                            String flag = go.checkCollaborativity(id1, id2, 1, "check");
                            System.out.println("flag = " + flag);
                            if (flag.equals("True")) {
                                //cannot remove there ara tasks
                                dos.writeUTF("True");
                            } else {
                                //delete
                                String statment = "delete from team_mate where  username_id_send=" + id1 + " and username_id_recive=" + id2;
                                db.go.runQ(statment);
                                String statment1 = "delete from team_mate where  username_id_send=" + id2 + " and username_id_recive=" + id1;
                                db.go.runQ(statment1);
                                dos.writeUTF("False");
                            }

                        } else if (key.equals("getRequestNotifications")) {
                            System.out.println("getRequestNotifications");
                            String user = obj.getString("username");
                            int user_id = go.GetUserId(user, "user_name");
                            System.out.println("Hello from server");
                            System.out.println(user_id);
                            ArrayList<TaskNotification> task_request = go.getRequestTasksNotifications(user_id);
                            ArrayList<TeamNotification> team_request = go.getRequestTeamNotifications(user_id);
                            System.out.println(task_request);
                            JSONObject return_result = new JSONObject();
                            return_result.put("task_request", task_request);
                            return_result.put("team_request", team_request);
                            System.out.println("----------------------");
                            System.out.println(return_result);
                            dos.writeUTF(return_result.toString());

                        } else if (key.equals("AcceptTask")) {
                            System.out.println("AcceptTask");
                            int task_id = obj.getInt("task_id");
                            int user_id = obj.getInt("username_id_collaborative");
                            String insert_stmt = "INSERT INTO collaborators (taske_id,username_id_collaborative) VALUES (" + task_id + "," + user_id + ")";

                            System.out.println(insert_stmt);
                            go.runQ(insert_stmt);
                            String delete_stmt = "delete from add_task_notification where task_id=" + task_id + " and username_id_reciver=" + user_id;
                            System.out.println(delete_stmt);
                            go.runQ(delete_stmt);
                            dos.writeUTF("Done");

                        } else if (key.equals("AcceptTeamMate")) {
                            System.out.println("AcceptTeamMate");
                            int recive_id = obj.getInt("username_id_recive");
                            int send_id = obj.getInt("username_id_send");

                            String update_stmt1 = "UPDATE team_mate SET status=2 WHERE username_id_send=" + send_id + " and username_id_recive=" + recive_id;
                            System.out.println(update_stmt1);
                            go.runQ(update_stmt1);
                            String update_stmt2 = "UPDATE team_mate SET status=2 WHERE username_id_send=" + recive_id + " and username_id_recive=" + send_id;
                            System.out.println(update_stmt2);
                            int x = go.runQ(update_stmt2);
                            System.out.println("resulit of updatiiiiiiiiing");
                            if (x < 1) {
                                String insert_stmt = "INSERT INTO team_mate (username_id_send, username_id_recive, status) VALUES (" + recive_id + "," + send_id + ",2)";
                                System.out.println(insert_stmt);
                                go.runQ(insert_stmt);
                            }
                            //  sendMessageToAll("pleas update your GUI");
                            dos.writeUTF("Done");

                        } else if (key.equals("RejectTeamMate")) {
                            System.out.println("RejectTeamMate");
                            int recive_id = obj.getInt("username_id_recive");
                            int send_id = obj.getInt("username_id_send");
                            String delete_stmt = "delete from team_mate where username_id_send=" + send_id + " and username_id_recive=" + recive_id;

                            System.out.println(delete_stmt);
                            go.runQ(delete_stmt);
                            dos.writeUTF("Done");

                        } else if (key.equals("RejectTask")) {
                            System.out.println("RejectTask");
                            int task_id = obj.getInt("task_id");
                            int user_id = obj.getInt("username_id_collaborative");
                            String delete_stmt = "delete from add_task_notification where task_id=" + task_id + " and username_id_reciver=" + user_id;

                            System.out.println(delete_stmt);
                            go.runQ(delete_stmt);
                            //   sendMessageToAll("mwsssage from server to all");
                            dos.writeUTF("Done");

                        } else if (key.equals("DeleteTeamMates")) {
                            String user1 = obj.getString("username1");
                            String user2 = obj.getString("username2");
                            int id1 = go.GetUserId(user1, "user_name");
                            int id2 = go.GetUserId(user2, "user_name");

                            go.checkCollaborativity(id1, id2, 1, "delete");
                            String delete_stmt = "delete from team_mate where username_id_send =" + id1 + " and username_id_recive=" + id2 + " and  status=2 ";
                            String delete_stmt2 = "delete from team_mate where username_id_send =" + id2 + " and username_id_recive=" + id1 + " and  status=2 ";
                            String delete_task_request = "delete from add_task_notification where username_id_send =" + id1 + " and username_id_reciver=" + id2;
                            String delete_task_request1 = "delete from add_task_notification where username_id_send =" + id2 + " and username_id_reciver=" + id1;

                            System.out.println(delete_stmt);
                            System.out.println(delete_stmt2);
                            go.runQ(delete_stmt);
                            go.runQ(delete_stmt2);
                            go.runQ(delete_task_request);
                            go.runQ(delete_task_request1);
                            //   sendMessageToAll("mwsssage from server to all");
                            dos.writeUTF("Done");

                        } else if (key.equals("GetTasksAssignToMeCount")) {
                            System.out.println("GetTasksCount");
                            String user = obj.getString("username");
                            JSONObject tasks_count = go.getCountofTasksAssignToMe(username);
                            dos.writeUTF(tasks_count.toString());
                        } else if (key.equals("getAllTasks")) {
                            String user = obj.getString("username");
                            ArrayList<Task_Details> list = go.GetMyTasksDetails(go.GetUserId(user, "user_name"));
                            ArrayList<Task_Details> AssignTasks = go.GetTasksAssignToMeDetails(go.GetUserId(user, "user_name"));
                            JSONObject ret_list = new JSONObject();
                            ret_list.put("data", list);
                            ret_list.put("TasksAssignToMe",AssignTasks);
                            dos.writeUTF(ret_list.toString());

                        } else if (key.equals("DeleteTask")) {
                            System.out.println("DeleteTask");
                            int task_id = obj.getInt("task_id");
                            go.DeleteTask(task_id);
                            dos.writeUTF("Done");

                        }else if (key.equals("insertNewComment")) {
                           
                            int task_id = obj.getInt("task_id");
                            String creator = obj.getString("creator");
                            String comment = obj.getString("comment");
                            String comm = "INSERT INTO comments (task_id, comment, Creator) VALUES ("+task_id+",'"+comment+"','"+creator+"')";
                            go.runQ(comm);
                            dos.writeUTF("Done");

                        }
                    }
                    // creating Date object
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            // closing resources
            this.dis.close();
            this.dos.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void sendMessageToAll(String msg) {
        for (ClientHandler ch : clientsVector) {
            ch.checkAccess();
            if (ch.isAlive()) {
                try {
                    System.out.println("hi from send to all");
                    ch.dos.writeUTF(msg);
                } catch (Exception ex) {
                    System.out.println("cannot send data");
                }
            }

            //System.out.println(msg);
        }
    }
}
