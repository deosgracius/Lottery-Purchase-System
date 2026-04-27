import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class User {
    public String name;
    public String email;
    public String phone;
    public String address;
    public String birthday;    // "YYYY-MM-DD"
    public String sex;         // "Male" / "Female" / "Other"
    public String last4SSN;    // last 4 digits of SSN
    public String last4ID;     // last 4 digits of state ID
    public UserType role;
    public String salt;
    public String pwHash;
    public List<Order> orders;

    public User() { this.orders = new ArrayList<>(); }

    public User(String name, String email, String phone, String birthday,
                UserType role, String salt, String pwHash) {
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.birthday = birthday;
        this.role     = role;
        this.salt     = salt;
        this.pwHash   = pwHash;
        this.orders   = new ArrayList<>();
    }

    /** Returns true if the user has filled in all required profile fields */
    public boolean isProfileComplete() {
        return address  != null && !address.isBlank()  &&
                sex      != null && !sex.isBlank()      &&
                last4SSN != null && last4SSN.length() == 4 &&
                last4ID  != null && last4ID.length()  == 4;
    }

    @SuppressWarnings("unchecked")
    public static User fromDict(Map<String, Object> data) {
        User u = new User();
        u.name     = (String) data.get("name");
        u.email    = (String) data.get("email");
        u.phone    = (String) data.getOrDefault("phone", "");
        u.address  = (String) data.getOrDefault("address", "");
        u.birthday = (String) data.getOrDefault("birthday", "");
        u.sex      = (String) data.getOrDefault("sex", "");
        u.last4SSN = (String) data.getOrDefault("last4SSN", "");
        u.last4ID  = (String) data.getOrDefault("last4ID", "");
        u.role     = UserType.valueOf(((String) data.get("role")).toUpperCase());
        u.salt     = (String) data.get("salt");
        u.pwHash   = (String) data.get("pwHash");
        List<Map<String, Object>> rawOrders =
                (List<Map<String, Object>>) data.getOrDefault("orders", new ArrayList<>());
        for (Map<String, Object> o : rawOrders) u.orders.add(Order.fromDict(o));
        return u;
    }

    public Map<String, Object> asDictFactory() {
        Map<String, Object> map = new HashMap<>();
        map.put("name",    name);
        map.put("email",   email);
        map.put("phone",   phone);
        map.put("address", address);
        map.put("birthday", birthday);
        map.put("sex",     sex);
        map.put("last4SSN", last4SSN);
        map.put("last4ID",  last4ID);
        map.put("role",    role.name().toLowerCase());
        map.put("salt",    salt);
        map.put("pwHash",  pwHash);
        return map;
    }
}