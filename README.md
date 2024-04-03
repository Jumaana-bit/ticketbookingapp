# Flight Ticket Booking System - SOFE3980 Group 26 Project

This application is a flight ticket booking system designed for the SOFE3980 Group 26 Project. Built using Java, Spring Boot, and Thymeleaf, it offers a platform for users to search, book, and manage flight itineraries. The project focused on designing and implementing tests using JUnit and Mockito.

![image](https://github.com/Jumaana-bit/ticketbookingapp/assets/58871999/ad6762af-5916-490a-94ad-2f932003a0cb)

## Key Features

- **Flight Search**: Users can search for flights based on origin, destination, and date. The search functionality is flexible, allowing for broad searches or more specific queries.

- **Itinerary Management**: Users can create and manage their itineraries, adding flights and viewing a detailed summary of their upcoming trips.

- **Booking Confirmation**: Before finalizing a booking, users are presented with a confirmation page detailing the flight, allowing them to review their choice before adding it to their itinerary.

- **Cyclic Itinerary Check**: The system includes logic to prevent users from creating cyclic itineraries, enhancing the user experience by ensuring travel plans are logical and sequential. Example:
  ![image](https://github.com/Jumaana-bit/ticketbookingapp/assets/58871999/6e69c082-4616-4226-8c97-aca14481618d)

- **User Authentication**: The application includes a signup and login system, with pages for users to create an account and sign in to access their personal dashboard.

- **Responsive Design**: Utilizing CSS and Thymeleaf, the application boasts a modern and responsive design, ensuring a seamless user experience across different devices.

## Testing and Key Testing Features

This project emphasizes the importance of rigorous testing to ensure the reliability and functionality of the flight ticket booking system. Here's an overview of the key testing features implemented:

### Unit Testing
- **Unit tests** are extensively written for individual components such as the `BookingManager`, `FlightManager`, and `UserManager`. These tests validate the logic of data processing, ensuring each unit performs as expected in isolation.

### Integration Testing
- **Integration tests** focus on the interaction between components, such as the integration of `BookingController` with `BookingManager` and `FlightManager`. These tests ensure that components work together seamlessly to process user requests and manage data.

### Mock Testing
- Utilizing **Mockito**, we simulate the behavior of complex dependencies, allowing for focused testing of service layers without the need for actual implementations. This approach is particularly useful for testing the service layer's interaction with repositories.

### End-to-End Testing
- **End-to-end tests** simulate user interactions with the application, from searching for flights to booking and managing itineraries. These tests cover the full workflow of the application, ensuring that all components work together to provide a smooth user experience.

### Test Driven Development (TDD)
- The project adopts a **Test Driven Development** approach, where tests are written before the actual implementation. This methodology encourages robust design and ensures that the codebase remains clean and testable.

### Continuous Integration / Continuous Delivery
- A **Continuous Integration** pipeline is set up to automatically run tests on each commit, ensuring that new changes do not break existing functionalities. This practice helps in maintaining code quality throughout the development process. ***VIDEO DEMONSTRATION of our Jenkins CI/CD Pipeline:*** https://drive.google.com/file/d/1mV7QwNVO7BXdqZsM6qnvfrAVqCiIv49C/view?usp=sharing 

## Usage

This application is a Maven-based Spring Boot project, which can be run locally using the following steps:

### Prerequisites

Ensure you have the following installed:

- **Java JDK**: Version 8 or newer.
- **Maven**: Version 3.6.3 or newer.

Use the provided pom.xml for the dependencies required to run the application.

### Running the Application

1. **Clone the Repository**

    Start by cloning the project repository to your local machine using:

    ```bash
    git clone https://github.com/Jumaana-bit/ticketbookingapp.git
    ```

2. **Navigate to Project Directory**

    Change into the project directory:

    ```bash
    cd ticketbookingapp
    ```

3. **Build the Project**

    Use Maven to build the project:

    ```bash
    mvn clean install
    ```

    To skip the tests during the build, you can add the `-DskipTests` flag:

    ```bash
    mvn clean install -DskipTests
    ```

4. **Run the Application**

    Launch the application using Spring Boot Maven plugin:

    ```bash
    mvn spring-boot:run
    ```

5. **Access the App**

    With the application running, open your web browser and go to `http://localhost:8080`. You should now be able to interact with the application.
