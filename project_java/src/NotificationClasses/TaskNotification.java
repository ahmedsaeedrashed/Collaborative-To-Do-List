/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NotificationClasses;

/**
 *
 * @author ALMOSTAFA
 */
public class TaskNotification {

    private int task_id;
    private int usename_id_sender;
    private int username_id_reciver;
    private int status;

    public TaskNotification(int task_id, int usename_id_sender, int username_id_reciver, int status) {
        this.task_id = task_id;
        this.usename_id_sender = usename_id_sender;
        this.username_id_reciver = username_id_reciver;
        this.status = status;
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
    
}
