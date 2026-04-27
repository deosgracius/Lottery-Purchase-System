import java.util.Map;

public class Ticket {
    public String id;
    public String name;
    public float price;
    public String desc;
    public String imgSrc;
    public boolean active;

    public Ticket() {}

    public Ticket(String id, String name, float price, String desc, String imgSrc, boolean active) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.imgSrc = imgSrc;
        this.active = active;
    }

    public static Ticket fromDict(Map<String, Object> data) {
        Ticket t = new Ticket();
        t.id = (String) data.get("id");
        t.name = (String) data.get("name");
        t.price = ((Number) data.get("price")).floatValue();
        t.desc = (String) data.get("desc");
        t.imgSrc = (String) data.get("imgSrc");
        t.active = data.containsKey("active") ? (boolean) data.get("active") : true;
        return t;
    }

    @Override
    public String toString() {
        return "Ticket{id='" + id + "', name='" + name + "', price=" + price + ", active=" + active + "}";
    }
}