package com.itteam.estatesapi.paypal;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

//@RestController
//@RequiredArgsConstructor
//@Slf4j
//@RequestMapping("/payment")
//
//public class PaypalController {
//
//    private final PaypalService paypalService;
//
//    @Value("${spring.application.actual-ui-domain}")
//    private String ACTUAL_UI_DOMAIN;
//
//    @PostMapping("/create")
//    public ResponseEntity<String> createPayment(
//            @RequestParam("method") String method
//    ) {
//        try {
//            String cancelUrl = "http://" + ACTUAL_UI_DOMAIN + "/payment/cancel";
//            String successUrl = "http://" + ACTUAL_UI_DOMAIN + "/payment/success";
//            String amount = "50.00";
//            String currency = "USD";
//            String description = "Consulting and Tour";
//            Payment payment = paypalService.createPayment(
//                    Double.valueOf(amount),
//                    currency,
//                    method,
//                    "sale",
//                    description,
//                    cancelUrl,
//                    successUrl
//            );
//
//            for (Links links: payment.getLinks()) {
//                if (links.getRel().equals("approval_url")) {
//                    return ResponseEntity.status(HttpStatus.FOUND).body(links.getHref());
//                }
//            }
//        } catch (PayPalRESTException e) {
//            log.error("Error occurred:: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment creation");
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment creation");
//    }
//
//    @GetMapping("/success")
//    public ResponseEntity<String> paymentSuccess(
//            @RequestParam("paymentId") String paymentId,
//            @RequestParam("PayerID") String payerId
//    ) {
//        try {
//            Payment payment = paypalService.executePayment(paymentId, payerId);
//            if (payment.getState().equals("approved")) {
//                return ResponseEntity.ok("Success! Your payment is done!");
//            }
//        } catch (PayPalRESTException e) {
//            log.error("Error occurred:: ", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment execution");
//        }
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during payment execution");
//    }
//
//    @GetMapping("/cancel")
//    public ResponseEntity<String> paymentCancel() {
//        return ResponseEntity.ok("Your Payment processing on PayPal has been canceled");
//    }
//
//    @GetMapping("/error")
//    public ResponseEntity<String> paymentError() {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment error");
//    }
//}

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class PaypalController {

    private final PaypalService paypalService;

    @Value("${spring.application.actual-ui-domain}")
    private String ACTUAL_UI_DOMAIN;

    @PostMapping("/create")
    public String createPayment() {
        try {
            String cancelUrl = "http://" + ACTUAL_UI_DOMAIN + "/payment/cancel";
            String successUrl = "http://" + ACTUAL_UI_DOMAIN + "/payment/success";
            double amount = 50.00;
            String method = "paypal";
            String currency = "USD";
            String description = "Consulting and Tour";
            Payment payment = paypalService.createPayment(
                    amount,
                    currency,
                    method,
                    "sale",
                    description,
                    cancelUrl,
                    successUrl
            );

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    log.info(links.getHref());
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return "Error occurred during payment creation";
        }
        return "Error occurred during payment creation";
    }

    @GetMapping("/success")
    public void paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            HttpServletResponse response
    ) throws IOException {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                response.sendRedirect("http://" + ACTUAL_UI_DOMAIN + "/payment/success");
            } else {
                response.sendRedirect("http://" + ACTUAL_UI_DOMAIN + "/payment/cancel");
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            response.sendRedirect("http://" + ACTUAL_UI_DOMAIN + "/payment/error");
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Your Payment processing on PayPal has been canceled");
    }

    @GetMapping("/error")
    public ResponseEntity<String> paymentError() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment error");
    }
}
