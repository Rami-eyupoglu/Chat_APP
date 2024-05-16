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
                System.out.println("Message from client " + clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort());
                System.out.println(clientMessage);
                String[] data = clientMessage.split(",");
                String operationCode = data[0];
                switch (operationCode) {
                    case "sign_in":
                        String email = data[1];
                        String passwordData = data[2];
                        signIn(email, passwordData);
                        break;

                    case "sign_up":
                        String name = data[1];
                        String lastName = data[2];
                        String emailSignUp = data[3];
                        String passwordSignUp = data[4];
                        signUp(name, lastName, emailSignUp, passwordSignUp);
                        break;

                    case "CreateProject":
                        String projectEmail = data[1];
                        String projectName = data[2];
                        createProject(projectEmail, projectName);
                        break;

                    case "joinProject":
                        String projectNameJoin = data[1];
                        String projectKey = data[2];
                        String clientEmailJoin = data[3];
                        joinProject(projectNameJoin, projectKey, clientEmailJoin);
                        break;
                    case "leaveProject":
                        email = data[1];
                        projectName = data[2];
                        leaveProject(email, projectName);
                        break;
                        
                    case "sendMessage":
                        String projectNameMessage = data[1];
                        String message = data[2];
                        sendMessage(projectNameMessage, message);
                        break;

                    default:
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

                    String response = "signIn_confirm," + nameToSend + "," + lastNameToSend + "," + emailToSend + ",";
                    response += getUserProjects(emailToSend);
                    dataOutputStream.writeUTF(response);
                } else {
                    System.out.println("Email in the database but wrong password");
                    dataOutputStream.writeUTF("signIn_wrongPass");
                }
            } else {
                System.out.println("Email not registered");
                dataOutputStream.writeUTF("signIn_emailNotFound");
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private synchronized void leaveProject(String email, String projectName) {
        try {
            int result = dbConnections.removeUserFromProject(email, projectName);
            if (result > 0) {
                System.out.println("User removed successfully from project.");
                dataOutputStream.writeUTF("leaveProject_done");
            } else {
                System.out.println("Failed to remove user from project.");
                dataOutputStream.writeUTF("leaveProject_failed");
            }
        } catch (Exception ex) {
            Logger.getLogger(DataBaseOperaions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void signUp(String name, String lastName, String email, String passwordData) {
        try {
            int result = dbConnections.signUpConnection(name, lastName, email, passwordData); // Use DBConnections class
            if (result == 0) {
                System.out.println("Email already exists");
                dataOutputStream.writeUTF("signUp_emailExist");
            } else if (result > 0) {
                System.out.println("Data inserted successfully");
                dataOutputStream.writeUTF("signUp_done");
            } else {
                dataOutputStream.writeUTF("insertion failed");
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void createProject(String adminEmail, String projectName) {
        String randomKey = generateKey();
        try {
            String resultKey = dbConnections.createProjectConnection(adminEmail, projectName, randomKey); // Use DBConnections class
            if (resultKey != null) {
                System.out.println("Project inserted successfully");
                addUserToProject(adminEmail, projectName); // Add the admin to the project as a member
                String messageToServer = "CreateProject_done," + randomKey;
                dataOutputStream.writeUTF(messageToServer); // Inform client about the project creation and key
            } else {
                System.out.println("Name not allowed: Project name is already used");
                dataOutputStream.writeUTF("CreateProject_nameDuplicate"); // Inform client that the project name is already used
            }
        } catch (Exception ex) {
            Logger.getLogger(DataBaseOperaions.class.getName()).log(Level.SEVERE, null, ex); // Log any exceptions
        }
    }

    private synchronized void sendMessage(String pName, String message) {
        try {
            ResultSet rs = dbConnections.getProjectMembersConnection(pName);
            while (rs.next()) {
                String email = rs.getString("email");
                for (Client client : Server.onlineClients) {
                    if (email.equals(client.clientEmail)) {
                        dataOutputStream.writeUTF("sendMessage_done");
                        dataOutputStream.writeUTF(message);
                    } else {
                        dataOutputStream.writeUTF("sendMessage_failed");
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

    public synchronized void addUserToProject(String email, String projectName) {
        try {
            int result = dbConnections.addUserToProjectConnection(email, projectName);
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

    private synchronized void joinProject(String projectName, String projectKey, String email) throws IOException {
        if (checkProjectKey(projectName, projectKey)) {
            addUserToProject(email, projectName);  // Call the updated method
            System.out.println(email + " joined the project " + projectName);
            dataOutputStream.writeUTF("joinProject_done");  // Inform client that joining was successful
        } else {
            dataOutputStream.writeUTF("joinProject_failed");  // Inform client that joining failed due to incorrect key
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
