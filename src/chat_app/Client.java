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
    // first we wnat to send the sign sInput data to check sInput the database
//    static String siginInData = "";
    public static String checkDBServerResult = "";
    ///

    Socket socket;

    DataInputStream sInput;
    DataOutputStream sOutput;
    // server adresi ip address
    String serverIp;
    // port numarası
    int port;
    boolean isListening = false;

    public String clientName;
    public String clientLastName;
    public String cleintEmail;

    public Client(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public boolean ConnectToServer() {
        try {
            // Client Soket nesnesi
            socket = new Socket(this.serverIp, this.port);
            sInput = new DataInputStream(socket.getInputStream());
            sOutput = new DataOutputStream(socket.getOutputStream());

            System.out.println("Connection accepted with server -> " + socket.getInetAddress() + ":" + socket.getPort());

        } catch (Exception err) {
            System.out.println("Error connecting to server: " + err);
        }
        return true;
    }
    

    public void userAddToDatabase(String data) {
        try {
            sOutput.writeUTF(data);
            System.out.println("data send to server is :" + data);
            checkDBServerResult = sInput.readUTF();
            System.out.println("Server says : " + checkDBServerResult);
            sOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void userCheckinDatabase(String data) {
        try {
            sOutput.writeUTF(data);
            System.out.println("data send to server is :" + data);
            checkDBServerResult = sInput.readUTF();
            System.out.println("result of checking in db : " + checkDBServerResult);
            sOutput.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnectClientFromServer(String data) {
        try {
            sOutput.writeUTF(data);
            System.out.println("data send to server is :" + data);
            checkDBServerResult = sInput.readUTF();
            System.out.println("result of checking in db : " + checkDBServerResult);
            sOutput.flush();
            Disconnect(); // Calling the Disconnect method to properly close the connection
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Listen() {
        this.isListening = true;
        this.start();

    }

    @Override
    public void run() {
        try {
            while (this.isListening) {
                byte[] messageByte = new byte[12];
                int bytesRead = sInput.read(messageByte);
                String message = new String(messageByte, 0, bytesRead);
                System.out.println(message);

            }

        } catch (IOException ex) {
            this.Disconnect();
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //mesaj gönderme fonksiyonu
    public void SendMessage(byte[] msg) {
        try {
            sOutput.write(msg);
            sOutput.flush();  
        } catch (IOException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Error sending message: " + e.getMessage(), e);
        }
    }
   
    //clientı kapatan fonksiyon
    public void Disconnect() {
        try {
            this.isListening = false;
            this.socket.close();
            this.sInput.close();
            this.sOutput.close();

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}