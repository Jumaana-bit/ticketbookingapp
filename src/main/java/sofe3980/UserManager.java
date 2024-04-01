package sofe3980;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserManager {

    private List<User> users;
    private int nextUserId;

    public UserManager() {
        this.users = new ArrayList<>();
        this.nextUserId = 1; // Start with user ID 1 then increment from here
    }

    public User registerUser(String name, String email, String password, LocalDate dob, String passportNumber) {
        int userId = nextUserId++; // auto increment ID value
        User newUser = new User(userId, name, email, password, dob, passportNumber);
        users.add(newUser); // store in a list for now, this would be a database if we were using persistent
                            // storage in this app
        return newUser;
    }

    public Optional<User> loginUser(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
