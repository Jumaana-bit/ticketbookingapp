package sofe3980;

import java.time.LocalDateTime;
import java.time.Duration;

public class Flight {

    private int flightId;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private String origin;
    private String destination;
    private double price;

    // New field to hold the ID of the corresponding flight (e.g., return flight for
    // a round trip)
    private Integer correspondingFlightId; // Use Integer to allow for null values

    // Constructor
    public Flight(int flightId, LocalDateTime departureTime, LocalDateTime arrivalTime,
            String departureLocation, String destinationLocation, double price) {
        this.flightId = flightId;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.origin = departureLocation;
        this.destination = destinationLocation;
        this.price = price;
    }

    /**
     * Calculates and returns the duration of the flight in the format "H:MM".
     * 
     * @return The duration of the flight as a string formatted as "H:MM".
     */
    public String calculateDuration() {
        long minutes = Duration.between(departureTime, arrivalTime).toMinutes();
        long hours = minutes / 60;
        long remainingMinutes = minutes % 60;

        return String.format("%d:%02d", hours, remainingMinutes);
    }

    // Getters

    // Getter for the corresponding flight ID
    public Integer getCorrespondingFlightId() {
        return correspondingFlightId;
    }

    public int getFlightId() {
        return flightId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getPrice() {
        return price;
    }

    // Setters

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setOrigin(String departureLocation) {
        this.origin = departureLocation;
    }

    public void setDestination(String destinationLocation) {
        this.destination = destinationLocation;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Setter for the corresponding flight ID
    public void setCorrespondingFlightId(Integer correspondingFlightId) {
        this.correspondingFlightId = correspondingFlightId;
    }

    /**
     * Returns a detailed description of the flight.
     * 
     * @return A string containing the flight details.
     */
    public String getDetails() {
        return "Flight ID: " + flightId
                + ", Departure: " + departureTime
                + ", Arrival: " + arrivalTime
                + ", From: " + origin
                + ", To: " + destination
                + ", Price: $" + price;
    }
}
