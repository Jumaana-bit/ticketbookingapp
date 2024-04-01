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

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @RunWith(SpringRunner.class)
// @WebMvcTest(APIController.class)
public class APIControllerTest {

    // @Autowired
    private MockMvc mockMvc;

    // @MockBean
    private BookingManager bookingManager;

    // @MockBean
    private FlightManager flightManager;

    // @Before
    public void setUp() {
        // Setup mocks for flightManager.getWeeklyFlights() if necessary
    }

    // @Test
    public void testGetAvailableFlights() throws Exception {
        List<Flight> flights = Collections.singletonList(new Flight(1, null, null, "Origin", "Destination", 100.00));
        given(flightManager.getWeeklyFlights()).willReturn(flights);

        mockMvc.perform(get("/api/flights"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(content().json("[{'flightId':1,'origin':'Origin','destination':'Destination','price':100.00}]"));
    }

    // @Test
    // public void testCreateBooking() throws Exception {
    //     Booking booking = new Booking(new User(1, "John Doe", "john.doe@example.com", "password", null, "123456789"), Collections.singletonList(new Flight(1, null, null, "Origin", "Destination", 100.00)), "one-way");
    //     given(bookingManager.createBooking(Mockito.any(User.class), Mockito.anyList(), Mockito.anyString())).willReturn(booking);

    //     mockMvc.perform(post("/api/bookings")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content("{\"userId\":1,\"flights\":[{\"flightId\":1}],\"bookingType\":\"one-way\"}"))
    //            .andExpect(status().isOk());
    //     // Further assertions can be made based on the expected JSON response
    // }
}
