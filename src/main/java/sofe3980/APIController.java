package sofe3980;

import sofe3980.Flight;
import sofe3980.Booking;
import sofe3980.BookingManager;
import sofe3980.FlightManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

// this class will be used to serve the API using RESTful requests
// this will make testing easier
@RestController
@RequestMapping("/api")
public class APIController {

    private final BookingManager bookingManager;
    private final FlightManager flightManager;

    @Autowired
    public APIController(BookingManager bookingManager, FlightManager flightManager) {
        this.bookingManager = bookingManager;
        this.flightManager = flightManager;
    }

    // endpoint for retrieving all flights
    @GetMapping("/flights")
    public List<Flight> getAvailableFlights() {
        return flightManager.getWeeklyFlights();
    }

    // endpoint for creating a new booking
    @PostMapping("/bookings")
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingManager.createBooking(booking.getUser(), booking.getFlights(), booking.getBookingType());
    }

    // endpoint for retrieving a booking by ID
    // ResponseEntity<?> allows the method to return an error HTTP Response with am
    // message if a Booking is not found
    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable int id) {
        Optional<Booking> booking = bookingManager.getBookingById(id);

        if (booking.isPresent()) {
            return ResponseEntity.ok(booking.get()); // Return the booking with status 200 OK
        } else {
            // Return an error message with status 404 Not Found
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Error: Booking with ID " + id + " not found.");
        }
    }

}
