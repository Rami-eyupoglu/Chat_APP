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

/**
 * This class is for the client. It connects to the server and sends data.
 */
public class Client extends Thread {

    String serverResponse = ""; // To store response from server
    Socket socket; // Client socket

    DataInputStream dataInputStream; // To read data from server
    DataOutputStream dataOutputStream; // To send data to server
    String serverIp; // Server IP address
    int serverPort; // Server port number
    boolean isListening = false; // To check if client is listening

    // Used for sign in operation later
    public String clientName;
    public String clientLastName;
    public String clientEmail;

    static SignInFrame signInFrm;

    // Constructor to set server IP and port
    public Client(String serverIp, int port) {
        this.serverIp = serverIp;
        this.serverPort = port;
    }

    // Method to connect to server
    public boolean ConnectToServer() {
        try {
            socket = new Socket(this.serverIp, this.serverPort); // Create socket connection
            dataInputStream = new DataInputStream(socket.getInputStream()); // Get input stream
            dataOutputStream = new DataOutputStream(socket.getOutputStream()); // Get output stream

            System.out.println("Connection accepted with server -> " + socket.getInetAddress() + ":" + socket.getPort());

        } catch (Exception err) {
            System.out.println("Error connecting to server: " + err); // Print error if connection fails
        }
        return true;
    }

    // Method to send data when client goes offline
    public void clientOffline(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            serverResponse = dataInputStream.readUTF(); // Read response from server
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }

    // Method to send data to project members
    public void DataToProjectMembers(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            System.out.println("data send to server is :" + request);
            serverResponse = "";
            dataOutputStream.flush(); // Flush output stream
            serverResponse = dataInputStream.readUTF(); // Read response from server
            System.out.println("Response form server (Sign In) : " + serverResponse);
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }

    // Method to send request to server for sign in
    public void DataToSignİn(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            System.out.println("data send to server is :" + request);
            serverResponse = "";
            dataOutputStream.flush(); // Flush output stream
            serverResponse = dataInputStream.readUTF(); // Read response from server
            System.out.println("Response form server (Sign In) : " + serverResponse);
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }

    // Method to send request to server for sign up
    public void DataToSignUp(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            System.out.println("data send to server is :" + request);
            serverResponse = "";
            dataOutputStream.flush(); // Flush output stream
            serverResponse = dataInputStream.readUTF(); // Read response from server
            System.out.println("Response from server (sign up) : " + serverResponse);
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }

    // Method to send request to server to create a project
    public void createProject(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            System.out.println("data send to server is :" + request);
            dataOutputStream.flush(); // Flush output stream
            serverResponse = "";
            serverResponse = dataInputStream.readUTF(); // Read response from server
            System.out.println("Response form server (Create new project) : " + serverResponse);
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }

    // Method to send request to server to join a project
    public void joinProject(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            System.out.println("data send to server is :" + request);
            dataOutputStream.flush(); // Flush output stream
            serverResponse = "";
            serverResponse = dataInputStream.readUTF(); // Read response from server
            System.out.println("Response form server (Join project) : " + serverResponse);
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }
    
    public void leaveProject(String request) {
        try {
            dataOutputStream.writeUTF(request); // Send data to server
            System.out.println("data send to server is :" + request);
            dataOutputStream.flush(); // Flush output stream
            serverResponse = "";
            serverResponse = dataInputStream.readUTF(); // Read response from server
            System.out.println("Response form server (Join project) : " + serverResponse);
            dataOutputStream.flush(); // Flush output stream

        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex); // Log error if exception occurs
        }
    }

    // Method to start listening for server messages
    public void Listen() {
        this.isListening = true; // Set listening flag to true
        this.start(); // Start the thread
    }

    // Method to send a message to the server
    public void SendMessage(String data) {
        try {
            dataOutputStream.writeUTF(data); // Send data to server
        } catch (IOException err) {
            System.out.println("Exception writing to server: " + err); // Print error if writing fails
        }
    }
}
