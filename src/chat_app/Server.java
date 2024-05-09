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
        while (isListening) {
            try {
                System.out.println("server waiting for clients...");
                Socket clientSocket = serverSocket.accept();//blocking
                System.out.println("client connected to server...");
                // we will handle all the clients when connect to server in the same time
                new Thread(() -> handleClient(clientSocket)).start();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void DicconnectClient(Client client) {
        this.clientList.remove(client);
        for (Client sClient : clientList) {
            String cinfo = sClient.socket.getInetAddress().toString() + ":" + sClient.socket.getPort();

        }

    }
    static String url = "jdbc:mysql://localhost:3306/ChatApp_db";
    static String db_name = "root";
    static String db_password = "Rahma123321123";

    private void handleClient(Socket clientSocket) {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            String clientMessage = in.readUTF();
            System.out.println("Message from client " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": " + clientMessage);

            String[] data = clientMessage.split(",");
            String operationCode = data[0];

            switch (operationCode) {
                case "1":
                    handleSignIn(data, out);
                    break;
                case "2":
                    handleSignUp(data, out);
                    break;
                case "3":
                    handleDisconnect(data, out);
                    break;
                default:
                    out.writeUTF("Invalid operation code");
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Error handling client connection: " + ex.getMessage(), ex);
        }
    }

    private void handleSignIn(String[] data, DataOutputStream out) throws IOException {
        String email = data[1];
        String password = data[2];

        try (Connection conn = DriverManager.getConnection(url, db_name, db_password);
                PreparedStatement stmt = conn.prepareStatement("SELECT * FROM clients WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    String response = "11," + rs.getString("name") + "," + rs.getString("surname") + "," + rs.getString("email");
                    out.writeUTF(response);
                } else {
                    out.writeUTF("10");
                }
            } else {
                out.writeUTF("0");
            }
        } catch (SQLException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Database error during sign in: " + e.getMessage(), e);
            out.writeUTF("Database error");
        }
    }

    private void handleSignUp(String[] data, DataOutputStream out) throws IOException {
        String name = data[1];
        String lastName = data[2];
        String email = data[3];
        String password = data[4];

        try (Connection conn = DriverManager.getConnection(url, db_name, db_password)) {
            if (emailExists(conn, email)) {
                out.writeUTF("Email already exists");
            } else {
                if (insertNewUser(conn, name, lastName, email, password)) {
                    out.writeUTF("Data inserted");
                } else {
                    out.writeUTF("Insertion failed");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, "Database error during sign up: " + e.getMessage(), e);
            out.writeUTF("Database error");
        }
    }

    private boolean emailExists(Connection conn, String email) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM clients WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getInt("count") > 0;
        }
    }

    private boolean insertNewUser(Connection conn, String name, String lastName, String email, String password) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO clients (name, surname, email, password) VALUES (?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, lastName);
            stmt.setString(3, email);
            stmt.setString(4, password);
            return stmt.executeUpdate() > 0;
        }
    }

    private void handleDisconnect(String[] data, DataOutputStream out) throws IOException {
        // Handle client disconnect logic
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
