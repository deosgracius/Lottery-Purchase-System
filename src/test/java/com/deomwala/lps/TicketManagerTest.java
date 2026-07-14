package com.deomwala.lps;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TicketManagerTest {

    @TempDir Path tmp;
    private TicketManager tm;

    @BeforeEach
    void setUp() {
        tm = new TicketManager(tmp.resolve("tickets.json").toString());
    }

    @Test
    void seedsFourDefaultActiveTickets() {
        assertEquals(4, tm.listTickets().size());
        assertEquals(4, tm.listActiveTickets().size());
    }

    @Test
    void toggleAvailabilityHidesTicketFromActiveList() {
        String id = tm.listTickets().get(0).id;
        tm.toggleTicketAvailability(id);
        assertFalse(tm.getTicket(id).active);
        assertEquals(3, tm.listActiveTickets().size());
    }

    @Test
    void removeTicketDecreasesCount() {
        String id = tm.listTickets().get(0).id;
        tm.removeTicket(id);
        assertEquals(3, tm.listTickets().size());
        assertNull(tm.getTicket(id));
    }

    @Test
    void getUnknownTicketReturnsNull() {
        assertNull(tm.getTicket("does-not-exist"));
    }
}
