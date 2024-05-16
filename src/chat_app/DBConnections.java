/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Casper
 */
package chat_app;

import java.sql.*;

/**
 * This class handle all database operations for chat app. It make easier to
 * connect, add, and get data from database.
 */
public class DBConnections {

    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/ChatApp_db";
    private String username = "root";
    private String password = "Rahma123321123";

    /**
     * Constructor to make database connection when this class is created.
     */
    public DBConnections() {
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        }
    }

    /**
     * Method to check sign in with email and return result set with user data
     * if found.
     */
    public ResultSet signInConnection(String email) throws SQLException {
        String sql = "SELECT * FROM clients WHERE email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        return statement.executeQuery();
    }

    /**
     * Method to register new user. It checks if email exist and if not, add new
     * user.
     */
    public int signUpConnection(String name, String lastName, String email, String passwordData) throws SQLException {
        String checkIfExistsSql = "SELECT COUNT(*) AS count FROM clients WHERE email = ?";
        PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsSql);
        checkIfExistsStatement.setString(1, email);
        ResultSet resultSet = checkIfExistsStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        if (count > 0) {
            return 0; // Email already exists
        } else {
            String insertSql = "INSERT INTO clients (name, surname, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, name);
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, email);
            insertStatement.setString(4, passwordData);
            return insertStatement.executeUpdate();
        }
    }

    /**
     * Create new project with email, project name, and key. Return key if
     * success.
     */
    public String createProjectConnection(String adminEmail, String projectName, String projectKey) throws SQLException {
        String insertQuery = "INSERT INTO projects (admin_email, project_name, project_key) VALUES (?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(insertQuery);
        pstmt.setString(1, adminEmail);
        pstmt.setString(2, projectName);
        pstmt.setString(3, projectKey);
        int rows = pstmt.executeUpdate();
        return rows > 0 ? projectKey : null;
    }

    /**
     * Delete the user from userProjects after leaving the project.
     */
    public int removeUserFromProject(String email, String projectName) throws SQLException {
        String deleteQuery = "DELETE FROM userProjects WHERE email = ? AND project_name = ?";
        PreparedStatement pstmt = connection.prepareStatement(deleteQuery);
        pstmt.setString(1, email);
        pstmt.setString(2, projectName);
        return pstmt.executeUpdate();
    }

    /**
     * Get project members by project name.
     */
    public ResultSet getProjectMembersConnection(String projectName) throws SQLException {
        String query = "SELECT email FROM userProjects WHERE project_name = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, projectName);
        return stmt.executeQuery();
    }

    /**
     * Get project key by admin email.
     */
    public String getProjectKeyConnection(String email) throws SQLException {
        String key = "";
        String sql = "SELECT project_key FROM projects WHERE admin_email = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            key = resultSet.getString("project_key");
        }
        resultSet.close();
        statement.close();
        return key;
    }

    /**
     * Add user to project. Return number of rows affected.
     */
    public int addUserToProjectConnection(String email, String projectName) throws SQLException {
        String insertQuery = "INSERT INTO userProjects (email, project_name) VALUES (?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(insertQuery);
        pstmt.setString(1, email);
        pstmt.setString(2, projectName);
        return pstmt.executeUpdate();
    }

    /**
     * Check if project key is correct for given project name.
     */
    public boolean checkProjectKeyConnection(String projectName, String projectKey) throws SQLException {
        String selectQuery = "SELECT project_key FROM projects WHERE project_name = ?";
        PreparedStatement pstmt = connection.prepareStatement(selectQuery);
        pstmt.setString(1, projectName);
        ResultSet rs = pstmt.executeQuery();
        boolean isValid = false;
        if (rs.next()) {
            String storedProjectKey = rs.getString("project_key");
            isValid = storedProjectKey.equals(projectKey);
        }
        rs.close();
        pstmt.close();
        return isValid;
    }

    /**
     * Get user projects by email.
     */
    public String getUserProjects(String email) throws SQLException {
        StringBuilder projectNameBuilder = new StringBuilder();
        String selectQuery = "SELECT project_name FROM userProjects WHERE email = ?";
        PreparedStatement pstmt = connection.prepareStatement(selectQuery);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            projectNameBuilder.append(rs.getString("project_name")).append(',');
        }
        rs.close();
        pstmt.close();
        return projectNameBuilder.toString();
    }

    /**
     * Close database connection when done.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database connection: " + e.getMessage());
        }
    }
}
