import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;

public class EmailNotifier {
    public String logFile;
    public Map<String, Object> smtpConfig;

    public EmailNotifier(String logFile, Map<String, Object> smtpConfig) {
        this.logFile    = logFile;
        this.smtpConfig = smtpConfig;
    }

    // ---------------------------------------------------------------
    // Log every email attempt to email.log
    // ---------------------------------------------------------------
    private void logEmail(String to, String subject, String body, boolean success) {
        try {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String status = success ? "SENT" : "FAILED";
            String entry  = String.format("[%s] [%s] TO=%s | SUBJECT=%s | BODY=%s%n",
                    timestamp, status, to, subject, body);
            Files.writeString(
                    Paths.get(logFile),
                    entry,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            System.err.println("Could not write to email log: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------
    // Send email via SMTP (Gmail by default)
    // ---------------------------------------------------------------
    public boolean sendEmail(String to, String subject, String body) {
        // If no SMTP config provided, just log and return
        if (smtpConfig == null || smtpConfig.isEmpty() ||
                !smtpConfig.containsKey("user") || !smtpConfig.containsKey("password")) {
            System.out.println("=== EMAIL (no SMTP configured) ===");
            System.out.println("TO:      " + to);
            System.out.println("SUBJECT: " + subject);
            System.out.println("BODY:    " + body);
            System.out.println("==================================");
            logEmail(to, subject, body, false);
            return false;
        }

        String host     = (String) smtpConfig.getOrDefault("host",     "smtp.gmail.com");
        int    port     = smtpConfig.containsKey("port")
                ? ((Number) smtpConfig.get("port")).intValue() : 587;
        String user     = (String) smtpConfig.get("user");
        String password = (String) smtpConfig.get("password");
        String from     = (String) smtpConfig.getOrDefault("from", user);

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            host);
        props.put("mail.smtp.port",            String.valueOf(port));
        props.put("mail.smtp.ssl.trust",       host);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from, "LPS — Lottery Purchase System"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Build a simple HTML email
            String htmlBody = buildHtmlEmail(subject, body);
            message.setContent(htmlBody, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("✅ Email sent to: " + to + " | Subject: " + subject);
            logEmail(to, subject, body, true);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Email failed to " + to + ": " + e.getMessage());
            logEmail(to, subject, body, false);
            return false;
        }
    }

    // ---------------------------------------------------------------
    // Simple HTML email template
    // ---------------------------------------------------------------
    private String buildHtmlEmail(String subject, String body) {
        // Convert newlines to <br> for HTML
        String htmlBody = body.replace("\n", "<br/>");
        return """
            <!DOCTYPE html>
            <html>
            <head>
              <meta charset="UTF-8"/>
              <style>
                body { font-family: 'Helvetica Neue', Arial, sans-serif; background:#f4f4f4; margin:0; padding:0; }
                .container { max-width:560px; margin:40px auto; background:#ffffff; border-radius:12px; overflow:hidden; box-shadow:0 4px 20px rgba(0,0,0,0.08); }
                .header { background:#0a0a0f; padding:28px 32px; text-align:center; }
                .header h1 { color:#c9a84c; font-size:1.6rem; margin:0; letter-spacing:2px; }
                .header p { color:#7a7a8a; font-size:0.8rem; margin:6px 0 0; letter-spacing:3px; text-transform:uppercase; }
                .body { padding:32px; color:#333333; font-size:0.95rem; line-height:1.7; }
                .body h2 { color:#0a0a0f; font-size:1.2rem; margin-bottom:12px; }
                .footer { background:#f9f9f9; padding:20px 32px; text-align:center; color:#aaaaaa; font-size:0.8rem; border-top:1px solid #eeeeee; }
                .btn { display:inline-block; margin-top:20px; padding:12px 28px; background:#c9a84c; color:#0a0a0f; font-weight:700; border-radius:8px; text-decoration:none; font-size:0.9rem; }
              </style>
            </head>
            <body>
              <div class="container">
                <div class="header">
                  <h1>🎰 LPS</h1>
                  <p>Texas Lottery Purchase System</p>
                </div>
                <div class="body">
                  <h2>%s</h2>
                  <p>%s</p>
                  <a href="http://localhost:4567/dashboard" class="btn">Go to Dashboard</a>
                </div>
                <div class="footer">
                  © 2026 Texas Lottery Purchase System · This is an automated message, please do not reply.
                </div>
              </div>
            </body>
            </html>
        """.formatted(subject, htmlBody);
    }
}