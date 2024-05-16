/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chat_app;

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
            this.connection = DriverManager.getConnection(url, username, password);  // Try connect to database.
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());  // If error, print it.
        }
    }

    /**
     * Method to check sign in with email and return result set with user data
     * if found.
     */
    public ResultSet signInConnection(String email) throws SQLException {
        String sql = "SELECT * FROM clients WHERE email = ?";  // SQL query.
        PreparedStatement statement = connection.prepareStatement(sql);  // Prepare statement with SQL.
        statement.setString(1, email);  // Set email in query.
        return statement.executeQuery();  // Execute query and return result.
    }

    /**
     * Method to register new user. It checks if email exist and if not, add new
     * user.
     */
    public int signUpConnection(String name, String lastName, String email, String passwordData) throws SQLException {
        String checkIfExistsSql = "SELECT COUNT(*) AS count FROM clients WHERE email = ?";  // SQL to check email.
        PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsSql);
        checkIfExistsStatement.setString(1, email);
        ResultSet resultSet = checkIfExistsStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        if (count > 0) {
            return 0; // Email already exists
        } else {
            String insertSql = "INSERT INTO clients (name, surname, email, password) VALUES (?, ?, ?, ?)";  // SQL to insert new user.
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);
            insertStatement.setString(1, name);
            insertStatement.setString(2, lastName);
            insertStatement.setString(3, email);
            insertStatement.setString(4, passwordData);
            return insertStatement.executeUpdate();  // Execute update and return number of rows affected.
        }
    }

    /**
     * Create new project with email, project name, and key. Return key if
     * success.
     */
    public String createProjectConnection(String adminEmail, String projectName, String projectKey) throws SQLException {
        String insertQuery = "INSERT INTO Projects (admin_email, project_name, project_key) VALUES (?, ?, ?)";  // SQL to insert new project.
        PreparedStatement pstmt = connection.prepareStatement(insertQuery);
        pstmt.setString(1, adminEmail);
        pstmt.setString(2, projectName);
        pstmt.setString(3, projectKey);
        int rows = pstmt.executeUpdate();  // Execute update and check rows affected.
        return rows > 0 ? projectKey : null;
    }

    /**
     * Get project members by project name.
     */
    public ResultSet getProjectMembersConnection(String projectName) throws SQLException {
        String query = "SELECT email FROM userProjects WHERE project_name = ?";  // SQL to get emails by project.
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, projectName);
        return stmt.executeQuery();  // Execute query and return results.
    }

    /**
     * Get project key by manager email.
     */
    public String getProjectKeyConnection(String email) throws SQLException {
        String key = "";
        String sql = "SELECT project_key FROM Projects WHERE manager_email = ?";  // SQL to get project key.
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            key = resultSet.getString("project_key");  // Get key from result.
        }
        resultSet.close();  // Close result set.
        statement.close();  // Close statement.
        return key;  // Return key.
    }

    /**
     * Add user to project. Return number of rows affected.
     */
    public int addUserToProjectConnection(String name, String lastName, String email, String projectName) throws SQLException {
        String insertQuery = "INSERT INTO userProjects (name, last_name, email, project_name) VALUES (?, ?, ?, ?)";  // SQL to add user to project.
        PreparedStatement pstmt = connection.prepareStatement(insertQuery);
        pstmt.setString(1, name);
        pstmt.setString(2, lastName);
        pstmt.setString(3, email);
        pstmt.setString(4, projectName);
        return pstmt.executeUpdate();  // Execute update and return result.
    }

    /**
     * Check if project key is correct for given project name.
     */
    public boolean checkProjectKeyConnection(String projectName, String projectKey) throws SQLException {
        String selectQuery = "SELECT project_key FROM Projects WHERE project_name = ?";  // SQL to check project key.
        PreparedStatement pstmt = connection.prepareStatement(selectQuery);
        pstmt.setString(1, projectName);
        ResultSet rs = pstmt.executeQuery();
        boolean isValid = false;
        if (rs.next()) {
            String storedProjectKey = rs.getString("project_key");
            isValid = storedProjectKey.equals(projectKey);  // Compare keys.
        }
        rs.close();  // Close result set.
        pstmt.close();  // Close statement.
        return isValid;  // Return check result.
    }

    /**
     * Get user projects by email.
     */
    public String getUserProjects(String email) throws SQLException {
        StringBuilder projectNameBuilder = new StringBuilder();
        String selectQuery = "SELECT project_name FROM userProjects WHERE email = ?";  // SQL to get projects by email.
        PreparedStatement pstmt = connection.prepareStatement(selectQuery);
        pstmt.setString(1, email);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            projectNameBuilder.append(rs.getString("project_name")).append(',');  // Build string with project names.
        }
        rs.close();  // Close result set.
        pstmt.close();  // Close statement.
        return projectNameBuilder.toString();  // Return project names.
    }

    /**
     * Close database connection when done.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();  // Close connection if not null.
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database connection: " + e.getMessage());  // Print error if fail.
        }
    }
}
