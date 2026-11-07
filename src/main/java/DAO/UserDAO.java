package DAO;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.database.DatabaseConnection;
import com.entity.Role;
import com.entity.User;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
	private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
	public void addUser(User user, String passwordHash)
	        throws SQLException {

	    String userSql =
	            "INSERT INTO users (full_name, email, age, role) "
	            + "VALUES (?, ?, ?, ?)";

	    String credentialsSql =
	            "INSERT INTO credentials (user_id, password_hash) "
	            + "VALUES (?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection()) {
	        conn.setAutoCommit(false);

	        try {
	            int userId;

	            try (PreparedStatement stmt = conn.prepareStatement(
	                    userSql, Statement.RETURN_GENERATED_KEYS)) {

	                stmt.setString(1, user.getFullName());
	                stmt.setString(2, user.getEmail());
	                stmt.setInt(3, user.getAge());
	                stmt.setString(4, "PARTICIPANT");
	                stmt.executeUpdate();

	                try (ResultSet keys = stmt.getGeneratedKeys()) {
	                    if (!keys.next()) {
	                        throw new SQLException("User ID was not generated.");
	                    }

	                    userId = keys.getInt(1);
	                }
	            }

	            try (PreparedStatement stmt =
	                    conn.prepareStatement(credentialsSql)) {

	                stmt.setInt(1, userId);
	                stmt.setString(2, passwordHash);
	                stmt.executeUpdate();
	            }

	            conn.commit();

	        } catch (SQLException | RuntimeException e) {
	            try {
	                conn.rollback();
	            } catch (SQLException rollbackError) {
	                e.addSuppressed(rollbackError);
	            }

	            throw e;
	        }

	    } catch (SQLException | RuntimeException e) {
	        LOGGER.log(Level.SEVERE, "Failed to create user and credentials.", e);
	        throw e;
	    }
	}
	
	
	public User getUserById(int id) throws SQLException {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return new User(rs.getInt("user_id"), rs.getString("full_name"), rs.getString("email"), rs.getInt("age"),Role.valueOf(rs.getString("role")));
				}
			}
			
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to create participant", e);
			throw e;
		}
		return null;
		
	}
	public List<User> getAllUsers() throws SQLException{
		String sql = "SELECT * FROM users";
		List<User> users = new ArrayList<>();
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				 User p = new User(rs.getInt("user_id"),
						 rs.getString("full_name"),
						 rs.getString("email"),
						 rs.getInt("age"),
						 Role.valueOf(rs.getString("role")));
				users.add(p);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to retrieve all users.", e);
			throw e;
		}
		return users;
	}
	
	public void updateUser(User p) throws SQLException{
		String sql = "UPDATE users "
				+ "SET full_name = ?, email = ?, age = ? WHERE user_id = ? AND role = 'PARTICIPANT'";
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1,p.getFullName());
			stmt.setString(2,p.getEmail());
			stmt.setInt(3, p.getAge());
			stmt.setInt(4,  p.getUserId());
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to update user.", e);
			throw e;
		}
				
	}
	public void deleteUser(int id) throws SQLException{
		String sql = "DELETE FROM users WHERE user_id = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt  = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to delete user.", e);
			throw e;
		}
	}
}
