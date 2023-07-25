package com.example.demo.controllers;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

import static com.twilio.example.ValidationExample.ACCOUNT_SID;
import static com.twilio.example.ValidationExample.AUTH_TOKEN;

@Controller
@Slf4j
public class PhoneNumberVerificationController {
    @GetMapping("/generateOTP")
    public String generateOTP(){

        Twilio.init("ACd20cff29460f8a65997087a81b68141e","e7dbc249e080bbb7ba00d905b5f84a7f");

        Verification verification = Verification.creator(
                        "VA954765dc76826bc3895c459da6744b6f", // this is your verification sid
                        "+79530994567", //this is your Twilio verified recipient phone number
                        "sms") // this is your channel type
                .create();

        System.out.println(verification.getStatus());

        log.info("OTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());

        return "redirect:/login";
    }
    @GetMapping("/verifyOTP")
    public ResponseEntity<?> verifyUserOTP() throws Exception {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        try {
            Random random = new Random();
            random.nextInt(1000,9999);

            VerificationCheck verificationCheck = VerificationCheck.creator(
                            "VA954765dc76826bc3895c459da6744b6f")
                    .setTo("+79530994567")
                    .setCode(random.toString())
                    .create();

            System.out.println(verificationCheck.getStatus());

        } catch (Exception e) {
            return new ResponseEntity<>("Verification failed.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("This user's verification has been completed successfully", HttpStatus.OK);
    }
}
