/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectToServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.json.JSONObject;

/**
 *
 * @author ALMOSTAFA
 */
public class ServerConnection {

    public static boolean ServerCheck = false;

    public static DataInputStream dis;
    public static DataOutputStream dos;
    public static Socket s;

    //  Thread th = new Thread(this);
    Alert a = new Alert(AlertType.ERROR);

    public ServerConnection() {
        System.out.println("constructor **");
        // th.start();
    }

    public static void StartConnection() {
        try {
            System.out.println("launching");
            // getting localhost ip
            InetAddress ip = InetAddress.getByName("localhost");
            System.out.println("ip = " + ip);
            // establish the connection with server port 5056
            //10.0.2.76
            s = new Socket(ip, 5005);

            // obtaining input and out streams
            dis = new DataInputStream(s.getInputStream());
            dos = new DataOutputStream(s.getOutputStream());

            System.out.println("Connection success");
            ServerCheck = true;
            /* while (ServerCheck) {
                String received = dis.readUTF();
                System.out.println("messge from the server");
                System.out.println(received);
            }*/
        } catch (IOException ex) {
           ServerCheck = false;
        }

    }

    public static String ConnectToServer(JSONObject data) throws IOException {

        try {
            if (ServerCheck) {
                
                System.out.println("connec 1 ");
                String tosend = data.toString();
                System.out.println("connet 2");
                dos.writeUTF(tosend);
                System.out.println("connect 3");
                //Response
                String received = dis.readUTF();
                System.out.println("method connect to server :  " + received);
                return received;
                
            } else {
                s.close();
                dis.close();
                dos.close();
                return "ServerError";
            }
        } catch (Exception e) {
            s.close();
            dis.close();
            dos.close();
            System.out.println("ErrorConnection");
            return "ErrorWithTheServer";

        }

    }

    public static void closeConnection() {
        try {
            ServerCheck = false;
            dos.writeUTF("Exit");
            s.close();
            dis.close();
            dos.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
