/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_app;

/**
 *
 * @author Casper
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends Thread {

    // data sending part
    // first we wnat to send the sign dataInputStream data to check dataInputStream the database
//    static String siginInData = "";
    String serverResponse = "";
    ///

    Socket socket;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    String serverIp;
    int serverPort;
    boolean isListening = false;
    
    // will be used for sign in operation latter.
    public String clientName;
    public String clientLastName;
    public String clientEmail;
    
    static SignInFrame signInFrm;

    public Client(String serverIp, int port) {
        this.serverIp = serverIp;
        this.serverPort = port;
    }

    public boolean ConnectToServer() {
        try {

            socket = new Socket(this.serverIp, this.serverPort);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connection accepted with server -> " + socket.getInetAddress() + ":" + socket.getPort());

        } catch (Exception err) {
            System.out.println("Error connecting to server: " + err);
        }
        return true;
    }
    
    public void clientOffline(String data) {
        try {
            dataOutputStream.writeUTF(data);
            System.out.println("data send to server is :" + data);
            serverResponse = dataInputStream.readUTF();
            System.out.println("result of checking in db : " + serverResponse);
            dataOutputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void DataToProjectMembers(String data) {
        try {
            dataOutputStream.writeUTF(data);
            System.out.println("data send to server is :" + data);
            serverResponse = "";
            dataOutputStream.flush();
            serverResponse = dataInputStream.readUTF();
            System.out.println("Response form server (Sign In) : " + serverResponse);
            dataOutputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DataToSignİn(String data) {
        try {
            dataOutputStream.writeUTF(data);
            System.out.println("data send to server is :" + data);
            serverResponse = "";
            dataOutputStream.flush();
            serverResponse = dataInputStream.readUTF();
            System.out.println("Response form server (Sign In) : " + serverResponse);
            dataOutputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void DataToSignUp(String data) {
        try {
            dataOutputStream.writeUTF(data);
            System.out.println("data send to server is :" + data);
            serverResponse = "";
            dataOutputStream.flush();
            serverResponse = dataInputStream.readUTF();
            System.out.println("Response from server (sign up) : " + serverResponse);
            dataOutputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void createProject(String data) {
        try {
            dataOutputStream.writeUTF(data);
            System.out.println("data send to server is :" + data);
            dataOutputStream.flush();
            serverResponse = "";
            serverResponse = dataInputStream.readUTF();
            System.out.println("Response form server (Create new project) : " + serverResponse);
            dataOutputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void joinProject(String data) {
        try {
            dataOutputStream.writeUTF(data);
            System.out.println("data send to server is :" + data);
            dataOutputStream.flush();
            serverResponse = "";
            serverResponse = dataInputStream.readUTF();
            System.out.println("Response form server (Join project) : " + serverResponse);
            dataOutputStream.flush();

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
    public void Listen() {
        this.isListening = true;
        this.start();
    }


    public void SendMessage(String data) {
        try {
            dataOutputStream.writeUTF(data);
        } catch (IOException err) {
            System.out.println("Exception writing to server: " + err);
        }
    }

    
}