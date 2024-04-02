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

    public int getFlightId() {
        return flightId;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureLocation() {
        return origin;
    }

    public String getDestinationLocation() {
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

    public void setDepartureLocation(String departureLocation) {
        this.origin = departureLocation;
    }

    public void setDestinationLocation(String destinationLocation) {
        this.destination = destinationLocation;
    }

    public void setPrice(double price) {
        this.price = price;
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
