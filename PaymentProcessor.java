import java.util.Map;

public class PaymentProcessor {
    public boolean processPayment(String userEmail, Map<String, Object> paymentInfo, float amount) {
        if (paymentInfo == null || amount <= 0) return false;
        // TODO: integrate real payment gateway (e.g. Stripe Java SDK)
        return true;
    }
}