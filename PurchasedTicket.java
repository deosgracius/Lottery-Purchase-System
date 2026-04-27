import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PurchasedTicket extends Ticket {
    public List<Integer> numbers;        // User's chosen numbers
    public List<Integer> winningNumbers; // Drawn winning numbers (null = PENDING)
    public boolean winning;
    public boolean resolved;             // true = draw has happened
    public boolean claimed;
    public float prizeAmount;            // Prize won (0 if no win)
    public String ticketType;            // e.g. "Powerball"
    public String confirmationNumber;    // Unique confirmation number

    public PurchasedTicket() {
        this.numbers = new ArrayList<>();
        this.winningNumbers = null;
        this.winning = false;
        this.resolved = false;
        this.claimed = false;
        this.prizeAmount = 0f;
        this.confirmationNumber = java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @SuppressWarnings("unchecked")
    public static PurchasedTicket fromDict(Map<String, Object> data) {
        PurchasedTicket pt = new PurchasedTicket();
        pt.id = (String) data.get("id");
        pt.name = (String) data.get("name");
        pt.price = ((Number) data.get("price")).floatValue();
        pt.desc = (String) data.getOrDefault("desc", "");
        pt.imgSrc = (String) data.getOrDefault("imgSrc", "");
        pt.active = data.containsKey("active") ? (boolean) data.get("active") : true;
        pt.numbers = data.containsKey("numbers") ? (List<Integer>) data.get("numbers") : new ArrayList<>();
        pt.winningNumbers = data.containsKey("winningNumbers") ? (List<Integer>) data.get("winningNumbers") : null;
        pt.winning = data.containsKey("winning") && (boolean) data.get("winning");
        pt.resolved = data.containsKey("resolved") && (boolean) data.get("resolved");
        pt.claimed = data.containsKey("claimed") && (boolean) data.get("claimed");
        pt.prizeAmount = data.containsKey("prizeAmount") ? ((Number) data.get("prizeAmount")).floatValue() : 0f;
        pt.ticketType = (String) data.getOrDefault("ticketType", "");
        pt.confirmationNumber = (String) data.getOrDefault("confirmationNumber",
                java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        return pt;
    }
}