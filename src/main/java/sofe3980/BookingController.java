package sofe3980;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// this class will be used to serve the web page UI
@Controller
@RequestMapping("/booking")
public class BookingController {

    private final BookingManager bookingManager;
    private final FlightManager flightManager;
    private final UserManager userManager;

    @Autowired
    public BookingController(BookingManager bookingManager, FlightManager flightManager, UserManager userManager) {
        this.bookingManager = bookingManager;
        this.flightManager = flightManager;
        this.userManager = userManager;
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User()); // Create an empty User object for form binding
        return "signup";
    }

    @PostMapping("/signup")
    public String handleSignup(@ModelAttribute User user) {
        // Attempt to register the user
        try {
            userManager.registerUser(user.getName(), user.getEmail(), user.getPassword(), user.getDob(),
                    user.getPassportNumber());
        } catch (Exception e) {
            // Log the exception or handle it according to your application's requirements
            System.err.println("Error registering user: " + e.getMessage());
            // Consider returning a different view if registration fails
            return "signup";
        }

        // Redirect to the login page on successful registration
        return "redirect:/booking/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Assumes you have login.html in the 'templates' directory (for Thymeleaf) or
                        // as a static file
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String email, @RequestParam String password) {
        // Placeholder for authentication logic
        // Redirect based on successful or failed authentication

        // Example: redirect to a "dashboard" page on successful authentication
        return "redirect:/booking/dashboard";
    }

    /**
     * Displays the main menu options to the user.
     * 
     */
    @GetMapping("/index")
    public void displayMainMenu() {
    }

    /**
     * Handles the request to view weekly flights.
     * Fetch available flights from the FlightManager and display them.
     */
    @GetMapping("/flights")
    public void viewWeeklyFlights() {
    }

    /**
     * Processes the booking request from the user.
     * Take booking details from the request, create a booking via the
     * BookingManager,
     * 
     * @param bookingDetails Details of the booking submitted by the user.
     */
    @PostMapping("/makeBooking")
    public void createBooking(@RequestBody Booking bookingDetails) {
    }

    /**
     * Displays booking confirmation details to the user.
     * After a booking is successfully made, show the user their confirmed booking
     * details.
     * 
     * @param booking The booking to confirm.
     */
    public void displayBookingConfirmation(Booking booking) {
    }
}
