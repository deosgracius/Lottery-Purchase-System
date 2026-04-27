import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class TicketManager {
    public String ticketFile;
    public List<Ticket> tickets;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public TicketManager(String ticketFile) {
        this.ticketFile = ticketFile;
        this.tickets = load();
        if (tickets.isEmpty()) initDefaultTickets();
    }

    // ---------------------------------------------------------------
    // Persistence
    // ---------------------------------------------------------------

    private List<Ticket> load() {
        Path path = Paths.get(ticketFile);
        if (!Files.exists(path)) return new ArrayList<>();
        try (Reader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<List<Ticket>>() {}.getType();
            List<Ticket> loaded = GSON.fromJson(r, listType);
            return loaded != null ? loaded : new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Failed to load tickets: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public void save() {
        try (Writer w = Files.newBufferedWriter(Paths.get(ticketFile), StandardCharsets.UTF_8)) {
            GSON.toJson(tickets, w);
        } catch (IOException e) {
            System.err.println("Failed to save tickets: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Default tickets (the 4 required by the SRS)
    // ---------------------------------------------------------------

    private void initDefaultTickets() {
        tickets.add(new Ticket(
                UUID.randomUUID().toString(),
                "Powerball",
                2.00f,
                "Pick 5 numbers from 1-50. Drawings every Saturday. Jackpot starts at $20 million!",
                "powerball.png",
                true
        ));
        tickets.add(new Ticket(
                UUID.randomUUID().toString(),
                "Mega Millions",
                2.00f,
                "Pick 5 numbers from 1-50. Drawings every Saturday. Starting jackpot of $40 million!",
                "megamillions.png",
                true
        ));
        tickets.add(new Ticket(
                UUID.randomUUID().toString(),
                "Lotto Texas",
                1.00f,
                "Pick 5 numbers from 1-50. Texas's own lottery game with weekly Saturday draws.",
                "lottotexas.png",
                true
        ));
        tickets.add(new Ticket(
                UUID.randomUUID().toString(),
                "Texas Two Step",
                1.50f,
                "Pick 5 numbers from 1-50. Two chances to win every Saturday draw!",
                "texastwostep.png",
                true
        ));
        save();
    }

    // ---------------------------------------------------------------
    // CRUD operations
    // ---------------------------------------------------------------

    public List<Ticket> listTickets() {
        return tickets;
    }

    public List<Ticket> listActiveTickets() {
        List<Ticket> active = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.active) active.add(t);
        }
        return active;
    }

    public Ticket getTicket(String id) {
        return tickets.stream()
                .filter(t -> t.id.equals(id))
                .findFirst().orElse(null);
    }

    public Ticket addTicket(Map<String, Object> data) {
        Ticket t = Ticket.fromDict(data);
        tickets.add(t);
        save();
        return t;
    }

    public Ticket removeTicket(String id) {
        Ticket t = getTicket(id);
        if (t != null) { tickets.remove(t); save(); }
        return t;
    }

    public Ticket toggleTicketAvailability(String id) {
        Ticket t = getTicket(id);
        if (t != null) { t.active = !t.active; save(); }
        return t;
    }

    public Ticket updateTicket(String id, String name, float price, String desc, String imgSrc) {
        Ticket t = getTicket(id);
        if (t != null) {
            t.name = name;
            t.price = price;
            t.desc = desc;
            t.imgSrc = imgSrc;
            save();
        }
        return t;
    }
}