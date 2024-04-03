package sofe3980;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class FlightManager {

    private List<Flight> flights;
    private int flightIdCounter = 1;
    String[] departureLocations = { "New York", "Los Angeles", "Chicago", "Miami", "Dallas" };
    String[] destinationLocations = { "Los Angeles", "Chicago", "Miami", "Dallas", "New York" };

    public FlightManager() {
        flights = new ArrayList<>();
        initializeFlights();
    }

    private void initializeFlights() {
        // Set startDateTime to a fixed date for testing
        LocalDateTime startDateTime = LocalDateTime.of(2024, 4, 1, 0, 0);

        Flight departureFlight = generateFlight(3, "New York", "Los Angeles", startDateTime); // Apr 4
        Flight returnFlight = generateFlight(4, "Los Angeles", "New York", startDateTime); // Apr 5

        departureFlight.setCorrespondingFlightId(returnFlight.getFlightId());
        returnFlight.setCorrespondingFlightId(departureFlight.getFlightId());

        addFlight(departureFlight);
        addFlight(returnFlight);
    }

    private Flight generateFlight(int dayOffset, String departure, String destination, LocalDateTime start) {
        LocalDateTime departureTime = start.plusDays(dayOffset);
        LocalDateTime arrivalTime = departureTime.plusHours(5); // Assuming a 5-hour flight for simplicity
        int flightId = flightIdCounter++; // Use the counter for flight ID and increment it

        return new Flight(flightId, departureTime, arrivalTime, departure, destination, 300.00);
    }

    public List<Flight> searchFlights(String from, String to, LocalDate departureDate) {
        List<Flight> resultFlights = new ArrayList<>();

        // Filter for departure flights
        List<Flight> departureFlights = flights.stream()
                .filter(flight -> flight.getOrigin().equalsIgnoreCase(from) &&
                        flight.getDestination().equalsIgnoreCase(to) &&
                        (departureDate == null || flight.getDepartureTime().toLocalDate().isEqual(departureDate)))
                .collect(Collectors.toList());

        for (Flight depFlight : departureFlights) {
            resultFlights.add(depFlight); // Add the departure flight

            // Find and add the corresponding return flight, if any
            flights.stream()
                    .filter(flight -> flight.getFlightId() == depFlight.getCorrespondingFlightId()) // Corrected this
                                                                                                    // line
                    .findFirst()
                    .ifPresent(resultFlights::add);
        }

        return resultFlights;
    }

    public List<Flight> searchFlights(String from, String to, LocalDate departureDate, LocalDate returnDate) {
        System.out.println("Starting flight search...");
        System.out.println("From: " + from + ", To: " + to + ", Departure Date: " + departureDate + ", Return Date: "
                + returnDate);

        List<Flight> resultFlights = new ArrayList<>();

        // Filter for departure flights
        List<Flight> departureFlights = flights.stream()
                .filter(flight -> {
                    boolean matches = flight.getOrigin().equalsIgnoreCase(from) &&
                            flight.getDestination().equalsIgnoreCase(to) &&
                            (departureDate == null || flight.getDepartureTime().toLocalDate().isEqual(departureDate));
                    if (matches) {
                        System.out.println("Matching departure flight found: " + flight);
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        System.out.println("Departure flights found: " + departureFlights.size());

        // If a return date is provided, find the corresponding return flights
        if (returnDate != null) {
            List<Flight> returnFlights = flights.stream()
                    .filter(flight -> {
                        boolean matches = flight.getOrigin().equalsIgnoreCase(to) &&
                                flight.getDestination().equalsIgnoreCase(from) &&
                                flight.getDepartureTime().toLocalDate().isEqual(returnDate);
                        if (matches) {
                            System.out.println("Matching return flight found: " + flight);
                        }
                        return matches;
                    })
                    .collect(Collectors.toList());

            System.out.println("Return flights found: " + returnFlights.size());

            // Combine departure and return flights
            resultFlights.addAll(departureFlights);
            resultFlights.addAll(returnFlights);
        } else {
            // If no return date is provided, just add the departure flights
            resultFlights.addAll(departureFlights);
        }

        System.out.println("Total flights found: " + resultFlights.size());

        return resultFlights;
    }

    // for adding flights to the list of all flights (used for adding dummy flights
    // for testing)
    public void addFlight(Flight flight) {
        this.flights.add(flight);
    }

    /**
     * Searches for direct flights based on the provided criteria.
     * 
     * @param from The departure location.
     * @param to   The destination location.
     * @param date The date of the flight.
     * @return A list of flights matching the criteria.
     */
    public List<Flight> searchDirectFlights(String from, String to, LocalDate date) {
        List<Flight> matchingFlights = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getOrigin().equals(from) &&
                    flight.getDestination().equals(to) &&
                    flight.getDepartureTime().toLocalDate().equals(date)) {
                matchingFlights.add(flight);
            }
        }
        return matchingFlights;
    }

    /**
     * Searches for multi-stop flights based on the provided paraneters.
     * 
     * @param from The departure location.
     * @param to   The destination location.
     * @param date The date of the flight.
     * @return returns a list of lists, where each inner list represents a sequence
     *         of flights forming a multi-stop journey.
     */
    public List<List<Flight>> searchMultiStopFlights(String from, String to, LocalDate date) {
        List<List<Flight>> multiStopFlights = new ArrayList<>();

        // First, find all flights departing from the origin on the specified date
        List<Flight> departingFlights = flights.stream()
                .filter(f -> f.getOrigin().equals(from) && f.getDepartureTime().toLocalDate().equals(date))
                .collect(Collectors.toList());

        // Then, for each departing flight, find connecting flights from the arrival
        // location to the final destination
        for (Flight firstLeg : departingFlights) {
            List<Flight> connectingFlights = flights.stream()
                    .filter(f -> f.getOrigin().equals(firstLeg.getDestination()) &&
                            f.getDestination().equals(to) &&
                            f.getDepartureTime().isAfter(firstLeg.getArrivalTime()))
                    .collect(Collectors.toList());

            // For each connecting flight found, add a new list containing the first leg and
            // the connecting flight to the results
            for (Flight secondLeg : connectingFlights) {
                List<Flight> multiStopFlight = new ArrayList<>();
                multiStopFlight.add(firstLeg);
                multiStopFlight.add(secondLeg);
                multiStopFlights.add(multiStopFlight);
            }
        }

        return multiStopFlights;
    }

    /**
     * Retrieves the list of weekly flights.
     * 
     * @return A list of weekly flights.
     */
    public List<Flight> getWeeklyFlights() {
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        return flights.stream()
                .filter(flight -> {
                    LocalDate flightDate = flight.getDepartureTime().toLocalDate();
                    return (!flightDate.isBefore(startOfWeek)) && (!flightDate.isAfter(endOfWeek));
                })
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a flight by its ID.
     * 
     * @param flightId The ID of the flight to retrieve.
     * @return The Flight object if found, or null otherwise.
     */
    public Optional<Flight> getFlightById(int flightId) {
        return flights.stream()
                .filter(flight -> flight.getFlightId() == flightId)
                .findFirst();
    }

    /**
     * Calculates the total flight time for a given list of flights.
     * 
     * @param flights List of flights for which to calculate the total time.
     * @return Total flight time as a string formatted as "H:MM".
     */
    public String calculateTotalFlightTime(List<Flight> flights) {
        int totalMinutes = 0;

        // using the calculateDuration() Flight method, we have to parse the String
        // result it provides in H:MM format
        for (Flight flight : flights) {
            String duration = flight.calculateDuration();
            String[] parts = duration.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            totalMinutes += (hours * 60) + minutes;
        }

        int totalHours = totalMinutes / 60;
        int remainingMinutes = totalMinutes % 60;

        return String.format("%d:%02d", totalHours, remainingMinutes);
    }
}
