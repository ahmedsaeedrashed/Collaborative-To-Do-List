/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import server_todo_prpject.Task_Details;
import server_todo_prpject.TaskNotification;
import server_todo_prpject.TeamNotification;

/**
 *
 * @author nada ezzat
 */
public class go {

    private static String url = "";
    private static Connection con;

    private static void setUrl() {

        try {
            // Class.forName("com.mysql.jdbc.Driver");
            // url = "jdbc:mysql://localhost:3306/store_program"+"?useUnicode=true&characterEncoding=UTF-8";
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }
        url = "jdbc:mysql://localhost:3306/todolist" + "?useUnicode=true&characterEncoding=UTF-8";

    }

    private static void setConnection() {

        try {
            setUrl();
            con = DriverManager.getConnection(url, "root", "1234");
        } catch (SQLException ex) {
            //Tools.msgBox(ex.getMessage());
            System.out.println("Error Connection To database");
        }

    }

    public static boolean checkUsernameIsExist(String username) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String strCheck = "SELECT * FROM users WHERE user_name='" + username + "'";
            System.out.println(strCheck);
            stmt.executeQuery(strCheck);
            while (stmt.getResultSet().next()) {
                con.close();
                System.out.println("here in while");
                return true;
            }
            con.close();

        } catch (SQLException ex) {

        }
        return false;
    }

    public static boolean checkEmailIsExist(String email) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String strCheck = "SELECT * FROM users WHERE mail='" + email + "'";
            System.out.println(strCheck);
            stmt.executeQuery(strCheck);
            while (stmt.getResultSet().next()) {
                con.close();
                System.out.println("here in while");
                return true;
            }
            con.close();

        } catch (SQLException ex) {

        }
        return false;
    }

    public static boolean checkUserAndPass(String username, String password) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String strCheck = "SELECT * FROM users WHERE user_name='" + username + "' and password='" + password + "';";

            stmt.executeQuery(strCheck);
            while (stmt.getResultSet().next()) {
                con.close();
                return true;
            }
            con.close();

        } catch (SQLException ex) {

        }
        return false;
    }

    public static String getGender(String username) {
        try {
            setConnection();
            String r = "";
            Statement stmt = con.createStatement();
            String strCheck = "SELECT gender FROM users WHERE user_name='" + username + "'";
            System.out.println(strCheck);
            ResultSet rs = stmt.executeQuery(strCheck);
            while (rs.next()) {

                r = rs.getString("gender");

            }
            con.close();
            return r;

        } catch (SQLException ex) {
            System.out.println("exceeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeption");
        }
        return null;
    }

    public static int getStatueCount(String stat, String usernsme) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            if (usernsme.equals("server")) {
                rs = stmt.executeQuery("select * from tasks");
            } else {
                int user_id = go.GetUserId(usernsme, "user_name");
                rs = stmt.executeQuery("select * from tasks where username_id = " + user_id);
            }
            int count = 0;
            while (rs.next()) {
                if (rs.getString(3).equals(stat)) {
                    count++;
                }
            }

            con.close();
            return count;
        } catch (SQLException ex) {
            return 0;
        }
    }

    public static int runQ(String sqlStatement) throws SQLException {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            //  System.out.println("***");
            int pri = stmt.executeUpdate(sqlStatement);
            System.out.println("RubqqqqqqqqqqqqqqqqqQ return");
            System.out.println(pri);

            con.close();
            return pri;
        } catch (SQLException ex) {
            con.close();
            return 0;
        }
    }

    public static ResultSet getResultSet(String sqlStatement) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement);

            //con.close();
            return rs;

        } catch (SQLException ex) {

            return null;
        }
    }

    public static ArrayList GetMyTasksDetails(int user_id) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("select * from tasks where username_id = " + user_id);
            int count = 0;
            ArrayList<Task_Details> TaskDetails = new ArrayList<Task_Details>();
            while (rs.next()) {

                int task_id = rs.getInt("task_id");
                String title = rs.getString("title");
                String status = rs.getString("status");
                String start_date = rs.getString("start_date");
                String end_date = rs.getString("end_date");
                String background_color = rs.getString("background_color");
                ArrayList items = GetMyTasksItems(task_id);
                JSONObject comments = GetMyTasksComments(task_id);
                ArrayList collaborators = GetMyTaskscollaborators(task_id);
                Task_Details td = new Task_Details(task_id, title, status, start_date, end_date, background_color, user_id, items, comments, collaborators);
                TaskDetails.add(td);

            }
            con.close();
            return TaskDetails;
        } catch (SQLException ex) {
            return null;
        }
    }

    public static ArrayList GetMyTasksItems(int Task_id) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("select item from items where task_id = " + Task_id);
            ArrayList<String> TaskItems = new ArrayList<String>();
            while (rs.next()) {
                String item = rs.getString("item");
                TaskItems.add(item);
            }
            con.close();
            return TaskItems;
        } catch (SQLException ex) {
            return null;
        }
    }

    public static JSONObject GetMyTasksComments(int Task_id) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("select * from comments where task_id = " + Task_id);
            JSONObject All_comments = new JSONObject();
            JSONArray creator = new JSONArray();
            JSONArray comment = new JSONArray();
            while (rs.next()) {
                String user = rs.getString("Creator");
                String task_comment = rs.getString("comment");
                creator.put(user);
                comment.put(task_comment);
            }
            All_comments.put("Creator", creator);
            All_comments.put("comment", comment);
            con.close();
            return All_comments;
        } catch (SQLException ex) {
            return null;
        }
    }

    public static ArrayList GetMyTaskscollaborators(int Task_id) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("select username_id_collaborative from collaborators where taske_id = " + Task_id);
            ArrayList<String> TaskColl = new ArrayList<String>();
            while (rs.next()) {
                int id = rs.getInt("username_id_collaborative");
                String name = GetUserName(id);
                TaskColl.add(name);
            }
            con.close();
            return TaskColl;
        } catch (SQLException ex) {
            return null;
        }
    }

    public static JSONObject getTeamMates(int id) {
        try {
            System.out.println("getTeamMates");
            System.out.println(id);
            String sqlStatement = "SELECT * FROM todolist.team_mate where username_id_send=" + id + " order by status";
            System.out.println(sqlStatement);
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlStatement);

            JSONObject dataRes = new JSONObject();

            JSONArray team = new JSONArray();
            JSONArray statusWaiting = new JSONArray();
            while (rs.next()) {
                String name = GetUserName(rs.getInt("username_id_recive"));
                team.put(name);
                statusWaiting.put(rs.getInt("status"));

            }
            System.out.println(team);
            //con.close();
            dataRes.put("username", team);
            dataRes.put("statusOnline_Offline", getStatus(team));
            dataRes.put("statusWaiting_Accept", statusWaiting);

            System.out.println("jsonobject  :" + dataRes);
            return dataRes;

        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static JSONArray getStatus(JSONArray team_name) {
        try {
            JSONArray status = new JSONArray();
            setConnection();
            Statement stmt2 = con.createStatement();

            for (int i = 0; i < team_name.length(); i++) {
                String getStatus = "SELECT status FROM users where user_name='" + team_name.getString(i) + "'";
                System.out.println(getStatus);
                ResultSet rs2 = stmt2.executeQuery(getStatus);
                if (rs2.next()) {
                    int state = rs2.getInt("status");
                    System.out.println(state);
                    status.put(state);

                }
            }
            System.out.println("status  :" + status);
            return status;
        } catch (SQLException ex) {
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static int GetUserId(String username, String colum) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String sqlStatment = " select user_id from users where " + colum + "= '" + username + "'";
            System.out.println(sqlStatment);
            ResultSet res = stmt.executeQuery(sqlStatment);
            System.out.println("RubQ return");
            // System.out.println(res);
            int userId = 0;
            while (res.next()) {
                userId = res.getInt("user_id");
                System.out.println("user ID  : " + userId);
            }
            con.close();
            return userId;
        } catch (SQLException ex) {
            //   Tools.msgBox(ex.getMessage());
            return 0;
        }
    }

    public static String GetUserName(int id) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String sqlStatment = " select user_name from users where user_id = " + id;
            System.out.println(sqlStatment);
            ResultSet res = stmt.executeQuery(sqlStatment);
            System.out.println("RubQ return");
            // System.out.println(res);
            String name = "";
            while (res.next()) {
                name = res.getString("user_name");

            }
            con.close();
            return name;
        } catch (SQLException ex) {
            //   Tools.msgBox(ex.getMessage());
            return null;
        }
    }

    public static String getMaxNumber(String tableName, String columnName) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String strAuto = "select max(" + columnName + ") AS autonum from " + tableName;
            stmt.executeQuery(strAuto);
            String num = "";
            while (stmt.getResultSet().next()) {
                num = stmt.getResultSet().getString("autonum");
            }

            con.close();
            if (num == null || "".equals(num)) {
                return "1";
            } else {
                return num;
            }
        } catch (SQLException ex) {

            return "0";
        }

    }

    public static String checkCollaborativity(int user1_id, int user2_id, int stat, String check) throws SQLException {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String strCheck = "SELECT task_id FROM Tasks WHERE username_id = " + user1_id;
            String flag = "False";
            List<Integer> tasks1_id = new ArrayList<Integer>();
            stmt.executeQuery(strCheck);
            while (stmt.getResultSet().next()) {
                tasks1_id.add(stmt.getResultSet().getInt("task_id"));
                System.out.print(stmt.getResultSet().getInt("task_id") + "  ");
            }
            String strCheck2 = "SELECT taske_id FROM collaborators WHERE username_id_collaborative = " + user2_id;
            System.out.println(strCheck2);
            System.out.println("--");
            List<Integer> tasks2_id = new ArrayList<Integer>();
            Statement stmt2 = con.createStatement();
            stmt2.executeQuery(strCheck2);
            while (stmt2.getResultSet().next()) {
                tasks2_id.add(stmt2.getResultSet().getInt("taske_id"));
                System.out.print(stmt2.getResultSet().getInt("taske_id") + "  ");
            }
            System.out.println("--");
            for (int k = 0; k < tasks1_id.size(); k++) {
                for (int l = 0; l < tasks2_id.size(); l++) {
                    if (tasks2_id.get(l) == tasks1_id.get(k)) {
                        if (check.equals("check")) {
                            flag = "True";
                            break;
                        } else {
                            String statment = "delete from collaborators where  taske_id=" + tasks2_id.get(l) + " and username_id_collaborative=" + user2_id;
                            System.out.println("deleeeeeeeeeeeeeeeeeeee");
                            System.out.println(statment);
                            Statement stmt_del = con.createStatement();
                            stmt_del.executeUpdate(statment);

                        }
                    }

                }
            }
            if (flag.equals("True") && check.equals("check")) {
                con.close();
                return "True";
            } else {
                if (stat == 1 && check.equals("check")) {
                    flag = checkCollaborativity(user2_id, user1_id, 2, "check");
                    con.close();
                    return flag;
                } else if (stat == 1) {
                    flag = checkCollaborativity(user2_id, user1_id, 2, "delete");
                }
            }
        } catch (SQLException ex) {
            con.close();
        }
        con.close();
        return "False";
    }

    public static void DeleteTask(int task_id) {
        try {
            String delNotificaion = " delete from add_task_notification where task_id =" + task_id;
            String delCollaboratives = " delete from collaborators where taske_id =" + task_id;
            String delItems = " delete from items where task_id =" + task_id;
            String delComments = " delete from comments where task_id =" + task_id;
            String delTask = " delete from tasks where task_id =" + task_id;
            setConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(delNotificaion);
            stmt.executeUpdate(delCollaboratives);
            stmt.executeUpdate(delItems);
            stmt.executeUpdate(delComments);
            stmt.executeUpdate(delTask);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getTaskName(int task_id) throws SQLException {
        try {
            String task_title = "Select title from tasks where task_id =" + task_id;
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(task_title);
            String title = "";
            if (rs.next()) {
                title = rs.getString("title");
            }
            con.close();
            return title;
        } catch (SQLException ex) {
            con.close();
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

    public static ArrayList getRequestTasksNotifications(int user_id) throws SQLException {

        String Tasks_notification = "Select * from add_task_notification where username_id_reciver =" + user_id + " and status =1";
        System.out.println("getRequestTasksNotifications");
        System.out.println(Tasks_notification);
        setConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(Tasks_notification);
        String reciver = go.GetUserName(user_id);
        ArrayList<TaskNotification> request_list = new ArrayList<TaskNotification>();
        while (rs.next()) {
            int task_id = rs.getInt("task_id");
            String task_title = getTaskName(task_id);
            int usename_id_sender = rs.getInt("usename_id_sender");
            String sender = go.GetUserName(usename_id_sender);
            int status = rs.getInt("status");
            TaskNotification tn = new TaskNotification(task_id, task_title, usename_id_sender, user_id, status, sender, reciver);
            request_list.add(tn);
        }
        con.close();
        return request_list;

    }

    public static ArrayList getRequestTeamNotifications(int user_id) throws SQLException {

        String Tasks_notification = "Select * from team_mate where username_id_recive =" + user_id + " and status =1";
        String user_reciever = go.GetUserName(user_id);
        setConnection();
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery(Tasks_notification);
        ArrayList<TeamNotification> Team_list = new ArrayList<TeamNotification>();
        while (rs.next()) {

            int username_id_send = rs.getInt("username_id_send");
            String user_send = go.GetUserName(username_id_send);
            int username_id_recive = rs.getInt("username_id_recive");
            int status = rs.getInt("status");
            TeamNotification tm = new TeamNotification(username_id_send, username_id_recive, status, user_reciever, user_send);
            Team_list.add(tm);
        }
        con.close();
        return Team_list;

    }

    public static void DEleteCollaborativity(int user1_id, int user2_id, int stat) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            String strCheck = "SELECT task_id FROM Tasks WHERE username_id = " + user1_id;
            String flag = "False";
            List<Integer> tasks1_id = new ArrayList<Integer>();
            stmt.executeQuery(strCheck);
            while (stmt.getResultSet().next()) {
                tasks1_id.add(stmt.getResultSet().getInt("task_id"));
                System.out.print(stmt.getResultSet().getInt("task_id") + "  ");
            }
            String strCheck2 = "SELECT taske_id FROM collaborators WHERE username_id_collaborative = " + user2_id;
            System.out.println(strCheck2);
            System.out.println("--");
            List<Integer> tasks2_id = new ArrayList<Integer>();
            Statement stmt2 = con.createStatement();
            stmt2.executeQuery(strCheck2);
            while (stmt2.getResultSet().next()) {
                tasks2_id.add(stmt2.getResultSet().getInt("taske_id"));
                System.out.print(stmt2.getResultSet().getInt("taske_id") + "  ");
            }
            System.out.println("--");
            for (int k = 0; k < tasks1_id.size(); k++) {
                for (int l = 0; l < tasks2_id.size(); l++) {
                    if (tasks2_id.get(l) == tasks1_id.get(k)) {
                        String statment = "delete from collaborators where  taske_id=" + tasks2_id.get(l) + " and username_id_collaborative=" + user1_id;
                        System.out.println("deleeeeeeeeeeeeeeeeeeee");
                        System.out.println(statment);
                        db.go.runQ(statment);

                    }

                }
            }

            if (stat == 1) {
                DEleteCollaborativity(user2_id, user1_id, 2);
            }

        } catch (SQLException ex) {

        }
    }

    public static JSONObject getCountofTasksAssignToMe(String user_name) {
        int user_id = GetUserId(user_name, "user_name");
        String stmt = "select taske_id from collaborators where username_id_collaborative = " + user_id;
        try {

            setConnection();
            Statement stmtCount = con.createStatement();
            ResultSet rs = null;
            rs = stmtCount.executeQuery(stmt);
            int done = 0;
            int inprogrss = 0;
            int todo = 0;
            List<Integer> list = new ArrayList<Integer>();
            while (rs.next()) {
                int task_id = rs.getInt("taske_id");
                list.add(task_id);

            }
            JSONObject res = countOfTasksAssignToMe(list);

            con.close();
            return res;
        } catch (SQLException ex) {
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static JSONObject countOfTasksAssignToMe(List<Integer> list) {
        try {
            String tasks = "select * from tasks ";
            setConnection();
            Statement s = con.createStatement();
            ResultSet rs = null;
            rs = s.executeQuery(tasks);
            int done = 0;
            int inprogrss = 0;
            int todo = 0;
            while (rs.next()) {
                int task_id = rs.getInt("task_id");
                for (int i = 0; i < list.size(); i++) {
                    if (task_id == list.get(i)) {
                        if (rs.getString("status").equals("done")) {
                            done++;
                        } else if (rs.getString("status").equals("inprogress")) {
                            inprogrss++;
                        } else if (rs.getString("status").equals("todo")) {
                            todo++;
                        }
                    }
                }

            }
            JSONObject count = new JSONObject();
            JSONArray status = new JSONArray();
            count.put("done", done);
            count.put("inprogress", inprogrss);
            count.put("todo", todo);
            return count;

        } catch (SQLException ex) {
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("kdfsldsdkhlskdhksdhksdh");
        return null;
    }

    public static ArrayList<Task_Details> GetTasksAssignToMeDetails(int id) {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery("select * from collaborators where username_id_collaborative = " + id);
            int count = 0;
            List<Integer> list = new ArrayList<Integer>();
           // ArrayList<Task_Details> TaskDetails = new ArrayList<Task_Details>();
            while (rs.next()) {
                int task_id = rs.getInt("taske_id");
                list.add(task_id);
                
            }
            
            
            con.close();
            return DetailsOfTasksAssignToMe(list);
        } catch (SQLException ex) {
            return null;
        }

    }

    public static ArrayList<Task_Details> DetailsOfTasksAssignToMe(List<Integer> list) {
        try {
            String tasks = "select * from tasks ";
            setConnection();
            Statement s = con.createStatement();
            ResultSet rs = null;
            rs = s.executeQuery(tasks);
            
            ArrayList<Task_Details> TaskDetails = new ArrayList<Task_Details>();

            while (rs.next()) {
                int task_id = rs.getInt("task_id");
                for (int i = 0; i < list.size(); i++) {
                    if (task_id == list.get(i)) {
                        int user_id = rs.getInt("username_id");
                        String title = rs.getString("title");
                        String status = rs.getString("status");
                        String start_date = rs.getString("start_date");
                        String end_date = rs.getString("end_date");
                        String background_color = rs.getString("background_color");
                        ArrayList items = GetMyTasksItems(task_id);
                        JSONObject comments = GetMyTasksComments(task_id);
                        ArrayList collaborators = GetMyTaskscollaborators(task_id);
                        Task_Details td = new Task_Details(task_id, title, status, start_date, end_date, background_color, user_id, items, comments, collaborators);
                        TaskDetails.add(td);
                    }
                }

            }
            
            return TaskDetails;

        } catch (SQLException ex) {
            Logger.getLogger(go.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("kdfsldsdkhlskdhksdhksdh");
        return null;
    }
}
