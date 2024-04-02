package sofe3980;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(APIController.class)
public class APIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingManager bookingManager;

    @MockBean
    private FlightManager flightManager;

    @Before
    public void setUp() {
        // Setup mocks for flightManager.getWeeklyFlights() if necessary
    }

    @Test
    public void testGetAvailableFlights() throws Exception {
        // Define test data
        List<Flight> testFlights = Arrays.asList(
                new Flight(1, LocalDateTime.now(), LocalDateTime.now().plusHours(2), "New York", "Los Angeles", 300.00),
                new Flight(2, LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(5), "Chicago",
                        "San Francisco", 400.00));

        // Configure mock to return test data
        given(flightManager.getWeeklyFlights()).willReturn(testFlights);

        // Perform the test
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2))) // Expecting 2 flights in the response
                .andExpect(jsonPath("$[0].flightId", is(testFlights.get(0).getFlightId())))
                .andExpect(jsonPath("$[1].flightId", is(testFlights.get(1).getFlightId())));
    }

    // @Test
    // public void testCreateBooking() throws Exception {
    // Booking booking = new Booking(new User(1, "John Doe", "john.doe@example.com",
    // "password", null, "123456789"), Collections.singletonList(new Flight(1, null,
    // null, "Origin", "Destination", 100.00)), "one-way");
    // given(bookingManager.createBooking(Mockito.any(User.class),
    // Mockito.anyList(), Mockito.anyString())).willReturn(booking);

    // mockMvc.perform(post("/api/bookings")
    // .contentType(MediaType.APPLICATION_JSON)
    // .content("{\"userId\":1,\"flights\":[{\"flightId\":1}],\"bookingType\":\"one-way\"}"))
    // .andExpect(status().isOk());
    // // Further assertions can be made based on the expected JSON response
    // }
}
