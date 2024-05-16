/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Casper
 */
class DataBaseOperaions extends Thread {

    Client client;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    DBConnections dbConnections = new DBConnections();
    
    DataBaseOperaions(Socket clientSocket) {
        try {
            // the client will
            client = new Client("localhost", 8080);
            this.client.socket = clientSocket;
            dataInputStream = new DataInputStream(client.socket.getInputStream());
            dataOutputStream = new DataOutputStream(client.socket.getOutputStream());
            Server.onlineClients.add(client);
        } catch (IOException ex) {
            Logger.getLogger(DataBaseOperaions.class.getName()).log(Level.SEVERE, null, ex);
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

    private synchronized void signIn(String email, String passwordData) {
        try {
            ResultSet resultSet = dbConnections.signInConnection(email); // Use DBConnections class
            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                if (storedPassword.equals(passwordData)) {
                    System.out.println("User in the database");
                    String nameToSend = resultSet.getString("name");
                    String lastNameToSend = resultSet.getString("surname");
                    String emailToSend = resultSet.getString("email");

                    String response = "11," + nameToSend + "," + lastNameToSend + "," + emailToSend + ",";
                    response += getUserProjects(emailToSend);
                    dataOutputStream.writeUTF(response);
                } else {
                    System.out.println("Email in the database but wrong password");
                    dataOutputStream.writeUTF("10");
                }
            } else {
                System.out.println("Email not registered");
                dataOutputStream.writeUTF("0");
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void signUp(String name, String lastName, String email, String passwordData) {
        try {
            int result = dbConnections.signUpConnection(name, lastName, email, passwordData); // Use DBConnections class
            if (result == 0) {
                System.out.println("Email already exists");
                dataOutputStream.writeUTF("000");
            } else if (result > 0) {
                System.out.println("Data inserted successfully");
                dataOutputStream.writeUTF("111");
            } else {
                System.out.println("Insertion failed");
                dataOutputStream.writeUTF("insertion failed");
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void createProject(String userName, String userLastName, String email, String projectName) {
        String randomKey = generateKey();
        try {
            String resultKey = dbConnections.createProjectConnection(email, projectName, randomKey); // Use DBConnections class
            if (resultKey != null) {
                System.out.println("Project inserted successfully");
                addUserToProject(userName, userLastName, email, projectName);
                String messageToServer = "31," + randomKey;
                dataOutputStream.writeUTF(messageToServer);
            } else {
                System.out.println("Name not allowed: Project name is already used");
                dataOutputStream.writeUTF("30");
            }
        } catch (Exception ex) {
            Logger.getLogger(DataBaseOperaions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void sendMessage(String pName, String message) {
        try {
            ResultSet rs = dbConnections.getProjectMembersConnection(pName);
            while (rs.next()) {
                String email = rs.getString("email");
                for (Client client : Server.onlineClients) {
                    if (email.equals(client.clientEmail)) {
                        dataOutputStream.writeUTF("51");
                        dataOutputStream.writeUTF(message);
                    } else {
                        dataOutputStream.writeUTF("50");
                    }
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized String getProjectPrivateKey(String email) {
        try {
            return dbConnections.getProjectKeyConnection(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
    }

    public synchronized void addUserToProject(String name, String lastName, String email, String pName) {
        try {
            int result = dbConnections.addUserToProjectConnection(name, lastName, email, pName);
            if (result > 0) {
                System.out.println("User added successfully to userProjects table.");
            } else {
                System.out.println("Failed to add user to userProjects table.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean checkProjectKey(String projectName, String projectKey) {
        try {
            return dbConnections.checkProjectKeyConnection(projectName, projectKey);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private synchronized String getUserProjects(String email) {
        try {
            return dbConnections.getUserProjects(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        }
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

    private String generateKey() {
        StringBuilder key = new StringBuilder();
        String chrs = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()-_=+[]{}|;:,.<>?";
        int keyLength = 7;
        Random random = new Random();
        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(chrs.length());
            key.append(chrs.charAt(index));
        }

        return key.toString();
    }
}
