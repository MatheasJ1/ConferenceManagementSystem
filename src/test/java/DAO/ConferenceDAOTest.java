package DAO;

import com.entity.Conference;
import org.junit.jupiter.api.Test;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ConferenceDAOTest {
    @Test
    void conferenceCanBeCreatedReadUpdatedAndDeleted() throws SQLException {
        ConferenceDAO dao = new ConferenceDAO();
        String uniqueTitle = "DAO test " + UUID.randomUUID();
        int id = -1;

        try {
            Conference original = new Conference(0, uniqueTitle, "Test description", 25,
                    LocalDateTime.of(2027, 5, 1, 9, 0),
                    LocalDateTime.of(2027, 5, 1, 17, 0), "Room A");
            id = dao.addConference(original);
            assertTrue(id > 0);
            Conference loaded = dao.getConferenceById(id);
            assertNotNull(loaded);
            assertEquals(uniqueTitle, loaded.getTitle());

            loaded.setTitle(uniqueTitle + " updated");
            dao.updateConference(loaded);
            assertEquals(uniqueTitle + " updated", dao.getConferenceById(id).getTitle());
            final int createdId = id;
            assertTrue(dao.getAllConferences().stream().anyMatch(c -> c.getConferenceId() == createdId));
            assertTrue(dao.deleteConference(id));
            assertNull(dao.getConferenceById(id));
        }
        finally {
            if (id > 0) {
                dao.deleteConference(id);
            }
        }
    }

    @Test
    void invalidScheduleIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Conference(0, "Invalid", "",
                10, LocalDateTime.of(2027, 1, 1, 12, 0),
                LocalDateTime.of(2027, 1, 1, 11, 0), "Room"));
    }
}