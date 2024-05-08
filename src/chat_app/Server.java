/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_app;

/**
 *
 * @author Casper
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    // her clientın bir soketi olamlı
    private ServerSocket serverSocket;
    public boolean isListening = false;
    // port numarası
    private int port;
    ArrayList<Client> clientList;

    //yapıcı metod
    public Server() {

    }

    // client başlatma
    public boolean Create(int port) {

        try {
            this.port = port;
            // Client Soket nesnesi
            serverSocket = new ServerSocket(this.port);
            clientList = new ArrayList<>();

        } catch (Exception err) {
            System.out.println("Error connecting to server: " + err);
        }
        return true;
    }

    public void Listen() {
        this.isListening = true;
        this.start();

    }

    @Override
    public void run() {
        try {
            while (this.isListening) {
                System.out.println("Server waiting client...");
                Socket clientSocket = this.serverSocket.accept();
                Client nsclient = new Client(clientSocket, this);
                nsclient.Listen();
                clientList.add(nsclient);

                String cinfo = clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort();

            }

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void DicconnectClient(Client client) {
        this.clientList.remove(client);
        for (Client sClient : clientList) {
            String cinfo = sClient.socket.getInetAddress().toString() + ":" + sClient.socket.getPort();

        }

    }

    public void handleClient(Socket clientSocket) {
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {

            byte[] credentialsBytes = new byte[1024]; // Buffer to store input bytes
            int bytesRead = input.read(credentialsBytes);
            String credentials = new String(credentialsBytes, 0, bytesRead, StandardCharsets.UTF_8);

            String[] parts = credentials.split("-");
            String userName = parts[0];
            String password = parts[1];

            if (authenticate(userName, password)) {
                output.writeUTF("confirmed");
            } else {
                output.writeUTF("denied");
            }
            output.flush();
        } catch (IOException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
    }


    /* 
    confirming the user from the database 
     */
    private boolean authenticate(String userName, String password) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            String url = "jdbc:mysql://localhost:3306/ChatApp_db";
            conn = DriverManager.getConnection(url, "root", "Rahma123321123");

            String query = "SELECT * FROM clients WHERE userName = ? AND password = ?";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userName);
            pstmt.setString(2, password);

            rs = pstmt.executeQuery();

            return rs.next();
        } catch (SQLException ex) {

            System.err.println("SQL Exception: " + ex.getMessage());
            return false;
        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("SQL Exception on close: " + ex.getMessage());
            }
        }
    }

    public void Stop() {
        try {
            this.isListening = false;
            this.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //mesaj gönderme fonksiyonu
    public void SendMessage(byte[] msg, Client client) {
        msg[msg.length - 1] = 0x14;
        client.SendMessage(msg);
    }

}
