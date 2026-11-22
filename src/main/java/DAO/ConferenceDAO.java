package DAO;

import com.database.DatabaseConnection;
import com.entity.Conference;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConferenceDAO {
	private static final Logger LOGGER = Logger.getLogger(ConferenceDAO.class.getName());

	public int addConference(Conference conference) throws SQLException {
		String sql = """
                INSERT INTO conferences
                (title, description, capacity, start_datetime, end_datetime, location)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
		try (Connection connection = DatabaseConnection.getConnection(); PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			bindConference(statement, conference);
			statement.executeUpdate();

			try (ResultSet keys = statement.getGeneratedKeys()) {
				if (keys.next()) {
					return keys.getInt(1);
				}
			}

			try (PreparedStatement idQuery = connection.prepareStatement(
					"SELECT conference_id FROM conferences WHERE title = ? AND start_datetime = ?")) {
				idQuery.setString(1, conference.getTitle());
				idQuery.setString(2, conference.getStartTime().toString());

				try (ResultSet rs = idQuery.executeQuery()) {
					if (rs.next()) {
						return rs.getInt(1);
					}
				}
			}

			throw new SQLException("Conference was inserted, but its generated ID could not be retrieved.");
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to create conference.", e);
			throw e;
		}
	}

	public Conference getConferenceById(int id) throws SQLException {
		String sql = "SELECT * FROM conferences WHERE conference_id = ?";

		try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setInt(1, id);

			try (ResultSet rs = p.executeQuery()) {
				return rs.next() ? map(rs) : null;
			}
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to retrieve conference " + id, e);
			throw e;
		}
	}

	public List<Conference> getAllConferences() throws SQLException {
		String sql = "SELECT * FROM conferences ORDER BY start_datetime, title";
		List<Conference> result = new ArrayList<>();

		try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql); ResultSet rs = p.executeQuery()) {
			while (rs.next()) {
				result.add(map(rs));
			}

			return result;
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to retrieve conferences.", e);
			throw e;
		}
	}

	public boolean updateConference(Conference conference) throws SQLException {
		String sql = """
                UPDATE conferences
                SET title = ?, description = ?, capacity = ?, start_datetime = ?, end_datetime = ?, location = ?
                WHERE conference_id = ?
                """;

		try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
			bindConference(p, conference);
			p.setInt(7, conference.getConferenceId());
			return p.executeUpdate() == 1;
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to update conference " + conference.getConferenceId(), e);
			throw e;
		}
	}

	public boolean deleteConference(int id) throws SQLException {
		String sql = "DELETE FROM conferences WHERE conference_id = ?";

		try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setInt(1, id);
			return p.executeUpdate() == 1;
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to delete conference " + id, e);
			throw e;
		}
	}

	public int countConfirmedRegistrations(int conferenceId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM registrations WHERE conference_id = ? AND status = 'CONFIRMED'";

		try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setInt(1, conferenceId);

			try (ResultSet rs = p.executeQuery()) {
				rs.next(); return rs.getInt(1);
			}
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to count confirmed registrations.", e);
			throw e;
		}
	}

	public boolean hasRegistrations(int conferenceId) throws SQLException {
		String sql = "SELECT EXISTS(SELECT 1 FROM registrations WHERE conference_id = ?)";

		try (Connection c = DatabaseConnection.getConnection(); PreparedStatement p = c.prepareStatement(sql)) {
			p.setInt(1, conferenceId);

			try (ResultSet rs = p.executeQuery()) {
				rs.next(); return rs.getInt(1) == 1;
			}
		}
		catch (SQLException e) {
			LOGGER.log(Level.SEVERE, "Unable to check conference registrations.", e);
			throw e;
		}
	}

	private static void bindConference(PreparedStatement p, Conference c) throws SQLException {
		p.setString(1, c.getTitle());
		p.setString(2, c.getDescription());
		p.setInt(3, c.getCapacity());
		p.setString(4, c.getStartTime().toString());
		p.setString(5, c.getEndTime().toString());
		p.setString(6, c.getLocation());
	}

	private static Conference map(ResultSet rs) throws SQLException {
		return new Conference(rs.getInt("conference_id"), rs.getString("title"),
				rs.getString("description"), rs.getInt("capacity"),
				LocalDateTime.parse(rs.getString("start_datetime")),
				LocalDateTime.parse(rs.getString("end_datetime")), rs.getString("location"));
	}
}
