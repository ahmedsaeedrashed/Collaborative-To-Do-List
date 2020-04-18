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
public class TeamNotification {

    private int username_id_send;
    private int username_id_recive;
     private String username_send;
    private String username_recive;
    private int status;

    public TeamNotification(int username_id_send, int username_id_recive, int status, String username_recive ,String username_send) {
        this.username_id_send = username_id_send;
        this.username_id_recive = username_id_recive;
        this.status = status;
        this.username_recive= username_recive;
        this.username_send = username_send;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUsername_id_send() {
        return username_id_send;
    }

    public void setUsername_id_send(int username_id_send) {
        this.username_id_send = username_id_send;
    }

    public int getUsername_id_recive() {
        return username_id_recive;
    }

    public void setUsername_id_recive(int username_id_recive) {
        this.username_id_recive = username_id_recive;
    }

    public String getUsername_send() {
        return username_send;
    }

    public void setUsername_send(String username_send) {
        this.username_send = username_send;
    }

    public String getUsername_recive() {
        return username_recive;
    }

    public void setUsername_recive(String username_recive) {
        this.username_recive = username_recive;
    }
    
}
