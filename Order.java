import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order {
    public String id;
    public String userEmail;
    public int quantity;
    public List<PurchasedTicket> tickets;
    public float total;
    public String purchaseDate;   // e.g. "2026-04-27"

    public Order() {
        this.tickets = new ArrayList<>();
    }

    public Order(String id, String userEmail, int quantity,
                 List<PurchasedTicket> tickets, float total, String purchaseDate) {
        this.id = id;
        this.userEmail = userEmail;
        this.quantity = quantity;
        this.tickets = tickets;
        this.total = total;
        this.purchaseDate = purchaseDate;
    }

    @SuppressWarnings("unchecked")
    public static Order fromDict(Map<String, Object> data) {
        Order o = new Order();
        o.id = (String) data.get("id");
        o.userEmail = (String) data.get("userEmail");
        o.quantity = ((Number) data.get("quantity")).intValue();
        o.total = data.containsKey("total") ? ((Number) data.get("total")).floatValue() : 0.0f;
        o.purchaseDate = (String) data.getOrDefault("purchaseDate", "");
        List<Map<String, Object>> rawTickets =
                (List<Map<String, Object>>) data.getOrDefault("tickets", new ArrayList<>());
        for (Map<String, Object> t : rawTickets) {
            o.tickets.add(PurchasedTicket.fromDict(t));
        }
        return o;
    }
}