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
public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public static String projectName;
    public static Project project;
    public static DefaultListModel projectListModel = new DefaultListModel();
    Client client;
    static String data = "";

    public MainFrame() {
        initComponents();
        clientProjectsList.setModel(projectListModel);
        client = SignInFrame.client;
        UserNameSurname.setText("User : " + client.clientName + " " + client.clientLastName);
        UserEmail.setText("Email : " + client.clientEmail);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        UserNameSurname = new javax.swing.JLabel();
        UserEmail = new javax.swing.JLabel();
        jButtonJoinProject = new javax.swing.JButton();
        ceartProjectBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        clientProjectsList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jTextProName = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextProKey = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jtxtCreProName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 204, 204));

        UserNameSurname.setBackground(new java.awt.Color(0, 204, 204));
        UserNameSurname.setText("UserNameSurname");

        UserEmail.setBackground(new java.awt.Color(0, 204, 204));
        UserEmail.setText("UserEmail");

        jButtonJoinProject.setText("Join Project");
        jButtonJoinProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonJoinProjectActionPerformed(evt);
            }
        });

        ceartProjectBtn.setText("Create Project");
        ceartProjectBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ceartProjectBtnActionPerformed(evt);
            }
        });

        clientProjectsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clientProjectsListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(clientProjectsList);

        jLabel1.setText("Joined projects:");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel2.setText("Project name: ");

        jLabel3.setText("Project Key: ");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("OR");

        jLabel5.setText("Project Name: ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jTextProName, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButtonJoinProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jTextProKey)
                                            .addComponent(jtxtCreProName)
                                            .addComponent(ceartProjectBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))))
                                .addGap(32, 32, 32))
                            .addComponent(UserEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(UserNameSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(195, 195, 195)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 38, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(UserNameSurname)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UserEmail)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(66, 66, 66)
                                .addComponent(jTextProName, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jTextProKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButtonJoinProject)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel4)
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jtxtCreProName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(ceartProjectBtn)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 83, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonJoinProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonJoinProjectActionPerformed
        // TODO add your handling code here:
        String projectName = jTextProName.getText();
        String projectKey = jTextProKey.getText();

        String packet = buildJoinProjectPacket("joinProject", projectName, projectKey, client.clientName, client.clientLastName, client.clientEmail);

        client.joinProject(packet);

        String[] serverFeedback = client.serverResponse.split(",");
        if (serverFeedback[0].equals("joinProject_done")) {

            MainFrame.projectListModel.addElement(projectName);
            Project.connectedClients.add(client.socket);
            JOptionPane.showMessageDialog(this, "Join the project succefully!");
        } else if (serverFeedback[0].equals("joinProject_failed")) {
            JOptionPane.showMessageDialog(this, "Did not joined the project!");
        }

    }//GEN-LAST:event_jButtonJoinProjectActionPerformed

    private void ceartProjectBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ceartProjectBtnActionPerformed
        // TODO add your handling code here:
        String serverKey = "";
        String projectName = jtxtCreProName.getText();

        String packet = buildCreateProjectPacket("CreateProject", projectName, client.clientEmail, client.clientName);

        // project manager
        client.createProject(packet);
        String[] serverReponses = client.serverResponse.split(",");

        if (serverReponses[0].equals("CreateProject_done")) {
            project = new Project(projectName, client.clientName);
            serverKey = serverReponses[0];
            project.projectServerKey = serverKey;
            MainFrame.projectListModel.addElement(projectName + " ---> " + serverReponses[1]);
            JOptionPane.showMessageDialog(this, "project created!");
            System.out.println("project created done!!");
        } else if (serverReponses[0].equals("CreateProject_nameDuplicate")) {
            JOptionPane.showMessageDialog(this, "This project name is already used!");
        }

    }//GEN-LAST:event_ceartProjectBtnActionPerformed

    private void clientProjectsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientProjectsListMouseClicked
        // TODO add your handling code here
        projectName = clientProjectsList.getSelectedValue();
        ProjectFrame pFrame = new ProjectFrame();
        pFrame.setVisible(true);
    }//GEN-LAST:event_clientProjectsListMouseClicked
    // make the packet to Join project to send to the server.
    public String buildJoinProjectPacket(String opType, String projectName, String projectKey, String clientName, String clientLastName, String clientEmail) {
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(opType);
        dataBuilder.append(",");
        dataBuilder.append(projectName);
        dataBuilder.append(",");
        dataBuilder.append(projectKey);
        dataBuilder.append(",");
        dataBuilder.append(clientName);
        dataBuilder.append(",");
        dataBuilder.append(clientLastName);
        dataBuilder.append(",");
        dataBuilder.append(clientEmail);
        return dataBuilder.toString();
    }
    // make the packet to create project to send to the server.
    public String buildCreateProjectPacket(String opType, String projectName, String clientEmail, String clientName) {
        StringBuilder dataBuilder = new StringBuilder();
        dataBuilder.append(opType);  // "3" indicates the specific command for creating a project
        dataBuilder.append(",");
        dataBuilder.append(clientEmail);
        dataBuilder.append(",");
        dataBuilder.append(projectName);
        dataBuilder.append(",");
        dataBuilder.append(clientName);
        return dataBuilder.toString();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced dataInputStream Java SE 6) is not available, stay with the default look and feel.
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
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel UserEmail;
    private javax.swing.JLabel UserNameSurname;
    private javax.swing.JButton ceartProjectBtn;
    private javax.swing.JList<String> clientProjectsList;
    private javax.swing.JButton jButtonJoinProject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextProKey;
    private javax.swing.JTextField jTextProName;
    private javax.swing.JTextField jtxtCreProName;
    // End of variables declaration//GEN-END:variables
}
