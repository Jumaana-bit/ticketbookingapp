package sofe3980;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// this class will be used to serve the web page UI
@Controller
public class BookingController {

    private final BookingManager bookingManager;
    private final FlightManager flightManager;
    private final UserManager userManager;

    private Random random = new Random();

    private String generateTicketNumber() {
        // Generates a realistic alphanumeric ticket number
        return "TKT" + (1000000 + random.nextInt(9000000));
    }

    private String generateSeatNumber() {
        // Generates a realistic seat number, assuming rows 1-30 and seats A-F
        int row = 1 + random.nextInt(30);
        char seat = (char) ('A' + random.nextInt(6));
        return row + "" + seat;
    }

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
            // Authentication successful, redirect to search flights page
            return "redirect:/search";
        } else {
            // Authentication failed, redirect back to login page with error message
            redirectAttributes.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
    }

    @PostMapping("/search")
    public String handleFlightSearch(@RequestParam("searchFrom") String searchFrom,
            @RequestParam("searchTo") String searchTo,
            @RequestParam("tripType") String tripType,
            @RequestParam("departureDate") String departureDate,
            @RequestParam(value = "returnDate", required = false) String returnDate,
            HttpSession session, RedirectAttributes redirectAttributes) {
        session.setAttribute("searchFrom", searchFrom);
        session.setAttribute("searchTo", searchTo);
        session.setAttribute("tripType", tripType);
        session.setAttribute("departureDate", departureDate);
        if (tripType.equals("Round Trip")) {
            session.setAttribute("returnDate", returnDate);
        }

        return "redirect:/flights";
    }

    @GetMapping("/flights")
    public String viewFlights(Model model, HttpSession session) {
        String from = (String) session.getAttribute("searchFrom");
        String to = (String) session.getAttribute("searchTo");
        String tripType = (String) session.getAttribute("tripType");
        String departureDateString = (String) session.getAttribute("departureDate");
        String returnDateString = (String) session.getAttribute("returnDate");

        System.out.println("Flight search initiated");
        System.out.println("From: " + from);
        System.out.println("To: " + to);
        System.out.println("Trip Type: " + tripType);
        System.out.println("Departure Date: " + departureDateString);
        System.out.println("Return Date: " + returnDateString);

        try {
            final LocalDate departureDate;
            final LocalDate returnDate;

            if (departureDateString != null && !departureDateString.isEmpty()) {
                departureDate = LocalDate.parse(departureDateString, DateTimeFormatter.ISO_DATE);
                System.out.println("Parsed Departure Date: " + departureDate);
            } else {
                departureDate = null;
            }

            if ("Round Trip".equals(tripType) && returnDateString != null && !returnDateString.isEmpty()) {
                returnDate = LocalDate.parse(returnDateString, DateTimeFormatter.ISO_DATE);
                System.out.println("Parsed Return Date: " + returnDate);
            } else {
                returnDate = null;
            }

            List<Flight> flights = flightManager.searchFlights(from, to, departureDate, returnDate);
            System.out.println("Number of flights found: " + flights.size());

            List<Flight> departureFlights = flights.stream()
                    .filter(flight -> departureDate != null
                            && flight.getDepartureTime().toLocalDate().equals(departureDate))
                    .collect(Collectors.toList());

            List<Flight> returnFlights = flights.stream()
                    .filter(flight -> returnDate != null && flight.getDepartureTime().toLocalDate().equals(returnDate))
                    .collect(Collectors.toList());

            model.addAttribute("departureFlights", departureFlights);
            model.addAttribute("returnFlights", returnFlights);
            model.addAttribute("tripType", tripType);

        } catch (DateTimeParseException e) {
            System.out.println("Date parsing error: " + e.getMessage());
            model.addAttribute("dateError", "Invalid date format. Please use YYYY-MM-DD.");
        }

        return "flights";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "search";
    }

    @GetMapping("/itinerary")
    public String showItinerary(Model model, HttpSession session) {
        List<Flight> itinerary = (List<Flight>) session.getAttribute("itinerary");
        if (itinerary == null || itinerary.isEmpty()) {
            System.out.println("Itinerary is empty or null.");
            model.addAttribute("errorMessage", "Your itinerary is currently empty.");
        } else {
            System.out.println("Itinerary size: " + itinerary.size() + " | Adding to model.");
            model.addAttribute("itinerary", itinerary);
        }
        return "itinerary";
    }

    @PostMapping("/book-and-generate-tickets")
    public String bookAndGenerateTickets(@RequestParam("depId") int depId,
            @RequestParam(value = "retId", required = false) Integer retId, Model model) {
        List<Ticket> tickets = new ArrayList<>();

        // Process departure flight
        Optional<Flight> depFlightOpt = flightManager.getFlightById(depId);
        depFlightOpt.ifPresent(flight -> {
            Ticket ticket = new Ticket();
            ticket.setFlight(flight);
            ticket.setTicketNumber(generateTicketNumber());
            ticket.setSeatNumber(generateSeatNumber());
            tickets.add(ticket);
        });

        // Process return flight only if retId is provided
        if (retId != null && retId > 0) { // Check if retId is provided and valid
            Optional<Flight> retFlightOpt = flightManager.getFlightById(retId);
            retFlightOpt.ifPresent(flight -> {
                Ticket ticket = new Ticket();
                ticket.setFlight(flight);
                ticket.setTicketNumber(generateTicketNumber());
                ticket.setSeatNumber(generateSeatNumber());
                tickets.add(ticket);
            });
        }

        model.addAttribute("tickets", tickets);
        return "ticket-success";
    }

}
