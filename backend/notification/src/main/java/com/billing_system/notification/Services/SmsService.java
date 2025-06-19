package com.billing_system.notification.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
  private String twilioPhoneNumber = "+13305835239";

  @PostConstruct
  public void initTwilio() {
    Twilio.init("AC314452e027127811dc8c2b256beb907a", "3b66529a3d17d3d4c29112edb11103cb");
  }

  public String sendSms(String destinationPhone, String messageText) {

    if (!destinationPhone.startsWith("+")) {
      destinationPhone = "+57" + destinationPhone;
    }
    Message message = Message.creator(
        new PhoneNumber(destinationPhone),
        new PhoneNumber(twilioPhoneNumber),
        messageText)
        .create();
    return message.getSid();
  }
}
