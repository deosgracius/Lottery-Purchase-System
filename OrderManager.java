import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class OrderManager {
    public String orderFile;
    public List<Order> orders;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public OrderManager(String orderFile) {
        this.orderFile = orderFile;
        this.orders = load();
    }

    // ---------------------------------------------------------------
    // Persistence
    // ---------------------------------------------------------------

    private List<Order> load() {
        Path path = Paths.get(orderFile);
        if (!Files.exists(path)) return new ArrayList<>();
        try (Reader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<Order>>() {}.getType();
            List<Order> loaded = GSON.fromJson(r, listType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to load orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(Paths.get(orderFile), StandardCharsets.UTF_8)) {
            GSON.toJson(orders, w);
        } catch (IOException e) {
            System.err.println("Failed to save orders: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Order operations
    // ---------------------------------------------------------------

    public Order createOrder(String userEmail, List<PurchasedTicket> tickets, float total) {
        // Generate purchase date
        String date = java.time.LocalDate.now().toString();
        Order order = new Order(
                UUID.randomUUID().toString(),
                userEmail,
                tickets.size(),
                tickets,
                total,
                date
        );
        orders.add(order);
        save();
        return order;
    }

    public List<Order> getOrdersForUser(String email) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.userEmail.equalsIgnoreCase(email)) result.add(o);
        }
        return result;
    }

    public Order getOrder(String id) {
        return orders.stream()
                .filter(o -> o.id.equals(id))
                .findFirst().orElse(null);
    }

    // Update a purchased ticket's winning status after a draw
    public void updateTicketWinStatus(String orderId, String ticketId,
                                      boolean winning, List<Integer> winningNumbers,
                                      float prizeAmount) {
        Order order = getOrder(orderId);
        if (order == null) return;
        for (PurchasedTicket pt : order.tickets) {
            if (pt.id.equals(ticketId)) {
                pt.winning = winning;
                pt.resolved = true;
                pt.winningNumbers = winningNumbers;
                pt.prizeAmount = prizeAmount;
                break;
            }
        }
        save();
    }

    // Mark a ticket as claimed
    public void claimTicket(String orderId, String ticketId) {
        Order order = getOrder(orderId);
        if (order == null) return;
        for (PurchasedTicket pt : order.tickets) {
            if (pt.id.equals(ticketId)) {
                pt.claimed = true;
                break;
            }
        }
        save();
    }

    // Revenue stats per ticket name
    public Map<String, float[]> getRevenueStats() {
        // key = ticket name, value = [totalSold, totalRevenue]
        Map<String, float[]> stats = new LinkedHashMap<>();
        for (Order o : orders) {
            for (PurchasedTicket pt : o.tickets) {
                stats.computeIfAbsent(pt.name, k -> new float[]{0, 0});
                stats.get(pt.name)[0]++;
                stats.get(pt.name)[1] += pt.price;
            }
        }
        return stats;
    }
}