package DAO;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

import com.database.DatabaseConnection;
import com.entity.Conference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceDAO {
	private static final Logger LOGGER = Logger.getLogger(ConferenceDAO.class.getName());
	public void addConference(Conference conference) throws SQLException{
		String sql = "INSERT INTO conferences (title, description, capacity, start_datetime, end_datetime, location) VALUES (?,?,?,?,?,?)";
		
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, conference.getTitle());
			stmt.setString(2, conference.getDescription());
			stmt.setInt(3, conference.getCapacity());
			stmt.setString(4, conference.getStartTime().toString());
			stmt.setString(5, conference.getEndTime().toString());
			stmt.setString(6, conference.getLocation());
			stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to add conference.", e);
			throw e;
		}
	}
	public Conference getConferenceById(int id) throws SQLException{
		String sql = "SELECT * FROM conferences WHERE conference_id = ?";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()) {
					return new Conference( rs.getInt("conference_id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            rs.getInt("capacity"),
                            LocalDateTime.parse(rs.getString("start_datetime")),
                            LocalDateTime.parse(rs.getString("end_datetime")),
                            rs.getString("location")
							);
				}
			}
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to get conference.", e);
			throw e;
		}
		return null;
	}
	public void deleteConference(int id) throws SQLException{
		String sql = "DELETE FROM conferences WHERE conference_id = ?";
		try(Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setInt(1, id);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to delete conference.", e);
			throw e;
		}
	}
	public void updateConference(Conference conference) throws SQLException{
		String sql = "UPDATE conferences "
				+ "SET title = ?, description = ?, capacity = ?, start_datetime = ?, end_datetime = ?, location = ? WHERE conference_id = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql)){
			stmt.setString(1, conference.getTitle());
	        stmt.setString(2, conference.getDescription());
	        stmt.setInt(3, conference.getCapacity());
	        stmt.setString(4, conference.getStartTime().toString());
	        stmt.setString(5, conference.getEndTime().toString());
	        stmt.setString(6, conference.getLocation());
	        stmt.setInt(7, conference.getConferenceId());

	        stmt.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Failed to update conference.", e);
			throw e;
		}
	}
}
