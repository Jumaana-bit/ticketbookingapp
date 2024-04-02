package sofe3980;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// this class will be used to serve the web page UI
@Controller
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
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Assumes you have login.html in the 'templates' directory (for Thymeleaf) or
                        // as a static file
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestParam String email, @RequestParam String password,
            RedirectAttributes redirectAttributes, HttpSession session) {
        // Attempt to login user with provided credentials
        Optional<User> user = userManager.loginUser(email, password);

        if (user.isPresent()) {
            // Store user's name in the session
            session.setAttribute("userName", user.get().getName());
            // Authentication successful, redirect to flights page
            return "redirect:/flights";
        } else {
            // Authentication failed, redirect back to login page with error message
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    /**
     * Handles the request to view weekly flights.
     * Fetch available flights from the FlightManager and display them.
     */
    @GetMapping("/flights")
    public String viewWeeklyFlights(@RequestParam Optional<String> from,
            @RequestParam Optional<String> to,
            @RequestParam Optional<String> dateString,
            Model model, HttpSession session) {
        final LocalDate[] searchDateContainer = { null };

        dateString.ifPresent(d -> session.setAttribute("searchDate", d));

        String sessionDateStr = (String) session.getAttribute("searchDate");
        if (sessionDateStr != null) {
            try {
                searchDateContainer[0] = LocalDate.parse(sessionDateStr, DateTimeFormatter.ISO_DATE);
            } catch (DateTimeParseException e) {
                session.removeAttribute("searchDate");
                model.addAttribute("dateError", "Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        from.ifPresent(f -> session.setAttribute("searchFrom", f));
        to.ifPresent(t -> session.setAttribute("searchTo", t));

        String searchFrom = (String) session.getAttribute("searchFrom");
        String searchTo = (String) session.getAttribute("searchTo");
        String userName = (String) session.getAttribute("userName");

        List<Flight> flights = flightManager.getWeeklyFlights();

        flights = flights.stream()
                .filter(flight -> searchFrom == null || flight.getDepartureLocation().equalsIgnoreCase(searchFrom))
                .filter(flight -> searchTo == null || flight.getDestinationLocation().equalsIgnoreCase(searchTo))
                .filter(flight -> searchDateContainer[0] == null
                        || flight.getDepartureTime().toLocalDate().isEqual(searchDateContainer[0]))
                .collect(Collectors.toList());

        model.addAttribute("flights", flights);
        model.addAttribute("userName", userName);
        model.addAttribute("searchFrom", searchFrom);
        model.addAttribute("searchTo", searchTo);
        model.addAttribute("searchDate", sessionDateStr);

        return "flights";
    }

    @GetMapping("/resetSearch")
    public String resetSearch(HttpSession session) {
        // Remove search criteria from the session
        session.removeAttribute("searchFrom");
        session.removeAttribute("searchTo");
        session.removeAttribute("searchDate");

        // Redirect back to the flights page
        return "redirect:/flights";
    }

    @GetMapping("/book/{id}")
    public String bookFlight(@PathVariable("id") int id, Model model) {
        // Assuming you have a method to get a flight by its ID
        Optional<Flight> flight = flightManager.getFlightById(id);

        if (flight.isPresent()) {
            model.addAttribute("flight", flight.get());
            return "flight-confirmation"; // Return the view for booking confirmation
        } else {
            // Handle the case where the flight ID is not found
            return "redirect:/flights"; // For example, redirect back to the flights list
        }
    }

    @PostMapping("/confirm-flight/{id}")
    public String confirmFlight(@PathVariable("id") int id, HttpSession session) {
        Optional<Flight> flightOpt = flightManager.getFlightById(id);

        if (flightOpt.isPresent()) {
            Flight confirmedFlight = flightOpt.get();

            // Retrieve or create the itinerary list from the session
            List<Flight> itinerary = (List<Flight>) session.getAttribute("itinerary");
            if (itinerary == null) {
                itinerary = new ArrayList<>();
                session.setAttribute("itinerary", itinerary);
            }

            // Add the confirmed flight to the itinerary
            itinerary.add(confirmedFlight);

            // Redirect to the itinerary page
            return "redirect:/add-to-itinerary";
        } else {
            // Handle case where flight ID is not found, e.g., redirect to flights page with
            // an error message
            return "redirect:/flights?error=FlightNotFound";
        }
    }

    @GetMapping("/add-to-itinerary")
    public String viewItinerary(Model model, HttpSession session) {
        List<Flight> itinerary = (List<Flight>) session.getAttribute("itinerary");
        if (itinerary == null) {
            itinerary = new ArrayList<>();
        }
        double totalPrice = itinerary.stream().mapToDouble(Flight::getPrice).sum();

        model.addAttribute("itinerary", itinerary);
        model.addAttribute("totalPrice", totalPrice);

        return "itinerary";
    }

    @PostMapping("/generate-tickets")
    public String generateTickets(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        List<Flight> itinerary = (List<Flight>) session.getAttribute("itinerary");

        // Check for a valid itinerary
        if (itinerary == null || itinerary.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No flights selected for booking.");
            return "redirect:/flights";
        }

        // Check for cyclic itinerary using the BookingManager
        if (bookingManager.isCyclicItinerary(itinerary)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cyclic itinerary detected. Unable to generate tickets.");
            return "redirect:/add-to-itinerary";
        }

        List<Ticket> tickets = new ArrayList<>();
        Random random = new Random();

        for (Flight flight : itinerary) {
            Ticket ticket = new Ticket();

            // Randomly generate a ticket number (assuming a format, adjust as needed)
            String ticketNumber = "TKT" + (100000 + random.nextInt(900000));
            ticket.setTicketNumber(ticketNumber);

            // Randomly generate a seat number (assuming format "A1", "B2", etc.)
            char seatRow = (char) ('A' + random.nextInt(6)); // For rows A-F
            int seatColumn = 1 + random.nextInt(10); // For columns 1-10
            String seatNumber = seatRow + String.valueOf(seatColumn);
            ticket.setSeatNumber(seatNumber);

            // Set other ticket properties
            ticket.setFlightDetails(flight.getDetails()); // Assuming a method to get flight details
            ticket.setPassengerName("John Doe"); // Example, replace with actual passenger name

            tickets.add(ticket);
        }

        // Add tickets to the model to display them on the success page
        model.addAttribute("tickets", tickets);

        // Redirect to the ticket-success page
        return "ticket-success";
    }

    @PostMapping("/remove-flight/{flightId}")
    public String removeFlightFromItinerary(@PathVariable("flightId") int flightId, HttpSession session) {
        List<Flight> itinerary = (List<Flight>) session.getAttribute("itinerary");
        if (itinerary != null) {
            itinerary.removeIf(flight -> flight.getFlightId() == flightId);
            session.setAttribute("itinerary", itinerary);
        }
        return "redirect:/add-to-itinerary";
    }
    
}
