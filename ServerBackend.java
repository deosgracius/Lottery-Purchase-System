import java.util.*;
import static spark.Spark.*;

public class ServerBackend {
    public String root, userFile, ticketFile, orderFile, emailLog;
    public UserManager userMan;
    public TicketManager ticketMan;
    public OrderManager orderMan;
    public PaymentProcessor paymentMan;
    public EmailNotifier notifMan;
    public UserRender userRender;
    public AdminRender adminRender;
    public LoginRender loginRender;

    public ServerBackend(String root, String userFile, String ticketFile,
                         String orderFile, String emailLog, Map<String, Object> smtpConfig) {
        this.root = root; this.userFile = userFile; this.ticketFile = ticketFile;
        this.orderFile = orderFile; this.emailLog = emailLog;
        this.userMan    = new UserManager(userFile);
        this.ticketMan  = new TicketManager(ticketFile);
        this.orderMan   = new OrderManager(orderFile);
        this.paymentMan = new PaymentProcessor();
        this.notifMan   = new EmailNotifier(emailLog, smtpConfig != null ? smtpConfig : new HashMap<>());
        this.userRender  = new UserRender();
        this.adminRender = new AdminRender();
        this.loginRender = new LoginRender();
    }

    public ServerBackend() {
        this(".", "users.json", "tickets.json", "orders.json", "email.log", buildSmtpConfig());
    }

    // ---------------------------------------------------------------
    // *** CONFIGURE YOUR GMAIL HERE ***
    // ---------------------------------------------------------------
    private static Map<String, Object> buildSmtpConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("host",     "smtp.gmail.com");
        config.put("port",     587);
        config.put("user",     "YOUR_GMAIL@gmail.com");      // <-- replace with your Gmail
        config.put("password", "YOUR_APP_PASSWORD");          // <-- replace with Gmail App Password
        config.put("from",     "YOUR_GMAIL@gmail.com");      // <-- same Gmail
        return config;
    }

    // ---------------------------------------------------------------
    // Prize calculation (SRS 3.2.14)
    // ---------------------------------------------------------------
    public static float calculatePrize(int matches, float ticketPrice) {
        float base = ticketPrice * 1000;
        return switch (matches) {
            case 5 -> base;
            case 4 -> base * 0.20f;
            case 3 -> base * 0.05f;
            case 2 -> base * 0.01f;
            default -> 0f;
        };
    }

    // ---------------------------------------------------------------
    // Purchase flow
    // ---------------------------------------------------------------
    public Order purchaseFlow(String userEmail, String ticketId, int quantity,
                              List<Integer> chosenNumbers, Map<String, Object> paymentInfo) {
        Ticket ticket = ticketMan.getTicket(ticketId);
        if (ticket == null || !ticket.active) return null;
        float total = ticket.price * quantity;
        if (!paymentMan.processPayment(userEmail, paymentInfo, total)) return null;

        List<PurchasedTicket> purchased = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < quantity; i++) {
            PurchasedTicket pt = new PurchasedTicket();
            pt.id = ticket.id; pt.name = ticket.name; pt.price = ticket.price;
            pt.desc = ticket.desc; pt.imgSrc = ticket.imgSrc;
            pt.active = ticket.active; pt.ticketType = ticket.name;
            if (chosenNumbers != null && !chosenNumbers.isEmpty()) {
                pt.numbers = new ArrayList<>(chosenNumbers);
            } else {
                Set<Integer> numSet = new LinkedHashSet<>();
                while (numSet.size() < 5) numSet.add(random.nextInt(50) + 1);
                pt.numbers = new ArrayList<>(numSet);
            }
            purchased.add(pt);
        }

        Order order = orderMan.createOrder(userEmail, purchased, total);
        userMan.addOrderToUser(userEmail, order);

        // Send purchase confirmation email
        notifMan.sendEmail(
                userEmail,
                "Your LPS Order is Confirmed! 🎫",
                "Thank you for your purchase!\n\n" +
                        "Order ID: " + order.id.substring(0,8).toUpperCase() + "\n" +
                        "Tickets: " + quantity + "\n" +
                        "Total Paid: $" + String.format("%.2f", total) + "\n\n" +
                        "Good luck on Saturday's draw! 🍀\n\n" +
                        "You can view your tickets and order history by logging into your account."
        );

        return order;
    }

    // ---------------------------------------------------------------
    // Routes
    // ---------------------------------------------------------------
    private void setupRoutes() {

        before((req, res) -> {
            String path = req.pathInfo(), email = req.session().attribute("email");
            UserType role = req.session().attribute("role");
            boolean isPublic = path.equals("/login") || path.equals("/register")
                    || path.equals("/forgot-password") || path.equals("/reset-password") || path.equals("/");
            if (!isPublic && email == null) { res.redirect("/login"); halt(); }
            if (path.startsWith("/admin") && role != UserType.ADMIN) { res.redirect("/dashboard"); halt(); }
        });

        get("/", (req, res) -> {
            res.redirect(req.session().attribute("email") != null ? "/dashboard" : "/login"); return null;
        });

        // ---- Auth ----
        get("/login",  (req, res) -> loginRender.loginPage());
        post("/login", (req, res) -> {
            UserType role = userMan.authenticate(req.queryParams("email"), req.queryParams("password"));
            if (role == null) { res.redirect("/login?error=Invalid+email+or+password"); return null; }
            req.session(true).attribute("email", req.queryParams("email"));
            req.session().attribute("role", role);
            res.redirect(role == UserType.ADMIN ? "/admin" : "/dashboard"); return null;
        });

        get("/register",  (req, res) -> loginRender.registerPage());
        post("/register", (req, res) -> {
            String err = userMan.tryCreateAccount(
                    req.queryParams("name"), req.queryParams("email"),
                    req.queryParams("phone"), req.queryParams("birthday"),
                    req.queryParams("password"));
            if (err != null) res.redirect("/register?error=" + java.net.URLEncoder.encode(err, "UTF-8"));
            else {
                // Send welcome email
                notifMan.sendEmail(
                        req.queryParams("email"),
                        "Welcome to LPS — Texas Lottery Purchase System! 🎰",
                        "Hi " + req.queryParams("name") + ",\n\n" +
                                "Your account has been created successfully!\n\n" +
                                "Before purchasing tickets, please complete your profile.\n\n" +
                                "Good luck and may fortune be with you! 🍀"
                );
                res.redirect("/login?success=Account+created+successfully+Please+login");
            }
            return null;
        });

        get("/logout", (req, res) -> { req.session().invalidate(); res.redirect("/login"); return null; });

        // ---- Password Reset ----
        get("/forgot-password",  (req, res) -> loginRender.forgotPasswordPage());
        post("/forgot-password", (req, res) -> {
            String email = req.queryParams("email");
            String token = userMan.generateResetToken(email);
            if (token != null) {
                String link = "http://localhost:4567/reset-password?token=" + token;
                notifMan.sendEmail(
                        email,
                        "LPS — Reset Your Password 🔐",
                        "You requested a password reset.\n\n" +
                                "Click the link below to reset your password:\n\n" +
                                link + "\n\n" +
                                "This link is valid for this session only.\n\n" +
                                "If you did not request this, please ignore this email."
                );
                System.out.println("=== RESET LINK === " + link);
            }
            res.redirect("/forgot-password?success=If+that+email+exists+a+reset+link+has+been+sent");
            return null;
        });

        get("/reset-password", (req, res) -> {
            String token = req.queryParams("token");
            if (token == null || !userMan.isValidResetToken(token)) {
                res.redirect("/login?error=Invalid+or+expired+reset+link"); return null;
            }
            return loginRender.resetPasswordPage(token);
        });

        post("/reset-password", (req, res) -> {
            if (!req.queryParams("password").equals(req.queryParams("confirm"))) {
                res.redirect("/reset-password?token=" + req.queryParams("token") + "&error=Passwords+do+not+match");
                return null;
            }
            boolean ok = userMan.resetPassword(req.queryParams("token"), req.queryParams("password"));
            res.redirect(ok ? "/login?success=Password+reset+successfully+Please+login"
                    : "/login?error=Invalid+or+expired+reset+link");
            return null;
        });

        // ---- Dashboard ----
        get("/dashboard", (req, res) -> {
            String email = req.session().attribute("email");
            User user = userMan.getUser(email);
            List<Order> orders = orderMan.getOrdersForUser(email);
            int ticketCount = orders.stream().mapToInt(o -> o.tickets.size()).sum();
            int winCount    = (int) orders.stream().flatMap(o -> o.tickets.stream()).filter(t -> t.winning).count();
            float totalSpent = (float) orders.stream().mapToDouble(o -> o.total).sum();
            return userRender.dashboard(user, ticketCount, orders.size(), winCount, totalSpent);
        });

        // ---- Browse Tickets ----
        get("/tickets", (req, res) -> userRender.browseTickets(ticketMan.listActiveTickets()));

        // ---- Purchase — profile gate ----
        get("/tickets/purchase", (req, res) -> {
            User user = userMan.getUser(req.session().attribute("email"));
            if (!user.isProfileComplete()) {
                res.redirect("/profile/edit?error=Please+complete+your+profile+before+purchasing+tickets");
                return null;
            }
            return userRender.purchaseTickets(ticketMan.listActiveTickets(), req.queryParams("id"));
        });

        post("/tickets/purchase", (req, res) -> {
            String email = req.session().attribute("email");
            User user = userMan.getUser(email);
            if (!user.isProfileComplete()) {
                res.redirect("/profile/edit?error=Please+complete+your+profile+first"); return null;
            }
            String ticketId = req.queryParams("ticketId");
            int quantity;
            try { quantity = Integer.parseInt(req.queryParams("quantity")); }
            catch (Exception e) { res.redirect("/tickets/purchase?id=" + ticketId + "&error=Invalid+quantity"); return null; }
            if (quantity < 1 || quantity > 10) {
                res.redirect("/tickets/purchase?id=" + ticketId + "&error=Quantity+must+be+1-10"); return null;
            }
            List<Integer> chosenNumbers = new ArrayList<>();
            String numbersParam = req.queryParams("numbers");
            if (numbersParam != null && !numbersParam.equals("auto") && !numbersParam.isBlank()) {
                try {
                    for (String s : numbersParam.split(",")) chosenNumbers.add(Integer.parseInt(s.trim()));
                    if (chosenNumbers.size() != 5) {
                        res.redirect("/tickets/purchase?id=" + ticketId + "&error=Please+pick+exactly+5+numbers"); return null;
                    }
                } catch (Exception e) {
                    res.redirect("/tickets/purchase?id=" + ticketId + "&error=Invalid+numbers"); return null;
                }
            }
            Map<String, Object> paymentInfo = new HashMap<>();
            paymentInfo.put("method",     req.queryParams("paymentMethod"));
            paymentInfo.put("cardNumber", req.queryParams("cardNumber"));
            paymentInfo.put("expiry",     req.queryParams("expiry"));
            paymentInfo.put("cvv",        req.queryParams("cvv"));

            Order order = purchaseFlow(email, ticketId, quantity,
                    chosenNumbers.isEmpty() ? null : chosenNumbers, paymentInfo);
            if (order == null) {
                res.redirect("/tickets/purchase?id=" + ticketId + "&error=Payment+failed+please+try+again"); return null;
            }
            req.session().attribute("lastOrderId", order.id);
            res.redirect("/tickets/confirm"); return null;
        });

        get("/tickets/confirm", (req, res) -> {
            String orderId = req.session().attribute("lastOrderId");
            if (orderId == null) { res.redirect("/orders"); return null; }
            Order order = orderMan.getOrder(orderId);
            return order == null ? null : userRender.purchaseConfirm(order);
        });

        // ---- Orders ----
        get("/orders", (req, res) ->
                userRender.orderHistory(orderMan.getOrdersForUser(req.session().attribute("email"))));

        // ---- Winners ----
        get("/winners", (req, res) -> userRender.browseWinners(orderMan.orders));

        // ---- Display / Print ----
        get("/tickets/my",    (req, res) -> userRender.displayTickets(orderMan.getOrdersForUser(req.session().attribute("email"))));
        get("/tickets/print", (req, res) -> userRender.printTickets(orderMan.getOrdersForUser(req.session().attribute("email"))));

        // ---- Profile view ----
        get("/profile", (req, res) -> {
            User user = userMan.getUser(req.session().attribute("email"));
            String msg = req.queryParams("success"), err = req.queryParams("error");
            if (msg != null) return userRender.viewProfile(user, decodeParam(msg), false);
            if (err != null) return userRender.viewProfile(user, decodeParam(err), true);
            return userRender.viewProfile(user, null, false);
        });

        // ---- Profile edit ----
        get("/profile/edit", (req, res) -> {
            User user = userMan.getUser(req.session().attribute("email"));
            String err = req.queryParams("error");
            return userRender.editProfile(user, err != null ? decodeParam(err) : null, true);
        });

        // ---- Profile save ----
        post("/profile/save", (req, res) -> {
            String email = req.session().attribute("email");
            userMan.updateProfile(email,
                    req.queryParams("birthday"),
                    req.queryParams("address"),
                    req.queryParams("sex"),
                    req.queryParams("last4SSN"),
                    req.queryParams("last4ID"));
            res.redirect("/profile?success=Profile+saved+successfully"); return null;
        });

        // ---- Claim Prize ----
        get("/claim", (req, res) -> {
            String email   = req.session().attribute("email");
            String orderId = req.queryParams("orderId");
            String ticketId = req.queryParams("ticketId");
            Order order = orderMan.getOrder(orderId);
            if (order == null || !order.userEmail.equalsIgnoreCase(email)) {
                res.redirect("/orders"); return null;
            }
            PurchasedTicket target = null;
            for (PurchasedTicket pt : order.tickets) if (pt.id.equals(ticketId)) { target = pt; break; }
            if (target == null || !target.winning || target.claimed) {
                res.redirect("/orders?error=Ticket+cannot+be+claimed"); return null;
            }
            if (target.prizeAmount >= 600) {
                res.redirect("/orders?error=Prizes+of+$600+or+more+must+be+claimed+in+person"); return null;
            }
            orderMan.claimTicket(orderId, ticketId);

            // Send claim confirmation email
            notifMan.sendEmail(
                    email,
                    "Prize Claimed Successfully! 🏆",
                    "Congratulations!\n\n" +
                            "Your prize of $" + String.format("%.2f", target.prizeAmount) +
                            " for ticket " + target.confirmationNumber + " has been claimed.\n\n" +
                            "The amount will be deposited to your selected payment method shortly.\n\n" +
                            "Thank you for playing with LPS!"
            );

            res.redirect("/orders?success=Prize+of+$" +
                    String.format("%.2f", target.prizeAmount) + "+claimed+successfully!"); return null;
        });

        // ---- Admin ----
        get("/admin", (req, res) -> {
            Map<String, float[]> stats = orderMan.getRevenueStats();
            int sold = 0; float rev = 0;
            for (float[] v : stats.values()) { sold += (int)v[0]; rev += v[1]; }
            return adminRender.systemStatus(sold, rev, stats, userMan.users.size());
        });

        get("/admin/tickets", (req, res) -> adminRender.manageTickets(ticketMan.listTickets()));

        post("/admin/tickets/add", (req, res) -> {
            Map<String, Object> d = new HashMap<>();
            d.put("id",     UUID.randomUUID().toString());
            d.put("name",   req.queryParams("name"));
            d.put("price",  Float.parseFloat(req.queryParams("price")));
            d.put("desc",   req.queryParams("desc"));
            d.put("imgSrc", "");
            d.put("active", true);
            ticketMan.addTicket(d);
            res.redirect("/admin/tickets"); return null;
        });

        post("/admin/tickets/remove", (req, res) -> {
            ticketMan.removeTicket(req.queryParams("ticketId"));
            res.redirect("/admin/tickets"); return null;
        });

        post("/admin/tickets/toggle", (req, res) -> {
            ticketMan.toggleTicketAvailability(req.queryParams("ticketId"));
            res.redirect("/admin/tickets"); return null;
        });

        post("/admin/tickets/update", (req, res) -> {
            ticketMan.updateTicket(req.queryParams("ticketId"), req.queryParams("name"),
                    Float.parseFloat(req.queryParams("price")), req.queryParams("desc"), "");
            res.redirect("/admin/tickets"); return null;
        });
    }

    private String decodeParam(String s) {
        try { return java.net.URLDecoder.decode(s, "UTF-8"); } catch (Exception e) { return s; }
    }

    public static void main(String[] args) {
        ServerBackend backend = new ServerBackend();
        System.out.println("Server running at http://localhost:4567");
        System.out.println("Tickets: " + backend.ticketMan.listTickets().size() +
                "  Users: " + backend.userMan.users.size());
        backend.setupRoutes();
    }
}