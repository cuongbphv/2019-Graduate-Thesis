package com.graduate.thesis.backend.controller;

import javax.servlet.http.HttpServletRequest;

import com.graduate.thesis.backend.config.PaypalPaymentIntent;
import com.graduate.thesis.backend.config.PaypalPaymentMethod;
import com.graduate.thesis.backend.entity.Account;
import com.graduate.thesis.backend.exception.ApplicationException;
import com.graduate.thesis.backend.model.response.RestAPIResponse;
import com.graduate.thesis.backend.security.CurrentUser;
import com.graduate.thesis.backend.security.oauth2.user.UserPrincipal;
import com.graduate.thesis.backend.service.AccountService;
import com.graduate.thesis.backend.service.PaymentService;
import com.graduate.thesis.backend.service.paypal.PaypalService;
import com.graduate.thesis.backend.util.APIStatus;
import com.graduate.thesis.backend.util.CommonUtil;
import com.graduate.thesis.backend.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Date;
import java.util.List;

/**
 * @author Huy Pham
 */

@RestController
@RequestMapping(Constant.PAYMENT_API)
public class PaymentController extends AbstractBasedAPI{

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PaypalService paypalService;

    @Autowired
    PaymentService paymentService;

    @Autowired
    AccountService accountService;


    @GetMapping()
    public ResponseEntity<RestAPIResponse> getAccountBalance(
            @CurrentUser UserPrincipal currentUser
    ){
        Account account = accountService.findByUserId(currentUser.getId());
        if(account == null){
            throw new ApplicationException(APIStatus.ERR_USER_NOT_FOUND);
        }

        return responseUtil.successResponse(account.getBalance());
    }

    @GetMapping("/history")
    public ResponseEntity<RestAPIResponse> getPaymentHistory(
            @CurrentUser UserPrincipal currentUser
    ){

        List<com.graduate.thesis.backend.entity.Payment> payments =
                paymentService.findByUserIdAndStatus(currentUser.getId(), "success");

        return responseUtil.successResponse(payments);
    }

    @PostMapping()
    public ResponseEntity<RestAPIResponse> pay(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam("price") double price,
            @RequestParam("coin") int coin)
    {

        com.graduate.thesis.backend.entity.Payment pay = new com.graduate.thesis.backend.entity.Payment();
        pay.setUserId(currentUser.getId());
        pay.setAmount(price);
        pay.setBeeCoin(coin);
        pay.setType("buy");
        pay.setCreatedDate(new Date());
        pay.setStatus("pending");
        com.graduate.thesis.backend.entity.Payment created = paymentService.save(pay);

        String cancelUrl = "http://localhost:4040/payment";
        String successUrl = "http://localhost:4040/payment/confirm?id=" + created.getId();
        try {
            Payment payment = paypalService.createPayment(
                    price,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "Buy bee coin",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return responseUtil.successResponse(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return responseUtil.successResponse("OK");
    }

    @GetMapping(Constant.SUCCESS)
    public ResponseEntity<RestAPIResponse> successPay(
            @CurrentUser UserPrincipal currentUser,
            @RequestParam("paymentId") String paymentId,
            @RequestParam("payerId") String payerId,
            @RequestParam("id") String id
            ){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){

                com.graduate.thesis.backend.entity.Payment pay = paymentService.findById(id);
                if(pay != null){
                    Account account = accountService.findByUserId(currentUser.getId());
                    account.setBalance(account.getBalance() + pay.getBeeCoin());
                    pay.setStatus("success");
                    accountService.save(account);
                    paymentService.save(pay);

                    return responseUtil.successResponse("OK");
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return responseUtil.successResponse("FAILED");
    }

}
