/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Casper
 */
class ClientHandler extends Thread {

    Client client;

    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;

    // My dataBase info.
    public String url = "jdbc:mysql://localhost:3306/ChatApp_db";
    public String username = "root";
    public String password = "Rahma123321123";

    ClientHandler(Socket clientSocket) {
        try {
            // the client will
            client = new Client("localhost", 8080);
            this.client.socket = clientSocket;
            dataInputStream = new DataInputStream(client.socket.getInputStream());
            dataOutputStream = new DataOutputStream(client.socket.getOutputStream());
            Server.onlineClients.add(client);
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        handleClient(client.socket);
    }

    public void handleClient(Socket clientSocket) {
        while (true) {
            try {
                String clientMessage = dataInputStream.readUTF();
                /// this is the messsage from the client we will check like four things
                // 1 if the message starts with one it means clients want to sign dataInputStream

                System.out.println("Message form client" + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());
                System.out.println(clientMessage);
                // we will split the data came from the client

                String[] data = clientMessage.split(",");

                String operationCode = data[0];
                // sign dataInputStream section 1 means sigin dataInputStream
                switch (operationCode) {
                    case "1":
                        // Sign-in section
                        String email = data[1];
                        String passwordData = data[2];
                        signIn(email, passwordData);
                        break;

                    case "2":
                        // Sign-up section
                        String name = data[1];
                        String lastName = data[2];
                        String emailSignUp = data[3]; 
                        String passwordSignUp = data[4]; 
                        signUp(name, lastName, emailSignUp, passwordSignUp);
                        break;

                    case "3":
                        // Create project section
                        String projectEmail = data[1]; 
                        String projectName = data[2]; 
                        String userName = data[3];
                        String userLastName = data[4];
                        createProject(userName, userLastName, projectEmail, projectName);
                        break;

                    case "4":
                        // Join project section
                        String projectNameJoin = data[1]; 
                        String projectKey = data[2];
                        String clientNameJoin = data[3]; 
                        String clientLastNameJoin = data[4]; 
                        String clientEmailJoin = data[5]; 
                        joinProject(projectNameJoin, projectKey, clientNameJoin, clientLastNameJoin, clientEmailJoin);
                        break;

                    case "5":
                        // Send message section
                        String projectNameMessage = data[1]; // Renamed for clarity
                        String message = data[2];
                        sendMessage(projectNameMessage, message);
                        break;

                    default:
                        // Handle unknown operation codes
                        System.out.println("Unknown operation code: " + operationCode);
                        break;
                }

            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private synchronized void sendMessage(String pName, String message) {
        // i will send the massege to all clients they are dataInputStream the same project and the
        // are conneccted online
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String query = "SELECT email FROM userProjects WHERE project_name = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            // Set the project name parameter dataInputStream the prepared statement
            stmt.setString(1, pName);
            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Iterate over the result set and print user details
            while (rs.next()) {
                String email = rs.getString("email");
                for (Client client : Server.onlineClients) {
                    if (email.equals(client.clientEmail)) {
                        try {
                            dataOutputStream.writeUTF("51");
                            dataOutputStream.writeUTF(message);
                        } catch (IOException ex) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        try {
                            dataOutputStream.writeUTF("50");
                        } catch (IOException ex) {
                            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // here tommorw
    // when we create new project we should add the user to the userProjects table
    // at the same time
    private synchronized void createProject(String userName, String userLastName, String email, String projectName) {
        String randomKey = generateKey();
        String messageToserver = "";
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String insertQuery = "INSERT INTO Projects (admin_email, project_name,project_key) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(insertQuery);
            pstmt.setString(1, email);
            pstmt.setString(2, projectName);
            pstmt.setString(3, randomKey);

            pstmt.executeUpdate();
            System.out.println("Project inserted successfully");
            try {
                // increaseProjectusers(projectName);
                // here we will generate a random key for entering the project
                addUserToProject(userName, userLastName, email, projectName);
                messageToserver = "";
                messageToserver += "31,";
                messageToserver += randomKey;
                dataOutputStream.writeUTF(messageToserver);
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            try {
                System.out.println("Name not allowed: Project name is already used");
                dataOutputStream.writeUTF("30,");
                // System.dataOutputStream.println(e.getMessage());
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    private synchronized void signUp(String name, String lastName, String email, String passwordData) {
        // Sign up logic
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String checkIfExistsSql = "SELECT COUNT(*) AS count FROM clients WHERE email = ?";
            PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsSql);
            checkIfExistsStatement.setString(1, email);
            ResultSet resultSet = checkIfExistsStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt("count");
            if (count > 0) {
                // Email already exists
                try {
                    System.out.println("Email already exists");
                    dataOutputStream.writeUTF("000");
                } catch (IOException ex) {
                    Logger.getLogger(Server.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                // Email doesn't exist, proceed with insertion
                String insertSql = "INSERT INTO clients (name, surname, email, password) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                insertStatement.setString(1, name);
                insertStatement.setString(2, lastName);
                insertStatement.setString(3, email);
                insertStatement.setString(4, passwordData);
                int rowsAffected = insertStatement.executeUpdate();
                if (rowsAffected > 0) {
                    try {
                        System.out.println("Data inserted successfully");
                        dataOutputStream.writeUTF("111");
                        // DicconnectClient(client);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    try {
                        System.out.println("Insertion failed");
                        dataOutputStream.writeUTF("insertion failed");
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private synchronized void signIn(String email, String passwordData) {
        // Sign dataInputStream logic
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM clients WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // User found, check password
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(passwordData)) {
                    // Passwords match
                    try {
                        System.out.println("User in the database");
                        // we will send the user data when he sign dataInputStream succefully
                        // Project.connectedToPRojectClients.add(client);
                        //////////////////////////
                        String nameTosend = resultSet.getString("name");
                        String lastNameToSend = resultSet.getString("surname");
                        String emailToSend = resultSet.getString("email");

                        String respone = "11";
                        respone += "," + nameTosend + "," + lastNameToSend + "," + emailToSend + ",";
                        respone += getUserProjects(emailToSend);
                        dataOutputStream.writeUTF(respone);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    // Passwords don't match
                    try {
                        System.out.println("email in the database but wrong password");
                        String respone = "10";
                        dataOutputStream.writeUTF(respone);
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                // User not found
                try {
                    System.out.println("Email not registered");
                    dataOutputStream.writeUTF("0");

                } catch (IOException ex) {
                    Logger.getLogger(Server.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String getProjectPrivateKey(String email) {
        String key = "";
        try {
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT project_key FROM Projects WHERE manager_email = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                key = "-->";
                key += resultSet.getString("project_key");
            }
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
        }
        return key;
    }

    private String generateKey() {
        StringBuilder key = new StringBuilder();
        String chrs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int KEY_LENGTH = 7;
        Random random = new Random();
        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = random.nextInt(chrs.length());
            key.append(chrs.charAt(index));
        }

        return key.toString();
    }

    private synchronized void joinProject(String pName, String projectKey, String name, String lastName, String email)
            throws IOException {
        if (checkProjectKey(pName, projectKey)) {
            addUserToProject(name, lastName, email, pName);
            System.out.println(name + "Join the project " + pName);
            dataOutputStream.writeUTF("41");
        } else {
            dataOutputStream.writeUTF("40");
        }
    }

    public synchronized void addUserToProject(String name, String lastName, String email, String pName) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String insertQuery = "INSERT INTO userProjects (name, last_name, email, project_name) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, name);
                pstmt.setString(2, lastName);
                pstmt.setString(3, email);
                pstmt.setString(4, pName);

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("User added successfully to userProjects table.");
                } else {
                    System.out.println("Failed to add user to userProjects table.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to add user to userProjects table.");
        }
    }

    public synchronized boolean checkProjectKey(String projectName, String projectKey) {
        boolean isValid = false;
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT project_key FROM Projects WHERE project_name = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, projectName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    String storedProjectKey = rs.getString("project_key");
                    isValid = storedProjectKey.equals(projectKey);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to check project key");
        }
        return isValid;
    }

    private synchronized String getUserProjects(String email) {
        StringBuilder projectNameBuilder = new StringBuilder();
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String selectQuery = "SELECT project_name FROM userProjects WHERE email = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(selectQuery)) {
                pstmt.setString(1, email);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    projectNameBuilder.append(rs.getString("project_name")).append(',');
                    System.out.println(rs.getString("project_name"));
                }
                System.out.println("data sent from getUserProjects : " + projectNameBuilder.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to retrieve user projects");
        }
        return projectNameBuilder.toString();
    }

    private synchronized void getProjectMembers(String pName) {

    }

}
