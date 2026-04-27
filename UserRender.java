import java.util.List;

public class UserRender {

    private static final String STYLE = """
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=DM+Sans:wght@300;400;500&display=swap');
            *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
            :root { --bg:#0a0a0f;--card:#13131a;--border:#2a2a3a;--gold:#c9a84c;--gold-light:#e8c97a;--text:#f0ede6;--muted:#7a7a8a;--error:#e05252;--success:#52c08a; }
            body { background:var(--bg);font-family:'DM Sans',sans-serif;color:var(--text);min-height:100vh; }
            nav { background:var(--card);border-bottom:1px solid var(--border);padding:16px 40px;display:flex;align-items:center;justify-content:space-between;position:sticky;top:0;z-index:100; }
            .nav-brand { font-family:'Playfair Display',serif;font-size:1.3rem;background:linear-gradient(135deg,var(--gold),var(--gold-light));-webkit-background-clip:text;-webkit-text-fill-color:transparent;cursor:pointer; }
            .nav-links { display:flex;gap:28px;align-items:center; }
            .nav-links a { color:var(--muted);text-decoration:none;font-size:0.9rem;transition:color 0.2s; }
            .nav-links a:hover { color:var(--text); }
            .nav-logout { color:var(--error)!important; }
            .page { max-width:1100px;margin:0 auto;padding:48px 24px; }
            h1 { font-family:'Playfair Display',serif;font-size:2rem;margin-bottom:8px; }
            .subtitle { color:var(--muted);margin-bottom:40px; }
            .grid { display:grid;grid-template-columns:repeat(auto-fill,minmax(260px,1fr));gap:24px; }
            .card { background:var(--card);border:1px solid var(--border);border-radius:16px;padding:28px;transition:border-color 0.2s,transform 0.2s; }
            .card:hover { border-color:var(--gold);transform:translateY(-2px); }
            .card h3 { font-family:'Playfair Display',serif;font-size:1.2rem;margin-bottom:8px; }
            .card p { color:var(--muted);font-size:0.88rem;line-height:1.6; }
            .badge { display:inline-block;padding:4px 12px;border-radius:20px;font-size:0.75rem;font-weight:600;letter-spacing:1px;text-transform:uppercase; }
            .badge-gold { background:rgba(201,168,76,0.15);color:var(--gold);border:1px solid rgba(201,168,76,0.3); }
            .badge-green { background:rgba(82,192,138,0.15);color:var(--success);border:1px solid rgba(82,192,138,0.3); }
            .badge-red { background:rgba(224,82,82,0.15);color:var(--error);border:1px solid rgba(224,82,82,0.3); }
            .btn { display:inline-block;padding:11px 22px;background:linear-gradient(135deg,var(--gold),var(--gold-light));color:#0a0a0f;font-weight:700;border:none;border-radius:10px;cursor:pointer;text-decoration:none;font-size:0.88rem;transition:opacity 0.2s,transform 0.1s; }
            .btn:hover { opacity:0.9;transform:translateY(-1px); }
            .btn-outline { background:transparent;border:1px solid var(--gold);color:var(--gold); }
            .btn-sm { padding:7px 16px;font-size:0.82rem; }
            .stat-grid { display:grid;grid-template-columns:repeat(auto-fill,minmax(200px,1fr));gap:20px;margin-bottom:40px; }
            .stat { background:var(--card);border:1px solid var(--border);border-radius:12px;padding:24px; }
            .stat .val { font-family:'Playfair Display',serif;font-size:2rem;color:var(--gold); }
            .stat .label { color:var(--muted);font-size:0.8rem;text-transform:uppercase;letter-spacing:1px;margin-top:4px; }
            .price { font-size:1.4rem;font-family:'Playfair Display',serif;color:var(--gold);margin:10px 0; }
            .numbers { display:flex;gap:6px;flex-wrap:wrap;margin:10px 0; }
            .num { width:34px;height:34px;border-radius:50%;background:rgba(201,168,76,0.15);border:1px solid var(--gold);display:flex;align-items:center;justify-content:center;font-size:0.8rem;font-weight:600;color:var(--gold); }
            .num.match { background:var(--gold);color:#0a0a0f; }
            .form-group { margin-bottom:20px; }
            .form-group label { display:block;font-size:0.78rem;letter-spacing:1px;text-transform:uppercase;color:var(--muted);margin-bottom:8px; }
            .form-group input,.form-group select { width:100%;background:rgba(255,255,255,0.04);border:1px solid var(--border);border-radius:10px;padding:13px 16px;color:var(--text);font-family:'DM Sans',sans-serif;font-size:0.95rem;outline:none;transition:border-color 0.2s; }
            .form-group input:focus,.form-group select:focus { border-color:var(--gold); }
            .form-group select option { background:#13131a; }
            table { width:100%;border-collapse:collapse; }
            th { text-align:left;padding:12px 16px;color:var(--muted);font-size:0.8rem;text-transform:uppercase;letter-spacing:1px;border-bottom:1px solid var(--border); }
            td { padding:14px 16px;border-bottom:1px solid rgba(42,42,58,0.5);font-size:0.9rem; }
            tr:hover td { background:rgba(255,255,255,0.02); }
            .alert { padding:13px 18px;border-radius:10px;font-size:0.88rem;margin-bottom:20px; }
            .alert-error { background:rgba(224,82,82,0.1);border:1px solid rgba(224,82,82,0.3);color:var(--error); }
            .alert-success { background:rgba(82,192,138,0.1);border:1px solid rgba(82,192,138,0.3);color:var(--success); }
            .search-bar { width:100%;background:rgba(255,255,255,0.04);border:1px solid var(--border);border-radius:10px;padding:13px 16px;color:var(--text);font-family:'DM Sans',sans-serif;font-size:0.95rem;outline:none;margin-bottom:28px;transition:border-color 0.2s; }
            .search-bar:focus { border-color:var(--gold); }
            .num-btn { width:40px;height:40px;border-radius:50%;background:rgba(255,255,255,0.04);border:1px solid var(--border);color:var(--muted);font-size:0.8rem;cursor:pointer;transition:all 0.15s; }
            .num-btn:hover { border-color:var(--gold);color:var(--gold); }
            .num-btn.selected { background:var(--gold);border-color:var(--gold);color:#0a0a0f;font-weight:700; }
            .pending { color:var(--muted);font-style:italic; }
            .winner-badge { background:linear-gradient(135deg,var(--gold),var(--gold-light));color:#0a0a0f;padding:3px 10px;border-radius:20px;font-size:0.75rem;font-weight:700; }
            .profile-banner { background:linear-gradient(135deg,rgba(201,168,76,0.1),rgba(232,201,122,0.05));border:1px solid rgba(201,168,76,0.3);border-radius:12px;padding:16px 20px;margin-bottom:28px;display:flex;align-items:center;gap:12px;font-size:0.9rem; }
            /* Profile info rows */
            .info-row { display:flex;justify-content:space-between;align-items:center;padding:14px 0;border-bottom:1px solid rgba(42,42,58,0.5); }
            .info-row:last-child { border-bottom:none; }
            .info-label { color:var(--muted);font-size:0.82rem;text-transform:uppercase;letter-spacing:1px;min-width:140px; }
            .info-value { color:var(--text);font-size:0.95rem;text-align:right; }
            .info-missing { color:var(--error);font-size:0.9rem;font-style:italic; }
            canvas#fireworks { position:fixed;top:0;left:0;width:100%;height:100%;pointer-events:none;z-index:999; }
            /* Auto-select toggle */
            .auto-toggle { display:flex;align-items:center;gap:12px;background:rgba(201,168,76,0.08);border:1px solid rgba(201,168,76,0.3);border-radius:10px;padding:14px 18px;margin-bottom:16px;cursor:pointer; }
            .auto-toggle input[type=checkbox] { width:18px;height:18px;accent-color:var(--gold);cursor:pointer; }
            .auto-toggle span { font-size:0.95rem;color:var(--gold);font-weight:500; }
        </style>
    """;

    private String nav(String active) {
        return "<nav><div class='nav-brand' onclick=\"location.href='/dashboard'\">🎰 LPS</div><div class='nav-links'>" +
                link("/dashboard","Home","dashboard",active) + link("/tickets","Buy Tickets","tickets",active) +
                link("/orders","My Orders","orders",active) + link("/winners","Winners","winners",active) +
                link("/profile","Profile","profile",active) +
                "<a href='/logout' class='nav-logout'>Logout</a></div></nav>";
    }
    private String link(String href, String label, String key, String active) {
        return "<a href='" + href + "'" + (key.equals(active) ? " style='color:var(--gold)'" : "") + ">" + label + "</a>";
    }
    private String page(String title, String active, String body) {
        return "<!DOCTYPE html><html lang='en'><head><meta charset='UTF-8'>" +
                "<meta name='viewport' content='width=device-width,initial-scale=1.0'>" +
                "<title>" + title + " — LPS</title>" + STYLE +
                "</head><body>" + nav(active) + "<div class='page'>" + body + "</div></body></html>";
    }

    // ---- Dashboard ----
    public String dashboard(User user, int ticketCount, int orderCount, int winCount, float totalSpent) {
        String firstName = user.name != null && user.name.contains(" ")
                ? user.name.split(" ")[0] : (user.name != null ? user.name : "");
        String banner = user.isProfileComplete() ? "" :
                "<div class='profile-banner'>⚠️ <span>Your profile is incomplete. " +
                        "<a href='/profile' style='color:var(--gold);font-weight:600;'>Complete it here</a> before purchasing tickets.</span></div>";
        return page("Dashboard", "dashboard",
                banner + "<h1>Hello, " + firstName + "! 👋</h1><p class='subtitle'>Your lottery overview</p>" +
                        "<div class='stat-grid'>" +
                        stat(String.valueOf(ticketCount), "Tickets Owned") + stat(String.valueOf(orderCount), "Total Orders") +
                        stat(String.valueOf(winCount), "Winning Tickets") + stat("$" + String.format("%.2f", totalSpent), "Total Spent") +
                        "</div><div style='display:flex;gap:16px;flex-wrap:wrap;'>" +
                        "<a href='/tickets' class='btn'>Browse Tickets</a><a href='/orders' class='btn btn-outline'>View Orders</a></div>");
    }
    private String stat(String val, String label) {
        return "<div class='stat'><div class='val'>" + val + "</div><div class='label'>" + label + "</div></div>";
    }

    // ---- Browse Tickets ----
    public String browseTickets(List<Ticket> tickets) {
        StringBuilder cards = new StringBuilder();
        for (Ticket t : tickets) {
            if (!t.active) continue;
            cards.append("<div class='card ticket-card' data-name='").append(t.name.toLowerCase()).append("'>" +
                    "<span class='badge badge-green'>Active</span>" +
                    "<h3 style='margin-top:12px'>").append(t.name).append("</h3>" +
                    "<div class='price'>$").append(String.format("%.2f", t.price)).append("</div>" +
                    "<p>").append(t.desc).append("</p>" +
                    "<div style='margin-top:20px;'><a href='/tickets/purchase?id=").append(t.id).append("' class='btn'>Purchase</a></div></div>");
        }
        if (cards.isEmpty()) cards.append("<div class='card' style='text-align:center;padding:40px;grid-column:1/-1'><p>No tickets available.</p></div>");
        return page("Browse Tickets", "tickets",
                "<h1>Available Tickets</h1><p class='subtitle'>Choose your lucky ticket — drawings every Saturday</p>" +
                        "<input class='search-bar' placeholder='🔍  Search tickets by name...' oninput=\"document.querySelectorAll('.ticket-card').forEach(c=>c.style.display=c.dataset.name.includes(this.value.toLowerCase())?'':'none')\" />" +
                        "<div class='grid'>" + cards + "</div>");
    }

    // ---- Purchase Page ----
    public String purchaseTickets(List<Ticket> tickets, String selectedId) {
        StringBuilder options = new StringBuilder();
        for (Ticket t : tickets) {
            if (!t.active) continue;
            String sel = t.id.equals(selectedId) ? "selected" : "";
            options.append("<option value='").append(t.id).append("' data-price='").append(t.price).append("' ").append(sel).append(">")
                    .append(t.name).append(" — $").append(String.format("%.2f", t.price)).append("</option>");
        }
        StringBuilder numBtns = new StringBuilder();
        for (int i = 1; i <= 50; i++)
            numBtns.append("<button type='button' class='num-btn' onclick='toggleNum(").append(i).append(")' id='n").append(i).append("'>").append(i).append("</button>");

        return page("Purchase Tickets", "tickets", """
            <h1>Purchase Tickets</h1>
            <p class="subtitle">Complete your purchase below</p>
            <div id="error-box"></div>
            <div style="display:grid;grid-template-columns:1fr 340px;gap:32px;align-items:start;">
              <div class="card">
                <form method="POST" action="/tickets/purchase" id="purchaseForm" onsubmit="return validateForm()">
                  <div class="form-group"><label>Ticket Type</label>
                    <select name="ticketId" id="ticketSelect" onchange="updatePrice()">""" + options + """
                    </select></div>
                  <div class="form-group"><label>Quantity (max 10)</label>
                    <input type="number" name="quantity" id="qty" min="1" max="10" value="1" oninput="updatePrice()" required /></div>

                  <div class="form-group">
                    <label>Your Lucky Numbers</label>
                    <p style="color:var(--muted);font-size:0.85rem;margin-bottom:14px;">Each ticket requires exactly 5 numbers between 1 and 50.</p>

                    <!-- Auto-select toggle (prominent) -->
                    <label class="auto-toggle" for="autoCheck">
                      <input type="checkbox" id="autoCheck" onchange="toggleAuto(this.checked)" />
                      <span>🎲 Auto-select my numbers for me</span>
                    </label>

                    <div id="numPickerWrap">
                      <div style="display:flex;gap:5px;flex-wrap:wrap;margin-bottom:10px;" id="numPicker">""" + numBtns + """
                      </div>
                      <div id="numCount" style="color:var(--muted);font-size:0.85rem;margin-top:4px;">0 / 5 numbers selected</div>
                    </div>
                    <input type="hidden" name="numbers" id="numbersInput" />
                  </div>

                  <hr style="border-color:var(--border);margin:24px 0;"/>
                  <h3 style="font-family:'Playfair Display',serif;margin-bottom:20px;">Payment Details</h3>
                  <div class="form-group"><label>Payment Method</label>
                    <select name="paymentMethod" id="payMethod" onchange="showPayFields()">
                      <option value="card">Credit / Debit Card</option>
                      <option value="paypal">PayPal</option>
                      <option value="venmo">Venmo</option>
                    </select></div>
                  <div id="cardFields">
                    <div class="form-group"><label>Card Number</label>
                      <input type="text" name="cardNumber" placeholder="1234 5678 9012 3456" maxlength="19"/></div>
                    <div style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                      <div class="form-group"><label>Expiry</label><input type="text" name="expiry" placeholder="MM/YY"/></div>
                      <div class="form-group"><label>CVV</label><input type="text" name="cvv" placeholder="123" maxlength="3"/></div>
                    </div>
                  </div>
                  <div id="paypalFields" style="display:none;">
                    <div class="form-group"><label>PayPal Email</label><input type="email" name="paypalEmail" placeholder="you@paypal.com"/></div>
                  </div>
                  <div id="venmoFields" style="display:none;">
                    <div class="form-group"><label>Venmo Username</label><input type="text" name="venmoUser" placeholder="@username"/></div>
                  </div>
                  <button class="btn" type="submit" style="width:100%;margin-top:8px;padding:15px;">Complete Purchase</button>
                </form>
              </div>
              <div class="card" style="position:sticky;top:90px;">
                <h3 style="font-family:'Playfair Display',serif;margin-bottom:20px;">Order Summary</h3>
                <div style="display:flex;justify-content:space-between;margin-bottom:12px;"><span style="color:var(--muted)">Ticket</span><span id="summTicket">—</span></div>
                <div style="display:flex;justify-content:space-between;margin-bottom:12px;"><span style="color:var(--muted)">Quantity</span><span id="summQty">1</span></div>
                <hr style="border-color:var(--border);margin:16px 0;"/>
                <div style="display:flex;justify-content:space-between;">
                  <span style="font-weight:600;">Total</span>
                  <span id="summTotal" style="font-family:'Playfair Display',serif;font-size:1.3rem;color:var(--gold);">$0.00</span>
                </div>
              </div>
            </div>
            <script>
            const selected=[];
            function toggleNum(n){
              const btn=document.getElementById('n'+n),idx=selected.indexOf(n);
              if(idx>=0){selected.splice(idx,1);btn.classList.remove('selected');}
              else{
                if(selected.length>=5){document.getElementById('error-box').innerHTML='<div class="alert alert-error">You can only pick 5 numbers.</div>';return;}
                selected.push(n);btn.classList.add('selected');
                document.getElementById('error-box').innerHTML='';
              }
              document.getElementById('numCount').textContent=selected.length+' / 5 numbers selected';
              document.getElementById('numbersInput').value=selected.join(',');
            }
            function toggleAuto(checked){
              document.getElementById('numPickerWrap').style.display=checked?'none':'block';
              document.getElementById('numbersInput').value=checked?'auto':'';
              if(!checked){selected.length=0;document.querySelectorAll('.num-btn').forEach(b=>b.classList.remove('selected'));document.getElementById('numCount').textContent='0 / 5 numbers selected';}
            }
            function updatePrice(){
              const sel=document.getElementById('ticketSelect'),opt=sel.options[sel.selectedIndex];
              const price=parseFloat(opt.dataset.price||0),qty=parseInt(document.getElementById('qty').value)||1;
              document.getElementById('summTicket').textContent=opt.text.split('—')[0].trim();
              document.getElementById('summQty').textContent=qty;
              document.getElementById('summTotal').textContent='$'+(price*qty).toFixed(2);
            }
            function showPayFields(){
              const m=document.getElementById('payMethod').value;
              document.getElementById('cardFields').style.display=m==='card'?'':'none';
              document.getElementById('paypalFields').style.display=m==='paypal'?'':'none';
              document.getElementById('venmoFields').style.display=m==='venmo'?'':'none';
            }
            function validateForm(){
              const v=document.getElementById('numbersInput').value;
              if(!v||(v!=='auto'&&selected.length<5)){
                document.getElementById('error-box').innerHTML='<div class="alert alert-error">Please pick 5 numbers or check "Auto-select my numbers for me".</div>';
                window.scrollTo(0,0);return false;}
              return true;
            }
            updatePrice();
            </script>
        """);
    }

    // ---- Purchase Confirmation ----
    public String purchaseConfirm(Order order) {
        StringBuilder ticketCards = new StringBuilder();
        for (PurchasedTicket pt : order.tickets) {
            StringBuilder nums = new StringBuilder();
            for (int n : pt.numbers) nums.append("<div class='num'>").append(n).append("</div>");
            ticketCards.append("<div class='card' style='margin-bottom:16px;'>" +
                    "<div style='display:flex;justify-content:space-between;align-items:center;margin-bottom:12px;'>" +
                    "<h3>").append(pt.name).append("</h3><span class='badge badge-gold'>").append(pt.confirmationNumber).append("</span></div>" +
                    "<div class='numbers'>").append(nums).append("</div>" +
                    "<p style='color:var(--muted);font-size:0.85rem;margin-top:8px;'>Draw: Next Saturday</p></div>");
        }
        return page("Order Confirmed", "orders",
                "<div style='text-align:center;margin-bottom:40px;'><div style='font-size:3.5rem;margin-bottom:16px;'>🎉</div>" +
                        "<h1>Purchase Confirmed!</h1><p class='subtitle'>Good luck on Saturday's draw!</p></div>" +
                        "<div style='max-width:600px;margin:0 auto;'>" +
                        "<div style='display:flex;justify-content:space-between;margin-bottom:24px;padding:16px 20px;background:var(--card);border:1px solid var(--border);border-radius:12px;'>" +
                        "<span style='color:var(--muted);'>Order ID</span><strong>" + order.id.substring(0,8).toUpperCase() + "</strong></div>" +
                        ticketCards +
                        "<div style='display:flex;justify-content:space-between;padding:16px 20px;background:var(--card);border:1px solid var(--gold);border-radius:12px;margin-top:8px;'>" +
                        "<span>Total Paid</span><strong style='color:var(--gold);font-family:\"Playfair Display\",serif;font-size:1.2rem;'>$" +
                        String.format("%.2f", order.total) + "</strong></div>" +
                        "<div style='display:flex;gap:16px;margin-top:32px;'><a href='/orders' class='btn'>View My Orders</a>" +
                        "<a href='/tickets' class='btn btn-outline'>Buy More</a></div></div>");
    }

    // ---- Order History ----
    public String orderHistory(List<Order> orders) {
        if (orders.isEmpty()) return page("Order History", "orders",
                "<h1>Order History</h1><p class='subtitle'>Your past purchases</p>" +
                        "<div class='card' style='text-align:center;padding:60px;'><div style='font-size:3rem;margin-bottom:16px;'>🎫</div>" +
                        "<h3>No orders yet</h3><p style='margin-top:8px'><a href='/tickets' style='color:var(--gold);'>Buy your first ticket!</a></p></div>");
        StringBuilder rows = new StringBuilder();
        for (Order o : orders) {
            for (PurchasedTicket pt : o.tickets) {
                String statusCell, winNumsCell;
                if (!pt.resolved) {
                    statusCell  = "<span class='pending'>PENDING</span>";
                    winNumsCell = "<span class='pending'>Next Saturday</span>";
                } else if (pt.winning) {
                    statusCell  = "<span class='winner-badge'>🏆 Winner!</span>";
                    winNumsCell = buildNums(pt.winningNumbers, pt.numbers);
                } else {
                    statusCell  = "<span style='color:var(--muted)'>No win</span>";
                    winNumsCell = buildNums(pt.winningNumbers, pt.numbers);
                }
                String action = "";
                if (pt.winning && !pt.claimed && pt.prizeAmount < 600)
                    action = "<a href='/claim?orderId=" + o.id + "&ticketId=" + pt.id + "' class='btn btn-sm'>Claim</a>";
                else if (pt.claimed)
                    action = "<span class='badge badge-green'>Claimed</span>";
                else if (pt.winning)
                    action = "<span style='color:var(--muted);font-size:0.8rem;'>Visit center</span>";
                rows.append("<tr>")
                        .append("<td><span style='font-family:monospace;font-size:0.8rem;'>").append(pt.confirmationNumber).append("</span></td>")
                        .append("<td>").append(pt.name).append("</td>")
                        .append("<td>").append(o.purchaseDate).append("</td>")
                        .append("<td>").append(buildNums(pt.numbers, pt.winningNumbers)).append("</td>")
                        .append("<td>").append(winNumsCell).append("</td>")
                        .append("<td>").append(statusCell).append("</td>")
                        .append("<td>$").append(String.format("%.2f", pt.price)).append("</td>")
                        .append("<td>").append(action).append("</td></tr>");
            }
        }
        return page("Order History", "orders",
                "<h1>Order History</h1><p class='subtitle'>Matching numbers highlighted in gold</p>" +
                        "<div class='card' style='overflow-x:auto;'><table><thead><tr>" +
                        "<th>Confirmation</th><th>Ticket</th><th>Date</th><th>Your Numbers</th><th>Winning Numbers</th><th>Status</th><th>Price</th><th>Action</th>" +
                        "</tr></thead><tbody>" + rows + "</tbody></table></div>");
    }

    private String buildNums(List<Integer> primary, List<Integer> matchAgainst) {
        if (primary == null || primary.isEmpty()) return "<span class='pending'>—</span>";
        StringBuilder sb = new StringBuilder("<div class='numbers' style='flex-wrap:nowrap;gap:3px;'>");
        for (int n : primary) {
            boolean match = matchAgainst != null && matchAgainst.contains(n);
            sb.append("<div class='num").append(match ? " match" : "").append("' style='width:28px;height:28px;font-size:0.75rem;'>").append(n).append("</div>");
        }
        return sb.append("</div>").toString();
    }

    // ---- Winners with fireworks ----
    public String browseWinners(List<Order> allOrders) {
        StringBuilder cards = new StringBuilder();
        boolean hasWinners = false;
        for (Order o : allOrders) {
            for (PurchasedTicket pt : o.tickets) {
                if (!pt.winning || pt.winningNumbers == null) continue;
                hasWinners = true;
                StringBuilder nums = new StringBuilder();
                for (int n : pt.winningNumbers) nums.append("<div class='num'>").append(n).append("</div>");
                cards.append("<div class='card winner-card' data-name='").append(pt.name.toLowerCase()).append("'>" +
                        "<span class='winner-badge'>🏆 Winner</span>" +
                        "<h3 style='margin-top:12px'>").append(pt.name).append("</h3>" +
                        "<div class='numbers' style='margin:12px 0;'>").append(nums).append("</div>" +
                        "<p style='color:var(--muted);font-size:0.85rem;'>Prize: <span style='color:var(--gold)'>$").append(String.format("%.2f", pt.prizeAmount)).append("</span></p></div>");
            }
        }
        if (!hasWinners) cards.append("<div class='card' style='text-align:center;padding:60px;grid-column:1/-1;'>" +
                "<div style='font-size:3rem;margin-bottom:16px;'>🏆</div><h3>No winners yet</h3>" +
                "<p style='margin-top:8px'><a href='/tickets' style='color:var(--gold);'>Be the first to win!</a></p></div>");

        String fireworks = !hasWinners ? "" : """
            <canvas id="fireworks"></canvas>
            <script>
            (function(){
              const c=document.getElementById('fireworks'),ctx=c.getContext('2d');
              c.width=innerWidth;c.height=innerHeight;
              window.addEventListener('resize',()=>{c.width=innerWidth;c.height=innerHeight;});
              const P=[],cols=['#c9a84c','#e8c97a','#fff','#ff6b6b','#51cf66','#74c0fc','#f783ac','#ffd43b'];
              function Particle(x,y,col){this.x=x;this.y=y;this.col=col;this.vx=(Math.random()-.5)*9;this.vy=(Math.random()-.5)*9-3;this.a=1;this.r=Math.random()*3+1;}
              Particle.prototype.update=function(){this.x+=this.vx;this.y+=this.vy;this.vy+=0.12;this.a-=0.012;};
              Particle.prototype.draw=function(){ctx.save();ctx.globalAlpha=this.a;ctx.fillStyle=this.col;ctx.beginPath();ctx.arc(this.x,this.y,this.r,0,Math.PI*2);ctx.fill();ctx.restore();};
              function burst(){const x=Math.random()*c.width,y=Math.random()*c.height*.5,col=cols[Math.floor(Math.random()*cols.length)];for(let i=0;i<90;i++)P.push(new Particle(x,y,col));}
              function loop(){ctx.clearRect(0,0,c.width,c.height);for(let i=P.length-1;i>=0;i--){P[i].update();P[i].draw();if(P[i].a<=0)P.splice(i,1);}requestAnimationFrame(loop);}
              burst();burst();burst();setInterval(burst,1100);loop();
              setTimeout(()=>c.style.display='none',9000);
            })();
            </script>
        """;

        return page("Winners", "winners",
                fireworks +
                        "<h1>Previous Winners</h1><p class='subtitle'>Past lottery winners and their winning numbers</p>" +
                        "<input class='search-bar' placeholder='🔍  Search by ticket name...' oninput=\"document.querySelectorAll('.winner-card').forEach(c=>c.style.display=c.dataset.name.includes(this.value.toLowerCase())?'':'none')\" />" +
                        "<div class='grid'>" + cards + "</div>");
    }

    // ---- Display Tickets ----
    public String displayTickets(List<Order> orders) {
        StringBuilder items = new StringBuilder();
        for (Order o : orders) {
            for (PurchasedTicket pt : o.tickets) {
                if (!pt.winning) continue;
                StringBuilder nums = new StringBuilder();
                for (int n : pt.numbers) nums.append("<div class='num'>").append(n).append("</div>");
                String action = pt.claimed ? "<span class='badge badge-green'>Claimed</span>"
                        : "<a href='/claim?orderId=" + o.id + "&ticketId=" + pt.id + "' class='btn'>Claim Prize</a>";
                items.append("<div class='card' style='margin-bottom:20px;display:flex;justify-content:space-between;align-items:start;'><div>" +
                        "<span class='winner-badge'>🏆 Winner</span><h3 style='margin-top:10px;'>").append(pt.name).append("</h3>" +
                        "<div class='numbers' style='margin-top:10px;'>").append(nums).append("</div>" +
                        "<p style='color:var(--muted);font-size:0.85rem;margin-top:6px;'>Confirmation: ").append(pt.confirmationNumber).append("</p>" +
                        "<p style='color:var(--gold);font-weight:600;margin-top:4px;'>Prize: $").append(String.format("%.2f", pt.prizeAmount)).append("</p>" +
                        "</div>").append(action).append("</div>");
            }
        }
        if (items.isEmpty()) items.append("<div class='card' style='text-align:center;padding:60px;'><div style='font-size:3rem;margin-bottom:16px;'>🎫</div><h3>No winning tickets yet</h3></div>");
        return page("My Winning Tickets", "orders", "<h1>My Winning Tickets</h1><p class='subtitle'>Only winning tickets shown here</p>" + items);
    }

    // ---- Print Tickets ----
    public String printTickets(List<Order> orders) {
        StringBuilder items = new StringBuilder();
        for (Order o : orders) {
            for (PurchasedTicket pt : o.tickets) {
                if (!pt.winning) continue;
                StringBuilder nums = new StringBuilder();
                for (int n : pt.numbers) nums.append("<div class='num'>").append(n).append("</div>");
                items.append("<div class='card' style='margin-bottom:20px;display:flex;justify-content:space-between;align-items:center;'><div>" +
                        "<h3>").append(pt.name).append("</h3><div class='numbers' style='margin-top:8px;'>").append(nums).append("</div>" +
                        "<p style='color:var(--muted);font-size:0.85rem;margin-top:6px;'>Confirmation: ").append(pt.confirmationNumber).append("</p></div>" +
                        "<a href='/tickets/print-pdf?orderId=").append(o.id).append("&ticketId=").append(pt.id).append("' class='btn'>🖨️ Print PDF</a></div>");
            }
        }
        if (items.isEmpty()) items.append("<div class='card' style='text-align:center;padding:60px;'><div style='font-size:3rem;margin-bottom:16px;'>🖨️</div><h3>No winning tickets to print</h3></div>");
        return page("Print Tickets", "orders", "<h1>Print Tickets</h1><p class='subtitle'>Download a PDF of your winning tickets</p>" + items);
    }

    // ---- Profile — READ-ONLY summary view ----
    public String viewProfile(User user, String message, boolean isError) {
        // Alert banner
        String alertHtml = "";
        if (message != null && !message.isBlank()) {
            alertHtml = "<div class='alert " + (isError ? "alert-error" : "alert-success") + "'>" + message + "</div>";
        }

        // Completion status
        String statusBanner = user.isProfileComplete()
                ? "<div class='alert alert-success' style='margin-bottom:24px;'>✅ Your profile is complete. You can purchase tickets.</div>"
                : "<div class='alert alert-error' style='margin-bottom:24px;'>⚠️ Please complete the missing fields below to purchase tickets.</div>";

        // Helper for a masked value
        String ssnDisplay  = (user.last4SSN != null && user.last4SSN.length()==4) ? "***-**-" + user.last4SSN : null;
        String idDisplay   = (user.last4ID  != null && user.last4ID.length()==4)  ? "***-" + user.last4ID     : null;

        // Read-only info rows
        String infoCard = "<div class='card' style='margin-bottom:24px;'>" +
                "<div style='display:flex;justify-content:space-between;align-items:center;margin-bottom:20px;'>" +
                "<h3 style='font-family:\"Playfair Display\",serif;'>Account Info</h3>" +
                "<a href='/profile/edit' class='btn btn-outline btn-sm'>✏️ Edit Profile</a></div>" +
                infoRow("Full Name",    user.name) +
                infoRow("Email",        user.email) +
                infoRow("Phone",        user.phone) +
                infoRow("Date of Birth",user.birthday) +
                infoRow("Address",      user.address) +
                infoRow("Sex",          user.sex) +
                infoRow("SSN (last 4)", ssnDisplay) +
                infoRow("State ID (last 4)", idDisplay) +
                "</div>";

        return page("My Profile", "profile",
                alertHtml + "<h1>My Profile</h1><p class='subtitle'>Your account information</p>" +
                        "<div style='max-width:560px;'>" + statusBanner + infoCard + "</div>");
    }

    private String infoRow(String label, String value) {
        String display = (value == null || value.isBlank())
                ? "<span class='info-missing'>Not provided</span>"
                : "<span class='info-value'>" + esc(value) + "</span>";
        return "<div class='info-row'><span class='info-label'>" + label + "</span>" + display + "</div>";
    }

    // ---- Profile Edit Form ----
    public String editProfile(User user, String message, boolean isError) {
        String alertHtml = "";
        if (message != null && !message.isBlank())
            alertHtml = "<div class='alert " + (isError ? "alert-error" : "alert-success") + "'>" + message + "</div>";

        // DOB is editable if missing
        String dobField = (user.birthday == null || user.birthday.isBlank())
                ? "<div class='form-group'><label>Date of Birth</label><input type='date' name='birthday' required /></div>"
                : "<div class='form-group'><label>Date of Birth</label><input type='date' value='" + esc(user.birthday) + "' readonly style='opacity:0.6;cursor:not-allowed;' /></div>";

        return page("Edit Profile", "profile",
                alertHtml + "<h1>Edit Profile</h1><p class='subtitle'>Fill in your details to complete your profile</p>" +
                        "<div style='max-width:540px;'><div class='card'>" +
                        "<h3 style='font-family:\"Playfair Display\",serif;margin-bottom:4px;'>Account Info</h3>" +
                        "<p style='color:var(--muted);font-size:0.85rem;margin-bottom:20px;'>Name, email, and phone are set at registration and cannot be changed.</p>" +
                        "<div class='form-group'><label>Full Name</label><input type='text' value='" + esc(user.name) + "' readonly style='opacity:0.6;cursor:not-allowed;'/></div>" +
                        "<div class='form-group'><label>Email</label><input type='email' value='" + esc(user.email) + "' readonly style='opacity:0.6;cursor:not-allowed;'/></div>" +
                        "<div class='form-group'><label>Phone</label><input type='tel' value='" + esc(nullSafe(user.phone)) + "' readonly style='opacity:0.6;cursor:not-allowed;'/></div>" +
                        dobField +
                        "<hr style='border-color:var(--border);margin:24px 0;'/>" +
                        "<h3 style='font-family:\"Playfair Display\",serif;margin-bottom:20px;'>Complete Your Profile</h3>" +
                        "<form method='POST' action='/profile/save'>" +
                        "<div class='form-group'><label>Home Address</label><input type='text' name='address' placeholder='123 Main St, City, TX' value='" + esc(nullSafe(user.address)) + "' required /></div>" +
                        "<div class='form-group'><label>Sex</label><select name='sex'>" +
                        "<option value='' disabled " + (isBlank(user.sex) ? "selected" : "") + ">Select...</option>" +
                        opt("Male",   user.sex) + opt("Female", user.sex) + opt("Other", user.sex) +
                        "</select></div>" +
                        "<div style='display:grid;grid-template-columns:1fr 1fr;gap:16px;'>" +
                        "<div class='form-group'><label>Last 4 of SSN</label><input type='text' name='last4SSN' maxlength='4' pattern='[0-9]{4}' placeholder='1234' value='" + esc(nullSafe(user.last4SSN)) + "' required /></div>" +
                        "<div class='form-group'><label>Last 4 of State ID</label><input type='text' name='last4ID' maxlength='4' placeholder='5678' value='" + esc(nullSafe(user.last4ID)) + "' required /></div>" +
                        "</div>" +
                        (user.birthday == null || user.birthday.isBlank()
                                ? "<input type='hidden' name='birthday' id='hidBirthday'/>" : "") +
                        "<div style='display:flex;gap:12px;'>" +
                        "<button class='btn' type='submit' style='flex:1;padding:14px;'>Save Profile</button>" +
                        "<a href='/profile' class='btn btn-outline' style='flex:1;padding:14px;text-align:center;'>Cancel</a></div>" +
                        "</form></div></div>");
    }

    private String opt(String val, String current) {
        return "<option value='" + val + "'" + (val.equals(current) ? " selected" : "") + ">" + val + "</option>";
    }
    private String esc(String s) { return s == null ? "" : s.replace("'","&#39;").replace("\"","&quot;"); }
    private String nullSafe(String s) { return s == null ? "" : s; }
    private boolean isBlank(String s) { return s == null || s.isBlank(); }
}