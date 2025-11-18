package com.shop.cosmetics.service;
import com.paypal.api.payments.*;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.shop.cosmetics.entity.Order;
import com.shop.cosmetics.entity.Payment;
import com.shop.cosmetics.enums.Status;
import com.shop.cosmetics.repository.OrderRepository;
import com.shop.cosmetics.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    APIContext apiContext;

    public String createPayment(Long orderId) throws PayPalRESTException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getStatus() == Status.CANCELLED) {
            throw new RuntimeException("Your order is cancelled. You can not do payment");
        }
        if (order.getStatus() == Status.PAID) {
            throw new RuntimeException("payment success");
        }
        if (order.getStatus() == Status.SHIPPING || order.getStatus() == Status.SUCCESS) {
            throw new RuntimeException("Order is processing. You can not do payment");
        }

        if (order.getStatus() != Status.PENDING) {
            order.setStatus(Status.PENDING);
        }
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(String.format("%.2f", order.getTotalPrice()));

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment for order #" + order.getOrderId());
        transaction.setAmount(amount);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/payments/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/payments/success");

        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(List.of(transaction));
        payment.setRedirectUrls(redirectUrls);

        com.paypal.api.payments.Payment createdPayment = payment.create(apiContext);


        Payment entityPayment = paymentRepository.findByOrder(order);
        entityPayment.setTransactionId(createdPayment.getId());
        entityPayment.setStatus(Status.PENDING);
        order.setStatus(Status.PENDING);
        orderRepository.save(order);
        paymentRepository.save(entityPayment);

        return createdPayment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .findFirst()
                .map(Links::getHref)
                .orElseThrow(() -> new RuntimeException("No paypal approval url found"));

    }

    @Transactional
    public void executePayment(String paymentId, String payerId) throws PayPalRESTException {
        com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
        payment.setId(paymentId);
        PaymentExecution execution = new PaymentExecution();
        execution.setPayerId(payerId);

        com.paypal.api.payments.Payment executed = payment.execute(apiContext, execution);

        if (!"approved".equals(executed.getState())) {
            throw new RuntimeException("Payment not approved: " + executed.getState());
        }

        String txnId = executed.getId();

        Payment dbPayment = paymentRepository.findByTransactionId(txnId);

        Order order = dbPayment.getOrder();
        if (order == null) {
            throw new RuntimeException("Order not found for payment: " + dbPayment.getPaymentId());
        }

        if (order.getStatus() == Status.CANCELLED) {
            throw new RuntimeException("Your order is cancelled. You can not do payment");
        }

        dbPayment.setStatus(Status.SUCCESS);
        order.setStatus(Status.PAID);

        orderRepository.save(order);
    }
}
