package sofe3980;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserManager {

    private Map<Integer, User> users; // Use HashMap to store users by their ID
    private int nextUserId;

    public UserManager() {
        this.users = new HashMap<>();
        this.nextUserId = 1; // Start with user ID 1 then increment from here
    }

    public User registerUser(String name, String email, String password, LocalDate dob, String passportNumber) {
        int userId = nextUserId++; // Auto increment ID value
        User newUser = new User(userId, name, email, password, dob, passportNumber);
        users.put(userId, newUser); // Store user in the HashMap
        return newUser;
    }

    public Optional<User> loginUser(String email, String password) {
        return users.values().stream()
                    .filter(user -> user.getEmail().equals(email) && user.getPassword().equals(password))
                    .findFirst();
    }

    public Optional<User> getUserById(int userId) {
        return Optional.ofNullable(users.get(userId)); // Retrieve the user by ID if it exists
    }
}
