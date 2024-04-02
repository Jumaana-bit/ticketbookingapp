package sofe3980;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserManager userManager; // Mock UserManager

    @MockBean
    private FlightManager flightManager; // Keep if FlightManager is used in BookingController

    @MockBean
    private BookingManager bookingManager; // Mock BookingManager if it's still used in BookingController

    @BeforeEach
    public void setUp() {
        // Setup for each test, if necessary
    }

    @Test
    public void testSignup() throws Exception {
        mockMvc.perform(post("/booking/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "John Doe")
                .param("email", "john.doe@example.com")
                .param("password", "password123")
                .param("dob", "1990-01-01")
                .param("passportNumber", "AB123456"))
                .andExpect(redirectedUrl("/booking/login"));
    }
    

    // Other tests...
}
