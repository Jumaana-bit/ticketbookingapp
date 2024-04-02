package sofe3980;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;

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
                .andExpect(jsonPath("$[0].flightId", is(1))) // Expecting flightId 1
                .andExpect(jsonPath("$[1].flightId", is(2))); // Expecting flightId 2
    }

    @Test
    public void testCreateBooking() throws Exception {
        // Setup test data for the booking
        User user = new User(1, "John Doe", "john.doe@example.com", "password", LocalDate.of(1990, 1, 1), "AB123456");
        List<Flight> flights = Collections.singletonList(new Flight(1, LocalDateTime.now(),
                LocalDateTime.now().plusHours(2), "New York", "Los Angeles", 300.00));
        Booking booking = new Booking(1, user, flights, "one-way");

        // Mock the behavior of the BookingManager
        given(bookingManager.createBooking(any(), anyList(), anyString())).willReturn(booking);

        // Define the booking payload as JSON
        String bookingPayload = "{ \"userId\": 1, \"flights\": [{ \"flightId\": 1 }], \"bookingType\": \"one-way\" }";

        // Perform the POST request
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookingPayload))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId", is(1)))
                .andExpect(jsonPath("$.user.userId", is(user.getUserId())))
                .andExpect(jsonPath("$.flights[0].flightId", is(1)))
                .andExpect(jsonPath("$.bookingType", is("one-way")));

        // Optionally, verify that BookingManager was called with the right parameters
        verify(bookingManager).createBooking(any(), anyList(), eq("one-way"));
    }

    @Test
    public void testGetBookingById() throws Exception {
        // Setup test data
        User user = new User(1, "John Doe", "john.doe@example.com", "password", LocalDate.of(1990, 1, 1), "AB123456");
        List<Flight> flights = Collections.singletonList(new Flight(1, LocalDateTime.now(),
                LocalDateTime.now().plusHours(2), "New York", "Los Angeles", 300.00));
        Booking expectedBooking = new Booking(1, user, flights, "one-way");

        // Mock the behavior of BookingManager
        given(bookingManager.getBookingById(1)).willReturn(Optional.of(expectedBooking));

        // Perform the GET request
        mockMvc.perform(get("/api/bookings/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookingId", is(expectedBooking.getBookingId())))
                .andExpect(jsonPath("$.user.userId", is(user.getUserId())))
                .andExpect(jsonPath("$.flights[0].flightId", is(1)))
                .andExpect(jsonPath("$.bookingType", is(expectedBooking.getBookingType())));
    }

}
