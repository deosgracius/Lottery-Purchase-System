public class LoginRender {

    private static final String STYLE = """
        <style>
            @import url('https://fonts.googleapis.com/css2?family=Playfair+Display:wght@700&family=DM+Sans:wght@300;400;500&display=swap');
            *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
            :root { --bg:#0a0a0f;--card:#13131a;--border:#2a2a3a;--gold:#c9a84c;--gold-light:#e8c97a;--text:#f0ede6;--muted:#7a7a8a;--error:#e05252;--success:#52c08a; }
            body { background:var(--bg);font-family:'DM Sans',sans-serif;color:var(--text);min-height:100vh;display:flex;align-items:center;justify-content:center;
                background-image:radial-gradient(ellipse at 20% 50%,#1a1a2e 0%,transparent 60%),radial-gradient(ellipse at 80% 20%,#16213e 0%,transparent 50%); }
            .container { width:100%;max-width:440px;padding:20px; }
            .logo { text-align:center;margin-bottom:40px; }
            .logo h1 { font-family:'Playfair Display',serif;font-size:2.2rem;background:linear-gradient(135deg,var(--gold),var(--gold-light));-webkit-background-clip:text;-webkit-text-fill-color:transparent;letter-spacing:2px; }
            .logo p { color:var(--muted);font-size:0.85rem;margin-top:6px;letter-spacing:3px;text-transform:uppercase; }
            .card { background:var(--card);border:1px solid var(--border);border-radius:16px;padding:40px;box-shadow:0 20px 60px rgba(0,0,0,0.5); }
            .card h2 { font-family:'Playfair Display',serif;font-size:1.6rem;margin-bottom:8px; }
            .card .subtitle { color:var(--muted);font-size:0.9rem;margin-bottom:32px; }
            .form-group { margin-bottom:18px; }
            .form-group label { display:block;font-size:0.78rem;letter-spacing:1px;text-transform:uppercase;color:var(--muted);margin-bottom:7px; }
            .form-group input, .form-group select { width:100%;background:rgba(255,255,255,0.04);border:1px solid var(--border);border-radius:10px;padding:13px 16px;color:var(--text);font-family:'DM Sans',sans-serif;font-size:0.95rem;outline:none;transition:border-color 0.2s,box-shadow 0.2s; }
            .form-group input:focus, .form-group select:focus { border-color:var(--gold);box-shadow:0 0 0 3px rgba(201,168,76,0.1); }
            .form-group select option { background:#13131a; }
            .btn { width:100%;padding:15px;background:linear-gradient(135deg,var(--gold),var(--gold-light));color:#0a0a0f;font-family:'DM Sans',sans-serif;font-size:0.95rem;font-weight:700;letter-spacing:1px;border:none;border-radius:10px;cursor:pointer;transition:opacity 0.2s,transform 0.1s;margin-top:8px; }
            .btn:hover { opacity:0.9;transform:translateY(-1px); }
            .footer-link { text-align:center;margin-top:24px;font-size:0.9rem;color:var(--muted); }
            .footer-link a { color:var(--gold);text-decoration:none;font-weight:500; }
            .alert { padding:12px 16px;border-radius:8px;font-size:0.88rem;margin-bottom:20px; }
            .alert-error { background:rgba(224,82,82,0.1);border:1px solid rgba(224,82,82,0.3);color:var(--error); }
            .alert-success { background:rgba(82,192,138,0.1);border:1px solid rgba(82,192,138,0.3);color:var(--success); }
            .divider { display:flex;align-items:center;gap:12px;margin:24px 0; }
            .divider hr { flex:1;border:none;border-top:1px solid var(--border); }
            .divider span { color:var(--muted);font-size:0.8rem; }
            .forgot-link { text-align:right;margin-top:-12px;margin-bottom:18px; }
            .forgot-link a { color:var(--muted);font-size:0.85rem;text-decoration:none; }
            .forgot-link a:hover { color:var(--gold); }
            .row2 { display:grid;grid-template-columns:1fr 1fr;gap:16px; }
        </style>
    """;

    public String loginPage() {
        return """
        <!DOCTYPE html><html lang="en">
        <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>Login — LPS</title>""" + STYLE + """
        </head><body>
        <div class="container">
            <div class="logo"><h1>LPS</h1><p>Texas Lottery Purchase System</p></div>
            <div class="card">
                <h2>Welcome back</h2>
                <p class="subtitle">Sign in to your account</p>
                <div id="alert-box"></div>
                <form method="POST" action="/login">
                    <div class="form-group"><label>Email Address</label>
                        <input type="email" name="email" placeholder="you@example.com" required autofocus /></div>
                    <div class="form-group"><label>Password</label>
                        <input type="password" name="password" placeholder="••••••••" required /></div>
                    <div class="forgot-link"><a href="/forgot-password">Forgot password?</a></div>
                    <button class="btn" type="submit">Sign In</button>
                </form>
                <div class="divider"><hr/><span>or</span><hr/></div>
                <div class="footer-link">Don't have an account? <a href="/register">Create one</a></div>
            </div>
        </div>
        <script>
            const p = new URLSearchParams(window.location.search), b = document.getElementById('alert-box');
            if (p.get('error')) b.innerHTML = '<div class="alert alert-error">' + decodeURIComponent(p.get('error')) + '</div>';
            if (p.get('success')) b.innerHTML = '<div class="alert alert-success">' + decodeURIComponent(p.get('success')) + '</div>';
        </script>
        </body></html>""";
    }

    public String registerPage() {
        return """
        <!DOCTYPE html><html lang="en">
        <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>Register — LPS</title>""" + STYLE + """
        </head><body>
        <div class="container" style="max-width:500px">
            <div class="logo"><h1>LPS</h1><p>Texas Lottery Purchase System</p></div>
            <div class="card">
                <h2>Create account</h2>
                <p class="subtitle">You must be 18 or older to register</p>
                <div id="alert-box"></div>
                <form method="POST" action="/register" onsubmit="return checkAge()">
                    <div class="form-group"><label>Full Name</label>
                        <input type="text" name="name" placeholder="John Doe" required /></div>
                    <div class="form-group"><label>Email Address</label>
                        <input type="email" name="email" placeholder="you@example.com" required /></div>
                    <div class="form-group"><label>Phone Number</label>
                        <input type="tel" name="phone" placeholder="+1 555 000 0000" /></div>
                    <div class="form-group"><label>Date of Birth</label>
                        <input type="date" name="birthday" id="birthday" required /></div>
                    <div class="form-group"><label>Password</label>
                        <input type="password" name="password" placeholder="Choose a strong password" required /></div>
                    <div id="age-error" class="alert alert-error" style="display:none;">
                        You must be at least 18 years old to play the Texas Lottery.
                    </div>
                    <button class="btn" type="submit">Create Account</button>
                </form>
                <div class="divider"><hr/><span>or</span><hr/></div>
                <div class="footer-link">Already have an account? <a href="/login">Sign in</a></div>
            </div>
        </div>
        <script>
            const p = new URLSearchParams(window.location.search), b = document.getElementById('alert-box');
            if (p.get('error')) b.innerHTML = '<div class="alert alert-error">' + decodeURIComponent(p.get('error')) + '</div>';

            function checkAge() {
                const dob = new Date(document.getElementById('birthday').value);
                const today = new Date();
                let age = today.getFullYear() - dob.getFullYear();
                const m = today.getMonth() - dob.getMonth();
                if (m < 0 || (m === 0 && today.getDate() < dob.getDate())) age--;
                if (age < 18) {
                    document.getElementById('age-error').style.display = 'block';
                    window.scrollTo(0, document.body.scrollHeight);
                    return false;
                }
                return true;
            }
        </script>
        </body></html>""";
    }

    public String forgotPasswordPage() {
        return """
        <!DOCTYPE html><html lang="en">
        <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>Forgot Password — LPS</title>""" + STYLE + """
        </head><body>
        <div class="container">
            <div class="logo"><h1>LPS</h1><p>Texas Lottery Purchase System</p></div>
            <div class="card">
                <h2>Forgot password?</h2>
                <p class="subtitle">Enter your email and we'll send a reset link</p>
                <div id="alert-box"></div>
                <form method="POST" action="/forgot-password">
                    <div class="form-group"><label>Email Address</label>
                        <input type="email" name="email" placeholder="you@example.com" required autofocus /></div>
                    <button class="btn" type="submit">Send Reset Link</button>
                </form>
                <div class="footer-link" style="margin-top:20px;"><a href="/login">← Back to login</a></div>
            </div>
        </div>
        <script>
            const p = new URLSearchParams(window.location.search), b = document.getElementById('alert-box');
            if (p.get('error'))   b.innerHTML = '<div class="alert alert-error">'   + decodeURIComponent(p.get('error'))   + '</div>';
            if (p.get('success')) b.innerHTML = '<div class="alert alert-success">' + decodeURIComponent(p.get('success')) + '</div>';
        </script>
        </body></html>""";
    }

    public String resetPasswordPage(String token) {
        return """
        <!DOCTYPE html><html lang="en">
        <head><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1.0">
        <title>Reset Password — LPS</title>""" + STYLE + """
        </head><body>
        <div class="container">
            <div class="logo"><h1>LPS</h1><p>Texas Lottery Purchase System</p></div>
            <div class="card">
                <h2>Reset password</h2>
                <p class="subtitle">Choose a new password</p>
                <div id="alert-box"></div>
                <form method="POST" action="/reset-password" onsubmit="return checkMatch()">
                    <input type="hidden" name="token" value=\"""" + token + """
                    " />
                    <div class="form-group"><label>New Password</label>
                        <input type="password" name="password" id="pw" placeholder="New password" required /></div>
                    <div class="form-group"><label>Confirm Password</label>
                        <input type="password" name="confirm" id="cf" placeholder="Repeat password" required /></div>
                    <button class="btn" type="submit">Reset Password</button>
                </form>
            </div>
        </div>
        <script>
            function checkMatch() {
                if (document.getElementById('pw').value !== document.getElementById('cf').value) {
                    document.getElementById('alert-box').innerHTML =
                        '<div class="alert alert-error">Passwords do not match.</div>';
                    return false;
                }
                return true;
            }
        </script>
        </body></html>""";
    }
}