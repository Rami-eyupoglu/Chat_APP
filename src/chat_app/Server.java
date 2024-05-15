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
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    boolean isListening = false;
    public static ArrayList<Client> onlineClients;
    int port;
    ServerSocket serverSocket;
    static InetAddress ipAddress; 
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    public boolean Create(int port) {
        try {
            this.port = port;
            serverSocket = new ServerSocket(this.port);
            ipAddress = serverSocket.getInetAddress();
            System.out.println("Server started...");
            onlineClients = new ArrayList<>();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public void Listen() {

        this.isListening = true;
        this.start();

    }

    @Override
    public void run() {
        while (isListening) {
            try {
                System.out.println("server waiting for clients...");
                Socket clientSocket = serverSocket.accept();// blocking
                //get the connect clients info in the server.
                String cinfo = clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort();
                System.out.println("client connected to server ---> " + cinfo);
                ServerFrame.clientsListModel.addElement(cinfo);

                // handling the opertions (sign in, sign up ....etc) 
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clientHandler.start();

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // this function will stop the server
    public void Stop() {
        try {
            this.isListening = false;
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.Create(8080);
        server.Listen();
    }

}
