/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_todo_prpject;

/**
 *
 * @author ALMOSTAFA
 */
public class TaskNotification {

    private int task_id;
    private String task_title;
    private int usename_id_sender;
    private int username_id_reciver;
    private int status;
    private String usename_sender;
    private String username_reciver;

    public TaskNotification(int task_id,String task_title, int usename_id_sender, int username_id_reciver, int status , String usename_sender, String username_reciver) {
        this.task_id = task_id;
        this.task_title = task_title;
        this.usename_id_sender = usename_id_sender;
        this.username_id_reciver = username_id_reciver;
        this.status = status;
        this.usename_sender = usename_sender;
        this.username_reciver = username_reciver;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getUsename_id_sender() {
        return usename_id_sender;
    }

    public void setUsename_id_sender(int usename_id_sender) {
        this.usename_id_sender = usename_id_sender;
    }

    public int getUsername_id_reciver() {
        return username_id_reciver;
    }

    public void setUsername_id_reciver(int username_id_reciver) {
        this.username_id_reciver = username_id_reciver;
    }

    public String getUsename_sender() {
        return usename_sender;
    }

    public void setUsename_sender(String usename_sender) {
        this.usename_sender = usename_sender;
    }

    public String getUsername_reciver() {
        return username_reciver;
    }

    public void setUsername_reciver(String username_reciver) {
        this.username_reciver = username_reciver;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }
    
}
