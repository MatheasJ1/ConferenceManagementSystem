package DAO;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import com.entity.Role;
import com.entity.User;

class UserDAOTest {

	 @Test
	    void testUserCRUD() throws SQLException {
	        UserDAO dao = new UserDAO();

	        String email = UUID.randomUUID() + "@example.com";
	        String passwordHash = BCrypt.hashpw(
	                "test123", BCrypt.gensalt()
	        );

	        Integer userId = null;

	        try {
	            
	            dao.addUser(
	                    new User(0, "Test User", email, 20),
	                    passwordHash
	            );

	            User created = dao.getAllUsers().stream()
	                    .filter(user -> email.equals(user.getEmail()))
	                    .findFirst()
	                    .orElseThrow(() ->
	                            new AssertionError("User was not created.")
	                    );

	            userId = created.getUserId();

	           
	            User retrieved = dao.getUserById(userId);

	            assertNotNull(retrieved);
	            assertEquals("Test User", retrieved.getFullName());
	            assertEquals(email, retrieved.getEmail());
	            assertEquals(20, retrieved.getAge());
	            assertEquals(Role.PARTICIPANT, retrieved.getRole());

	            
	            retrieved.setFullName("Updated User");
	            retrieved.setEmail("updated-" + email);
	            retrieved.setAge(25);

	            dao.updateUser(retrieved);

	            User updated = dao.getUserById(userId);

	            assertNotNull(updated);
	            assertEquals("Updated User", updated.getFullName());
	            assertEquals("updated-" + email, updated.getEmail());
	            assertEquals(25, updated.getAge());
	            assertEquals(Role.PARTICIPANT, updated.getRole());

	           new CredentialsDAO().deleteCredentials(userId);
	            dao.deleteUser(userId);

	            assertNull(dao.getUserById(userId));

	        } finally {
	            if (userId != null && dao.getUserById(userId) != null) {
	                CredentialsDAO credentialsDAO = new CredentialsDAO();

	                if (credentialsDAO.getPasswordHash(userId) != null) {
	                    credentialsDAO.deleteCredentials(userId);
	                }

	                dao.deleteUser(userId);
	            }
	        }
	    }

}
