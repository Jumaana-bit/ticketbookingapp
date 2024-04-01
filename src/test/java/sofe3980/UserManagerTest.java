package sofe3980;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.Optional;

public class UserManagerTest {

    private UserManager userManager;
    private User registeredUser;
    private String name = "John Doe";
    private String email = "johndoe@example.com";
    private String password = "password";
    private LocalDate dob = LocalDate.now();
    private String passportNumber = "AA123456789";

    @Before
    public void setUp() throws Exception {
        userManager = new UserManager();

        // Register a user before each test
        registeredUser = userManager.registerUser(name, email, password, dob, passportNumber);
    }

    @Test
    public void testRegisterUser() {
        assertNotNull("User should be registered successfully", registeredUser);
        assertEquals("Registered user should have the correct name", name, registeredUser.getName());
        assertEquals("Registered user should have the correct email", email, registeredUser.getEmail());
    }

    @Test
    public void testLoginUser() {
        // Test successful login
        Optional<User> successfulResult = userManager.loginUser(email, password);
        assertTrue("User should be able to log in successfully with correct credentials", successfulResult.isPresent());
        assertEquals("Logged in user should have the correct email", email, successfulResult.get().getEmail());

        // Test unsuccessful login with incorrect email
        Optional<User> wrongEmailResult = userManager.loginUser("wrongemail@example.com", password);
        assertFalse("User should not be able to log in with incorrect email", wrongEmailResult.isPresent());

        // Test unsuccessful login with incorrect password
        Optional<User> wrongPasswordResult = userManager.loginUser(email, "wrongpassword");
        assertFalse("User should not be able to log in with incorrect password", wrongPasswordResult.isPresent());
    }
}
