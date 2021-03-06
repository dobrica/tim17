package com.payment.paypal.web;

import com.payment.paypal.payment.ExecutePayment;
import com.payment.paypal.payment.PaymentRequest;
import com.payment.paypal.payment.PaymentResponse;
import com.payment.paypal.payment.PaymentService;
import com.payment.paypal.subscription.BillingPlanRequest;
import com.payment.paypal.subscription.ExecuteSubscription;
import com.payment.paypal.subscription.SubscriptionRequest;
import com.payment.paypal.subscription.SubscriptionService;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/paypal")
@AllArgsConstructor
public class PaypalController {

	private final PaymentService paymentService;
	private final SubscriptionService subscriptionService;

	@PostMapping(value = "/payment/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<PaymentResponse> createPayment(@RequestBody final PaymentRequest paymentRequest) throws PayPalRESTException {
		//TODO Kako jedinstveno identifikovati placanje. Payment id?
		return new ResponseEntity<>(paymentService.createPayment(paymentRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/payment/success")
	public ResponseEntity<String> paymentSuccessful(@RequestParam final String paymentId, @RequestParam final String token,
		@RequestParam("PayerID") final String payerId) {
		log.info("Success payment from {}, with payment id {} and token {}", payerId, paymentId, token);
		return new ResponseEntity<>("Success!", HttpStatus.OK);
	}

	@GetMapping(value = "/payment/cancel")
	public ResponseEntity<String> paymentCanceled(@RequestParam final String token) {
		log.info("Canceled with token {}", token);
		return new ResponseEntity<>("Canceled!", HttpStatus.OK);
	}

	@PostMapping(value = "/payment/execute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> executePayment(@RequestBody final ExecutePayment executePayment) throws PayPalRESTException {
		return new ResponseEntity<>(paymentService.executePayment(executePayment).toJSON(), HttpStatus.OK);
	}

	@PostMapping(value = "/billing-plan/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createBillingPlan(@RequestBody final BillingPlanRequest billingPlanRequest) {
		return new ResponseEntity<>(subscriptionService.createBillingPlan(billingPlanRequest), HttpStatus.OK);
	}

	@PostMapping(value = "/subscription/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> createSubscription(@RequestBody final SubscriptionRequest subscriptionRequest) {
		return new ResponseEntity<>(subscriptionService.createSubscription(subscriptionRequest), HttpStatus.OK);
	}

	@GetMapping(value = "/subscription/success")
	public ResponseEntity<String> subscriptionSuccessful(@RequestParam final String token) {
		log.info("Success subscription with token {}", token);
		return new ResponseEntity<>("Success!", HttpStatus.OK);
	}

	@GetMapping(value = "/subscription/cancel")
	public ResponseEntity<String> subscriptionCanceled(@RequestParam final String token) {
		log.info("Canceled subscription for token {}", token);
		return new ResponseEntity<>("Canceled!", HttpStatus.OK);
	}

	@PostMapping(value = "/subscription/execute", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> executeSubscription(@RequestBody final ExecuteSubscription executeSubscription) {
		return new ResponseEntity<>(subscriptionService.executeSubscription(executeSubscription), HttpStatus.OK);
	}

}
