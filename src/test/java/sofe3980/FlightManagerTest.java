package sofe3980;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FlightManagerTest {

    private FlightManager flightManager;

    @Before
    public void setUp() {
        flightManager = new FlightManager();

        // Dynamic flights for testing weekly flights
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        for (int i = 0; i < 7; i++) { // Add a flight for each day of the current week
            LocalDate flightDate = startOfWeek.plusDays(i);
            LocalDateTime departureTime = flightDate.atTime(10, 0); // 10:00 AM
            LocalDateTime arrivalTime = departureTime.plusHours(2); // 2 hours later
            flightManager.addFlight(new Flight(i + 1, departureTime, arrivalTime, "City" + (char) ('A' + i),
                    "City" + (char) ('B' + i), 100.00 + i * 10));
        }

        // Specific flights for testing direct flights
        // Assuming you want to test a direct flight from CityA to CityB on a specific
        // date
        LocalDate directFlightDate = today.plusDays(8); // Next week to avoid overlap with weekly flights
        flightManager.addFlight(new Flight(8, directFlightDate.atTime(11, 0), directFlightDate.atTime(13, 30), "CityA",
                "CityB", 250.00));

        // Specific flights for testing multi-stop flights (CityX -> CityY -> CityZ)
        // Leg 1: CityX to CityY
        LocalDate multiStopDate = today.plusDays(9); // Next week to avoid overlap
        flightManager.addFlight(
                new Flight(9, multiStopDate.atTime(7, 0), multiStopDate.atTime(9, 0), "CityX", "CityY", 140.00));
        // Leg 2: CityY to CityZ (with enough layover time)
        flightManager.addFlight(
                new Flight(10, multiStopDate.atTime(11, 0), multiStopDate.atTime(13, 0), "CityY", "CityZ", 160.00));
    }

    @Test
    public void testSearchDirectFlights() {
        // Test for correct number of results
        LocalDate directFlightDate = LocalDate.now().plusDays(8); // Match the date used in setUp()
        List<Flight> results = flightManager.searchDirectFlights("CityA", "CityB", directFlightDate);
        assertEquals("Should return the correct number of flights", 1, results.size());

        // Assuming we have at least one result from the above assertion
        if (!results.isEmpty()) {
            // Test for correct flight data
            Flight resultFlight = results.get(0);
            assertEquals("Departure location should match", "CityA", resultFlight.getOrigin());
            assertEquals("Destination location should match", "CityB", resultFlight.getDestination());
            // Add more detailed assertions as needed, like checking times, flight IDs, etc.
        }

        // Test for no results
        results = flightManager.searchDirectFlights("NonExistentCity", "AnotherNonExistentCity",
                LocalDate.of(2024, 4, 10));
        assertTrue("Should return an empty list when no flights match", results.isEmpty());

        // Test for edge cases (Example: Searching with a different date)
        results = flightManager.searchDirectFlights("CityA", "CityB", LocalDate.of(2024, 4, 11)); // No flight on this
                                                                                                  // date in the setup
        assertTrue("Should return an empty list for dates with no flights", results.isEmpty());

        // Additional test cases for other edge cases can be added here...
    }

    @Test
    public void testSearchMultiStopFlights() {
        // Adjust the date to match the multi-stop flights added in the setUp() method
        LocalDate multiStopDate = LocalDate.now().plusDays(9); // 9 days from today, matching the setup for "CityX" to
                                                               // "CityZ" via "CityY"

        // Search for multi-stop flights from "CityX" to "CityZ" on the specified date
        List<List<Flight>> results = flightManager.searchMultiStopFlights("CityX", "CityZ", multiStopDate);

        // Expect 1 multi-stop flight journey consisting of 2 flights (Flight 9 and
        // Flight 10)
        int expectedNumberOfMultiStopFlights = 1; // Adjusted based on the updated test data
        assertEquals("Should return the correct number of multi-stop flights", expectedNumberOfMultiStopFlights,
                results.size());

        if (!results.isEmpty()) {
            List<Flight> firstMultiStopFlight = results.get(0);

            // Verify the first leg is from "CityX" to "CityY"
            Flight firstLeg = firstMultiStopFlight.get(0);
            assertEquals("First leg's departure location should match", "CityX", firstLeg.getOrigin());
            assertEquals("First leg's destination location should match", "CityY", firstLeg.getDestination());

            // Verify the last leg is from "CityY" to "CityZ"
            Flight lastLeg = firstMultiStopFlight.get(firstMultiStopFlight.size() - 1);
            assertEquals("Last leg's departure location should match", "CityY", lastLeg.getOrigin());
            assertEquals("Last leg's destination location should match", "CityZ", lastLeg.getDestination());

            // Verify the correct sequencing of flights
            for (int i = 0; i < firstMultiStopFlight.size() - 1; i++) {
                Flight currentLeg = firstMultiStopFlight.get(i);
                Flight nextLeg = firstMultiStopFlight.get(i + 1);
                assertEquals("Destination of the current leg should match the departure of the next leg",
                        currentLeg.getDestination(), nextLeg.getOrigin());
            }
        }

        // Test for no results with non-existent cities
        results = flightManager.searchMultiStopFlights("NonExistentCity", "AnotherNonExistentCity", multiStopDate);
        assertTrue("Should return an empty list when no multi-stop flights match", results.isEmpty());

        // Test for edge cases with a different date where no multi-stop flights are set
        // up
        LocalDate differentDate = LocalDate.now().plusDays(10); // A date with no multi-stop flights set up in the test
                                                                // data
        results = flightManager.searchMultiStopFlights("CityX", "CityZ", differentDate);
        assertTrue("Should return an empty list for dates with no multi-stop flights", results.isEmpty());
    }

    @Test
    public void testGetWeeklyFlights() {
        // Assuming the setup includes flights scheduled throughout the week
        List<Flight> weeklyFlights = flightManager.getWeeklyFlights();

        // Check that the list is not null
        assertNotNull("Weekly flights list should not be null", weeklyFlights);

        // Test for correct number of flights within the week
        int expectedNumberOfWeeklyFlights = 54;
        assertEquals("Should return the correct number of weekly flights", expectedNumberOfWeeklyFlights,
                weeklyFlights.size());

        // Test that each flight is within the current week
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate endOfWeek = LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        for (Flight flight : weeklyFlights) {
            LocalDate flightDate = flight.getDepartureTime().toLocalDate();
            assertTrue("Flight date should be within the current week",
                    (flightDate.isEqual(startOfWeek) || flightDate.isAfter(startOfWeek)) &&
                            (flightDate.isEqual(endOfWeek) || flightDate.isBefore(endOfWeek)));
        }

        // Test for edge cases, such as flights at the very start and end of the week
        boolean hasFlightAtStartOfWeek = weeklyFlights.stream()
                .anyMatch(flight -> flight.getDepartureTime().toLocalDate().isEqual(startOfWeek));
        boolean hasFlightAtEndOfWeek = weeklyFlights.stream()
                .anyMatch(flight -> flight.getDepartureTime().toLocalDate().isEqual(endOfWeek));
        assertTrue("There should be at least one flight at the start of the week", hasFlightAtStartOfWeek);
        assertTrue("There should be at least one flight at the end of the week", hasFlightAtEndOfWeek);
    }

    @Test
    public void testGetFlightById() {
        // Assuming there's a flight with ID 1
        Optional<Flight> optionalFlight = flightManager.getFlightById(1);
        assertTrue("Flight should be found by ID", optionalFlight.isPresent());
        Flight flight = optionalFlight.get();
        assertEquals("Flight ID should match", 1, flight.getFlightId());

        // Assuming there is no flight with ID -1
        Optional<Flight> optionalFlightNull = flightManager.getFlightById(-1);
        assertFalse("No flight should be found for an invalid ID", optionalFlightNull.isPresent());
    }

    @Test
    public void testCalculateTotalFlightTime() {
        // Choose a subset of flights for this test. For simplicity, let's use the
        // weekly flights, which is 7
        LocalDate startOfWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        List<Flight> weeklyFlights = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate flightDate = startOfWeek.plusDays(i);
            weeklyFlights.addAll(flightManager.searchDirectFlights("City" + (char) ('A' + i), "City" + (char) ('B' + i),
                    flightDate));
        }

        // Calculate the total flight time for these flights
        String totalFlightTime = flightManager.calculateTotalFlightTime(weeklyFlights);

        // Expected total flight time is 14 hours (2 hours per flight for 7 flights)
        String expectedTotalFlightTime = "14:00";

        assertEquals("Total flight time should be correctly calculated", expectedTotalFlightTime, totalFlightTime);
    }

}
