/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chat_app;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author Casper
 */
public class ServerFrame extends javax.swing.JFrame {

    /**
     * Creates new form ServerFrame
     */
    // creat a model to add the connected clients.
    public static DefaultListModel clientsListModel = new DefaultListModel();
    public static Server server;

    public ServerFrame() {
        initComponents();
        connectedClientsList.setModel(clientsListModel);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        portTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        connectedClientsList = new javax.swing.JList<>();
        startServerBtn = new javax.swing.JButton();
        stopServerBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server_test");
        setBackground(new java.awt.Color(0, 204, 204));

        jLabel1.setText("Port No: ");

        portTxt.setText("8080");
        portTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                portTxtActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(connectedClientsList);

        startServerBtn.setText("Start");
        startServerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startServerBtnActionPerformed(evt);
            }
        });

        stopServerBtn.setText("Stop");
        stopServerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopServerBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Connected Clients: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(startServerBtn)
                                        .addGap(18, 18, 18)
                                        .addComponent(stopServerBtn))
                                    .addComponent(portTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 167, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(portTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startServerBtn)
                    .addComponent(stopServerBtn))
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void startServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startServerBtnActionPerformed

        String portString = portTxt.getText().trim();
        // make sure the port number non empty and contain numbers only. 
        if (portString.isEmpty() || !portString.matches("^\\d+$")) {
            // Inform the user about the incorrect input.
            JOptionPane.showMessageDialog(this, "Please enter a valid port number (non-empty, digits only).", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else {
            // Parse the port number and proceed with server creation.
            int port = Integer.parseInt(portString);
            server = new Server();
            if (server.Create(port)) { 
                this.setEnabled(false);
                stopServerBtn.setEnabled(true);
                server.Listen();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to start the server on port " + port, "Server Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_startServerBtnActionPerformed

    private void stopServerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopServerBtnActionPerformed
        // stopping the server.
        server.Stop();
        this.setVisible(false);

    }//GEN-LAST:event_stopServerBtnActionPerformed

    private void portTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_portTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_portTxtActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> connectedClientsList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField portTxt;
    private javax.swing.JButton startServerBtn;
    private javax.swing.JButton stopServerBtn;
    // End of variables declaration//GEN-END:variables
}
