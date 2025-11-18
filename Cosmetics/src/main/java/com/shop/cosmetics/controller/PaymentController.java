package com.shop.cosmetics.controller;

import com.paypal.base.rest.PayPalRESTException;
import com.shop.cosmetics.response.ApiResponse;
import com.shop.cosmetics.service.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    PaymentService paymentService;

    // === API CHO POSTMAN (giữ nguyên) ===
    @GetMapping("/create/{orderId}")
    public ApiResponse<String> createPayment(@PathVariable Long orderId) throws PayPalRESTException {
        return ApiResponse.<String>builder()
                .result(paymentService.createPayment(orderId))
                .build();
    }

    // === TRANG SUCCESS (HTML) – DÙNG KHI PAYPAL REDIRECT ===
    @GetMapping(value = "/success", produces = "text/html")
    public ResponseEntity<String> successPage(
            @RequestParam String paymentId,
            @RequestParam ("PayerID") String payerId) throws PayPalRESTException {

        paymentService.executePayment(paymentId, payerId);

        String html = """
            <!DOCTYPE html>
            <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Payment success</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background: linear-gradient(135deg, #667eea, #764ba2);
                        color: #fff;
                        text-align: center;
                        padding: 50px 20px;
                        margin: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: rgba(255, 255, 255, 0.1);
                        padding: 40px;
                        border-radius: 15px;
                        box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                    }
                    h1 {
                        font-size: 36px;
                        margin-bottom: 20px;
                        color: #a8ff78;
                    }
                    p {
                        font-size: 18px;
                        margin: 15px 0;
                    }
                    .transaction-id {
                        font-family: monospace;
                        background: rgba(0,0,0,0.2);
                        padding: 8px 15px;
                        border-radius: 8px;
                        display: inline-block;
                        margin: 10px 0;
                        font-weight: bold;
                    }
                    .btn {
                        display: inline-block;
                        margin-top: 30px;
                        padding: 12px 30px;
                        background: #a8ff78;
                        color: #333;
                        text-decoration: none;
                        font-weight: bold;
                        border-radius: 50px;
                        transition: 0.3s;
                    }
                    .btn:hover {
                        background: #8fe65f;
                        transform: scale(1.05);
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Payment success!</h1>
                    <p>Thanks for your believe <strong>Cosmetics Shop</strong></p>
                    <p>Transaction code PayPal:</p>
                    <div class="transaction-id">%s</div>
                    <p>Your order is processing.</p>
                    <a href="/" class="btn">Go to homepage</a>
                </div>
            </body>
            </html>
            """.formatted(paymentId);

        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

    // === TRANG CANCEL (HTML) ===
    @GetMapping(value = "/cancel", produces = "text/html")
    public ResponseEntity<String> cancelPage() {
        String html = """
            <!DOCTYPE html>
            <html lang="vi">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Order cancellation</title>
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        background: linear-gradient(135deg, #ff6b6b, #ee5a52);
                        color: #fff;
                        text-align: center;
                        padding: 50px 20px;
                        margin: 0;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: rgba(255, 255, 255, 0.1);
                        padding: 40px;
                        border-radius: 15px;
                        box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                    }
                    h1 {
                        font-size: 36px;
                        margin-bottom: 20px;
                        color: #ffcccc;
                    }
                    .btn {
                        display: inline-block;
                        margin-top: 30px;
                        padding: 12px 30px;
                        background: #fff;
                        color: #e74c3c;
                        text-decoration: none;
                        font-weight: bold;
                        border-radius: 50px;
                        transition: 0.3s;
                    }
                    .btn:hover {
                        background: #f1f1f1;
                        transform: scale(1.05);
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <h1>Payment cancel</h1>
                    <a href="/" class="btn">Go back </a>
                </div>
            </body>
            </html>
            """;

        return ResponseEntity.ok()
                .header("Content-Type", "text/html; charset=UTF-8")
                .body(html);
    }

    @GetMapping(value = "/success", params = {"paymentId", "payerId"}, produces = "application/json")
    public ApiResponse<String> successJson(
            @RequestParam String paymentId,
            @RequestParam String payerId) throws PayPalRESTException {
        paymentService.executePayment(paymentId, payerId);
        return ApiResponse.<String>builder()
                .result("Payment success")
                .build();
    }

    @GetMapping(value = "/cancel", produces = "application/json")
    public ApiResponse<String> cancelJson() {
        return ApiResponse.<String>builder()
                .result("Payment cancelled")
                .build();
    }
}