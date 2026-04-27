import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class UserManager {
    public String userFile;
    public List<User> users;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, String> resetTokens = new HashMap<>();

    public UserManager(String userFile) {
        this.userFile = userFile;
        this.users = load();
    }

    private List<User> load() {
        Path path = Paths.get(userFile);
        if (!Files.exists(path)) return new ArrayList<>();
        try (Reader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<User>>(){}.getType();
            List<User> loaded = GSON.fromJson(r, listType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (IOException e) { return new ArrayList<>(); }
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(Paths.get(userFile), StandardCharsets.UTF_8)) {
            GSON.toJson(users, w);
        } catch (IOException e) { System.err.println("Failed to save users: " + e.getMessage()); }
    }

    private Map<String, String> hashPassword(String password, String salt) {
        try {
            if (salt == null) salt = UUID.randomUUID().toString().replace("-", "");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest((salt + password).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return Map.of("salt", salt, "pwHash", hex.toString());
        } catch (Exception e) { throw new RuntimeException("Hashing failed", e); }
    }

    /** Returns null on success, or an error message string on failure */
    public String tryCreateAccount(String name, String email, String phone,
                                   String birthday, String password) {
        if (name == null || name.isBlank() ||
                email == null || email.isBlank() ||
                password == null || password.isBlank() ||
                birthday == null || birthday.isBlank()) {
            return "Please fill in all required fields.";
        }
        // Age check — Texas requires 18+
        try {
            LocalDate dob = LocalDate.parse(birthday);
            int age = Period.between(dob, LocalDate.now()).getYears();
            if (age < 18) {
                return "You must be at least 18 years old to play the Texas Lottery.";
            }
        } catch (Exception e) {
            return "Invalid date of birth format.";
        }
        if (users.stream().anyMatch(u -> u.email.equalsIgnoreCase(email))) {
            return "An account with that email already exists.";
        }
        Map<String, String> hashed = hashPassword(password, null);
        User user = new User(name, email, phone, birthday,
                UserType.USER, hashed.get("salt"), hashed.get("pwHash"));
        users.add(user);
        save();
        return null; // success
    }

    public UserType authenticate(String email, String password) {
        Optional<User> match = users.stream()
                .filter(u -> u.email.equalsIgnoreCase(email)).findFirst();
        if (match.isEmpty()) return null;
        User user = match.get();
        Map<String, String> hashed = hashPassword(password, user.salt);
        return hashed.get("pwHash").equals(user.pwHash) ? user.role : null;
    }

    public User getUser(String email) {
        return users.stream().filter(u -> u.email.equalsIgnoreCase(email)).findFirst().orElse(null);
    }

    public void updateProfile(String email, String birthday, String address, String sex,
                              String last4SSN, String last4ID) {
        User u = getUser(email);
        if (u != null) {
            if (birthday != null && !birthday.isBlank()) u.birthday = birthday;
            u.address  = address;
            u.sex      = sex;
            u.last4SSN = last4SSN;
            u.last4ID  = last4ID;
            save();
        }
    }

    public void addOrderToUser(String email, Order order) {
        users.stream().filter(u -> u.email.equalsIgnoreCase(email)).findFirst()
                .ifPresent(u -> { u.orders.add(order); save(); });
    }

    public String generateResetToken(String email) {
        if (users.stream().noneMatch(u -> u.email.equalsIgnoreCase(email))) return null;
        String token = UUID.randomUUID().toString().replace("-", "");
        resetTokens.put(token, email);
        return token;
    }

    public boolean resetPassword(String token, String newPassword) {
        String email = resetTokens.get(token);
        if (email == null) return false;
        Optional<User> match = users.stream().filter(u -> u.email.equalsIgnoreCase(email)).findFirst();
        if (match.isEmpty()) return false;
        User user = match.get();
        Map<String, String> hashed = hashPassword(newPassword, null);
        user.salt   = hashed.get("salt");
        user.pwHash = hashed.get("pwHash");
        save();
        resetTokens.remove(token);
        return true;
    }

    public boolean isValidResetToken(String token) { return resetTokens.containsKey(token); }
}