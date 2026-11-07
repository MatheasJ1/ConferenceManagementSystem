package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.database.DatabaseConnection;
import com.entity.Registration;

public class RegistrationDAO {
	private static final Logger LOGGER = Logger.getLogger(RegistrationDAO.class.getName());
	
	 public void addRegistration(Registration registration)
	            throws SQLException {

	        validateState(registration);

	        String sql =
	                "INSERT INTO registrations "
	                + "(user_id, conference_id, status, wait_position) "
	                + "VALUES (?, ?, ?, ?)";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, registration.getUserId());
	            stmt.setInt(2, registration.getConfId());
	            stmt.setString(3, registration.getStatus());
	            setWaitPosition(stmt, 4, registration.getWaitPos());

	            stmt.executeUpdate();

	        } catch (SQLException e) {
	            LOGGER.log(Level.SEVERE, "Failed to add registration", e);
	            throw e;
	        }
	    }
	 
	public Registration getRegistrationById(int id)
	            throws SQLException {

	        String sql =
	                "SELECT * FROM registrations WHERE registration_id = ?";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);

	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return mapRegistration(rs);
	                }
	            }

	        } catch (SQLException e) {
	            LOGGER.log(
	                    Level.SEVERE,
	                    "Failed to retrieve registration ID " + id,
	                    e
	            );
	            throw e;
	        }

	        return null;
	    }

	    public List<Registration> getAllRegistrations()
	            throws SQLException {

	        String sql =
	                "SELECT * FROM registrations ORDER BY registration_id";

	        List<Registration> registrations = new ArrayList<>();

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql);
	             ResultSet rs = stmt.executeQuery()) {

	            while (rs.next()) {
	                registrations.add(mapRegistration(rs));
	            }

	        } catch (SQLException e) {
	            LOGGER.log(
	                    Level.SEVERE,
	                    "Failed to retrieve registrations",
	                    e
	            );
	            throw e;
	        }

	        return registrations;
	    }

	    public void updateRegistration(Registration registration)
	            throws SQLException {

	        validateState(registration);

	        String sql =
	                "UPDATE registrations "
	                + "SET user_id = ?, conference_id = ?, "
	                + "status = ?, wait_position = ? "
	                + "WHERE registration_id = ?";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, registration.getUserId());
	            stmt.setInt(2, registration.getConfId());
	            stmt.setString(3, registration.getStatus());
	            setWaitPosition(stmt, 4, registration.getWaitPos());
	            stmt.setInt(5, registration.getRegId());

	            int rowsUpdated = stmt.executeUpdate();

	            if (rowsUpdated == 0) {
	                throw new SQLException(
	                        "Registration not found: "
	                        + registration.getRegId()
	                );
	            }

	        } catch (SQLException e) {
	            LOGGER.log(
	                    Level.SEVERE,
	                    "Failed to update registration ID "
	                    + registration.getRegId(),
	                    e
	            );
	            throw e;
	        }
	    }

	    public void deleteRegistration(int id) throws SQLException {

	        String sql =
	                "DELETE FROM registrations WHERE registration_id = ?";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, id);

	            int rowsDeleted = stmt.executeUpdate();

	            if (rowsDeleted == 0) {
	                throw new SQLException(
	                        "Registration not found: " + id
	                );
	            }

	        } catch (SQLException e) {
	            LOGGER.log(
	                    Level.SEVERE,
	                    "Failed to delete registration ID " + id,
	                    e
	            );
	            throw e;
	        }
	    }

	    private Registration mapRegistration(ResultSet rs)
	            throws SQLException {

	        int position = rs.getInt("wait_position");

	        
	        Integer waitPosition = rs.wasNull() ? null : position;

	        return new Registration(
	                rs.getInt("registration_id"),
	                rs.getInt("user_id"),
	                rs.getInt("conference_id"),
	                rs.getString("status"),
	                waitPosition
	        );
	    }

	    private void setWaitPosition(
	            PreparedStatement stmt,
	            int parameterIndex,
	            Integer waitPosition
	    ) throws SQLException {

	        if (waitPosition == null) {
	            stmt.setNull(parameterIndex, Types.INTEGER);
	        } else {
	            stmt.setInt(parameterIndex, waitPosition);
	        }
	    }

	    private void validateState(Registration registration) {

	        if (registration == null) {
	            throw new IllegalArgumentException(
	                    "Registration cannot be null."
	            );
	        }

	        
	        registration.setRegistrationState(
	                registration.getStatus(),
	                registration.getWaitPos()
	        );
	    }
}
