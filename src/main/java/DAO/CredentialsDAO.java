package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.database.DatabaseConnection;

public class CredentialsDAO {

    private static final Logger LOGGER =
            Logger.getLogger(CredentialsDAO.class.getName());

    public void addCredentials(int userId, String passwordHash)
            throws SQLException {

        String sql =
                "INSERT INTO credentials (user_id, password_hash) "
                + "VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setString(2, passwordHash);
            stmt.executeUpdate();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add credentials", e);
            throw e;
        }
    }

    public String getPasswordHash(int userId) throws SQLException {

        String sql =
                "SELECT password_hash FROM credentials WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }

            return null;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve credentials", e);
            throw e;
        }
    }

    public void updatePasswordHash(int userId, String passwordHash)
            throws SQLException {

        String sql =
                "UPDATE credentials SET password_hash = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, passwordHash);
            stmt.setInt(2, userId);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException(
                        "Credentials not found for user ID " + userId
                );
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to update credentials", e);
            throw e;
        }
    }

    public void deleteCredentials(int userId) throws SQLException {

        String sql =
                "DELETE FROM credentials WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            if (stmt.executeUpdate() == 0) {
                throw new SQLException(
                        "Credentials not found for user ID " + userId
                );
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to delete credentials", e);
            throw e;
        }
    }
}
