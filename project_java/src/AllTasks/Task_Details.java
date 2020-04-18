/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AllTasks;

import java.util.ArrayList;
import java.util.Dictionary;
import javafx.scene.control.ListView;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author ALMOSTAFA
 */
public class Task_Details {

    private int task_id;
    private String title;
    private String status;
    private String start_date;
    private String end_date;
    private String background_color;
    private int username_id;
    private JSONArray TaskItems;
    private JSONArray TaskCollaborators;
    private JSONObject TaskComments ;

    public Task_Details(int task_id, String title, String status, String start_date, String end_date, String background_color, int username_id,JSONArray TaskItems, JSONObject TaskComments, JSONArray TaskCollaborators) {
        this.task_id = task_id;
        this.title = title;
        this.status = status;
        this.start_date = start_date;
        this.end_date = end_date;
        this.background_color = background_color;
        this.username_id = username_id;
        this.TaskItems = TaskItems;
        this.TaskCollaborators = TaskCollaborators;
        this.TaskComments = TaskComments;
    }

    public int getUsername_id() {
        return username_id;
    }

    public void setUsername_id(int username_id) {
        this.username_id = username_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    
    public JSONObject getTaskComments() {
        return TaskComments;
    }

    public void setTaskComments(JSONObject TaskComments) {
        this.TaskComments = TaskComments;
    }

    public JSONArray getTaskItems() {
        return TaskItems;
    }

    public void setTaskItems(JSONArray TaskItems) {
        this.TaskItems = TaskItems;
    }

    public JSONArray getTaskCollaborators() {
        return TaskCollaborators;
    }

    public void setTaskCollaborators(JSONArray TaskCollaborators) {
        this.TaskCollaborators = TaskCollaborators;
    }

    
}
