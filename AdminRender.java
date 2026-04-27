import java.util.List;
import java.util.Map;

public class AdminRender {

    private static final String STYLE = """
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=DM+Sans:wght@300;400;500&display=swap');
            *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
            :root { --bg:#0a0a0f;--card:#13131a;--border:#2a2a3a;--gold:#c9a84c;--gold-light:#e8c97a;--text:#f0ede6;--muted:#7a7a8a;--error:#e05252;--success:#52c08a; }
            body { background:var(--bg);font-family:'DM Sans',sans-serif;color:var(--text);min-height:100vh; }
            nav { background:var(--card);border-bottom:1px solid var(--border);padding:16px 40px;display:flex;align-items:center;justify-content:space-between;position:sticky;top:0;z-index:100; }
            .nav-brand { font-family:'Playfair Display',serif;font-size:1.3rem;background:linear-gradient(135deg,var(--gold),var(--gold-light));-webkit-background-clip:text;-webkit-text-fill-color:transparent; }
            .nav-links { display:flex;gap:28px;align-items:center; }
            .nav-links a { color:var(--muted);text-decoration:none;font-size:0.9rem;transition:color 0.2s; }
            .nav-links a:hover { color:var(--text); }
            .nav-logout { color:var(--error)!important; }
            .admin-tag { background:rgba(201,168,76,0.1);border:1px solid var(--gold);color:var(--gold);font-size:0.7rem;padding:2px 8px;border-radius:4px;letter-spacing:2px;text-transform:uppercase;margin-left:10px;vertical-align:middle; }
            .page { max-width:1100px;margin:0 auto;padding:48px 24px; }
            h1 { font-family:'Playfair Display',serif;font-size:2rem;margin-bottom:8px; }
            .subtitle { color:var(--muted);margin-bottom:40px; }
            .card { background:var(--card);border:1px solid var(--border);border-radius:16px;padding:28px; }
            .stat-grid { display:grid;grid-template-columns:repeat(auto-fill,minmax(200px,1fr));gap:20px;margin-bottom:40px; }
            .stat { background:var(--card);border:1px solid var(--border);border-radius:12px;padding:24px; }
            .stat .val { font-family:'Playfair Display',serif;font-size:2rem;color:var(--gold); }
            .stat .label { color:var(--muted);font-size:0.8rem;text-transform:uppercase;letter-spacing:1px;margin-top:4px; }
            .badge { display:inline-block;padding:4px 12px;border-radius:20px;font-size:0.75rem;font-weight:600;letter-spacing:1px;text-transform:uppercase; }
            .badge-green { background:rgba(82,192,138,0.15);color:var(--success);border:1px solid rgba(82,192,138,0.3); }
            .badge-red { background:rgba(224,82,82,0.15);color:var(--error);border:1px solid rgba(224,82,82,0.3); }
            .btn { display:inline-block;padding:9px 18px;background:linear-gradient(135deg,var(--gold),var(--gold-light));color:#0a0a0f;font-weight:700;border:none;border-radius:8px;cursor:pointer;text-decoration:none;font-size:0.85rem;transition:opacity 0.2s; }
            .btn:hover { opacity:0.9; }
            .btn-red { background:var(--error);color:white; }
            .btn-outline { background:transparent;border:1px solid var(--gold);color:var(--gold); }
            table { width:100%;border-collapse:collapse; }
            th { text-align:left;padding:12px 16px;color:var(--muted);font-size:0.8rem;text-transform:uppercase;letter-spacing:1px;border-bottom:1px solid var(--border); }
            td { padding:14px 16px;border-bottom:1px solid rgba(42,42,58,0.5);font-size:0.9rem; }
            .form-group { margin-bottom:16px; }
            .form-group label { display:block;font-size:0.8rem;letter-spacing:1px;text-transform:uppercase;color:var(--muted);margin-bottom:8px; }
            .form-group input { width:100%;background:rgba(255,255,255,0.04);border:1px solid var(--border);border-radius:10px;padding:12px 14px;color:var(--text);font-family:'DM Sans',sans-serif;font-size:0.9rem;outline:none;transition:border-color 0.2s; }
            .form-group input:focus { border-color:var(--gold); }
        </style>
    """;

    private static final String NAV = """
        <nav>
            <div class="nav-brand">🎰 LPS <span class="admin-tag">Admin</span></div>
            <div class="nav-links">
                <a href="/admin">Dashboard</a>
                <a href="/admin/tickets">Manage Tickets</a>
                <a href="/logout" class="nav-logout">Logout</a>
            </div>
        </nav>
    """;

    private String page(String title, String body) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'><meta name='viewport' content='width=device-width,initial-scale=1.0'><title>" + title + " — Admin</title>" + STYLE + "</head><body>" + NAV + "<div class='page'>" + body + "</div></body></html>";
    }

    public String systemStatus(int totalSold, float totalRevenue,
                               Map<String, float[]> perTicket, int totalUsers) {
        StringBuilder rows = new StringBuilder();
        for (Map.Entry<String, float[]> e : perTicket.entrySet()) {
            rows.append("<tr><td>").append(e.getKey()).append("</td>")
                    .append("<td>").append((int)e.getValue()[0]).append("</td>")
                    .append("<td>$").append(String.format("%.2f", e.getValue()[1])).append("</td></tr>");
        }
        if (rows.isEmpty()) {
            rows.append("<tr><td colspan='3' style='text-align:center;color:var(--muted);padding:30px;'>No sales yet</td></tr>");
        }

        return page("System Status", """
            <h1>Admin Dashboard</h1>
            <p class="subtitle">System overview</p>
            <div class="stat-grid">
                <div class="stat"><div class="val">%d</div><div class="label">Total Users</div></div>
                <div class="stat"><div class="val">%d</div><div class="label">Tickets Sold</div></div>
                <div class="stat"><div class="val">$%.2f</div><div class="label">Total Revenue</div></div>
            </div>
            <div class="card">
                <h3 style="font-family:'Playfair Display',serif;margin-bottom:20px;">Revenue Breakdown</h3>
                <table>
                    <thead><tr><th>Ticket</th><th>Sold</th><th>Revenue</th></tr></thead>
                    <tbody>%s</tbody>
                </table>
            </div>
            <div style="margin-top:24px;">
                <a href="/admin/tickets" class="btn">Manage Tickets</a>
            </div>
        """.formatted(totalUsers, totalSold, totalRevenue, rows));
    }

    public String manageTickets(List<Ticket> tickets) {
        StringBuilder rows = new StringBuilder();
        for (Ticket t : tickets) {
            rows.append("""
                <tr>
                    <td>%s</td>
                    <td>$%.2f</td>
                    <td>%s</td>
                    <td><span class="%s">%s</span></td>
                    <td style="display:flex;gap:8px;flex-wrap:wrap;">
                        <form method="POST" action="/admin/tickets/toggle" style="display:inline">
                            <input type="hidden" name="ticketId" value="%s"/>
                            <button class="btn btn-outline" type="submit" style="padding:6px 12px;font-size:0.8rem;">
                                %s
                            </button>
                        </form>
                        <form method="POST" action="/admin/tickets/remove"
                              style="display:inline"
                              onsubmit="return confirm('Are you sure you want to remove this ticket?')">
                            <input type="hidden" name="ticketId" value="%s"/>
                            <button class="btn btn-red" type="submit" style="padding:6px 12px;font-size:0.8rem;">Remove</button>
                        </form>
                    </td>
                </tr>
            """.formatted(
                    t.name, t.price, t.desc,
                    t.active ? "badge badge-green" : "badge badge-red",
                    t.active ? "Active" : "Inactive",
                    t.id, t.active ? "Deactivate" : "Activate",
                    t.id
            ));
        }

        return page("Manage Tickets", """
            <h1>Manage Tickets</h1>
            <p class="subtitle">Add, edit, or remove lottery tickets</p>

            <div class="card" style="margin-bottom:32px;">
                <h3 style="font-family:'Playfair Display',serif;margin-bottom:20px;">Add New Ticket</h3>
                <form method="POST" action="/admin/tickets/add"
                      onsubmit="return confirm('Add this new ticket?')">
                    <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                        <div class="form-group">
                            <label>Ticket Name</label>
                            <input type="text" name="name" placeholder="e.g. Golden Draw" required />
                        </div>
                        <div class="form-group">
                            <label>Price ($)</label>
                            <input type="number" name="price" step="0.01" min="0.01" placeholder="2.00" required />
                        </div>
                        <div class="form-group" style="grid-column:1/-1;">
                            <label>Description</label>
                            <input type="text" name="desc" placeholder="Short description" required />
                        </div>
                    </div>
                    <button class="btn" type="submit">Add Ticket</button>
                </form>
            </div>

            <div class="card">
                <h3 style="font-family:'Playfair Display',serif;margin-bottom:20px;">Current Tickets</h3>
                <table>
                    <thead>
                        <tr><th>Name</th><th>Price</th><th>Description</th><th>Status</th><th>Actions</th></tr>
                    </thead>
                    <tbody>""" + rows + """
                    </tbody>
                </table>
            </div>
        """);
    }
}