package DAO;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import com.database.DatabaseConnection;
import com.entity.Conference;

class ConferenceDAOTest {
    @Test
    void testConferenceCRUD() throws SQLException {
        ConferenceDAO dao = new ConferenceDAO();
        String title = "Test Conference " + UUID.randomUUID();
        Conference conference = new Conference(
                0,
                title,
                "Testing conference CRUD operations",
                50,
                LocalDateTime.of(2027, 5, 10, 9, 0),
                LocalDateTime.of(2027, 5, 10, 17, 0),
                "Room 202"
        );

        Integer conferenceId = null;

        try {
            dao.addConference(conference);

            conferenceId = findConferenceId(title);

            assertNotNull(conferenceId, "Conference should be inserted into the database.");

            Conference retrieved = dao.getConferenceById(conferenceId);
            assertNotNull(retrieved);
            assertEquals(title, retrieved.getTitle());
            assertEquals("Testing conference CRUD operations", retrieved.getDescription());
            assertEquals(50, retrieved.getCapacity());
            assertEquals(
                    LocalDateTime.of(2027, 5, 10, 9, 0),
                    retrieved.getStartTime()
            );
            assertEquals(
                    LocalDateTime.of(2027, 5, 10, 17, 0),
                    retrieved.getEndTime()
            );
            assertEquals("Room 202", retrieved.getLocation());

            retrieved.setTitle("Updated Test Conference");
            retrieved.setDescription("Updated description");
            retrieved.setCapacity(100);
            retrieved.setTimes(
                    LocalDateTime.of(2027, 6, 15, 10, 0),
                    LocalDateTime.of(2027, 6, 15, 16, 0)
            );
            retrieved.setLocation("Room 303");
            dao.updateConference(retrieved);
            Conference updated = dao.getConferenceById(conferenceId);
            assertNotNull(updated);
            assertEquals("Updated Test Conference", updated.getTitle());
            assertEquals("Updated description", updated.getDescription());
            assertEquals(100, updated.getCapacity());
            assertEquals("Room 303", updated.getLocation());

            dao.deleteConference(conferenceId);
            Conference deleted = dao.getConferenceById(conferenceId);
            assertNull(deleted, "Conference should no longer exist after deletion.");
        }
        finally {
            if (conferenceId != null) {
                dao.deleteConference(conferenceId);
            }
        }
    }

    private int findConferenceId(String title) throws SQLException {
        String sql = "SELECT conference_id FROM conferences WHERE title = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("conference_id");
                }
            }
        }

        return -1;
    }

    @Test
    void testGetConferenceByInvalidId() throws SQLException {

        ConferenceDAO dao = new ConferenceDAO();

        Conference result = dao.getConferenceById(-1);

        assertNull(result, "An invalid conference ID should return null.");
    }
}
