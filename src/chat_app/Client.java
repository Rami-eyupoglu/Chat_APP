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

    // her clientın bir soketi olamlı
    public Socket socket;
    private Server server;
    // gönderilecek alınacak bilgileri byte dizisine çevirmek için
    private DataInputStream sInput;
    //private DataInputStream sInput;
    private DataOutputStream sOutput;

    public boolean isListening = false;

    //yapıcı metod
    public Client(Socket socket, Server server) {

        try {
            this.server = server;
            this.socket = socket;
            this.sInput = new DataInputStream(this.socket.getInputStream());
            this.sOutput = new DataOutputStream(this.socket.getOutputStream());
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

    public String ListenForResponse() {
        try {
            byte[] messageByte = new byte[1024]; // Assume response will fit in this buffer
            int bytesRead = sInput.read(messageByte);
            if (bytesRead > 0) {
                return new String(messageByte, 0, bytesRead);
            }
        } catch (IOException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, "Error listening for response: " + e.getMessage(), e);
        }
        return "";
    }

    //clientı kapatan fonksiyon
    public void Disconnect() {
        try {
            this.isListening = false;
            this.socket.close();
            this.sInput.close();
            this.sOutput.close();
            this.server.DicconnectClient(this);

        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}