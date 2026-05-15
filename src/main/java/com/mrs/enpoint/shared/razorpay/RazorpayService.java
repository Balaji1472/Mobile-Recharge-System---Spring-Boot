
package com.mrs.enpoint.shared.razorpay;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class RazorpayService {

	private final RazorpayClient razorpayClient;

	@Value("${razorpay.key.secret}")
	private String keySecret;

	public RazorpayService(RazorpayClient razorpayClient) {
		this.razorpayClient = razorpayClient;
	}

	public String createOrder(BigDecimal amount, String receiptId) {
		try {
			int amountInPaise = amount.multiply(BigDecimal.valueOf(100)).intValue();

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amountInPaise);
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "receipt_" + receiptId);

			Order order = razorpayClient.orders.create(orderRequest);
			return order.get("id");

		} catch (RazorpayException e) {
			throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
		}
	}

	public boolean verifyPaymentSignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) {
		try {
			String data = razorpayOrderId + "|" + razorpayPaymentId;

			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec secretKey = new SecretKeySpec(keySecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(secretKey);
			byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

			StringBuilder hexString = new StringBuilder();
			for (byte b : hash) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString().equals(razorpaySignature);

		} catch (Exception e) {
			throw new RuntimeException("Payment signature verification failed", e);
		}
	}
}