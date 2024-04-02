package sofe3980;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

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

    private User testUser;
    private Flight testFlight;

    @BeforeEach
    public void setUp() {
        // Setup for each test, if necessary
        testUser = new User(1, "Test User", "test@example.com", "password", LocalDate.now(), "AB123456");
        testFlight = new Flight(1, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "CityA", "CityB", 200.00);
    }

    @Test
    public void testSignup() throws Exception {
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("name", "John Doe")
                .param("email", "john.doe@example.com")
                .param("password", "password123")
                .param("dob", "1990-01-01")
                .param("passportNumber", "AB123456"))
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testAuthenticate() throws Exception {
        given(userManager.loginUser(testUser.getEmail(), testUser.getPassword())).willReturn(Optional.of(testUser));

        mockMvc.perform(post("/authenticate")
                .param("email", testUser.getEmail())
                .param("password", testUser.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/flights"));
    }

    @Test
    public void testViewWeeklyFlights() throws Exception {
        given(flightManager.getWeeklyFlights()).willReturn(Collections.singletonList(testFlight));

        mockMvc.perform(get("/flights"))
                .andExpect(status().isOk())
                .andExpect(view().name("flights"))
                .andExpect(model().attributeExists("flights"));
    }

    @Test
    public void testBookFlight() throws Exception {
        given(flightManager.getFlightById(testFlight.getFlightId())).willReturn(Optional.of(testFlight));

        mockMvc.perform(get("/book/{id}", testFlight.getFlightId()))
                .andExpect(status().isOk())
                .andExpect(view().name("flight-confirmation"))
                .andExpect(model().attributeExists("flight"));
    }

    // @Test
    // public void testConfirmFlight() throws Exception {
    //     mockMvc.perform(post("/confirm-flight/{id}", testFlight.getFlightId()))
    //             .andExpect(status().is3xxRedirection())
    //             .andExpect(redirectedUrl("/add-to-itinerary"));
    // }

    // @Test
    // public void testGenerateTickets() throws Exception {
    //     mockMvc.perform(post("/generate-tickets"))
    //             .andExpect(status().is3xxRedirection())
    //             .andExpect(redirectedUrlPattern("/ticket-success*"));
    // }

}
