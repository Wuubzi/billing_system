package com.billing_system.notification.Services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendRestPasswordEmail(String to, String code){
        // HTML Content of the Email
        String emailContent = "<html><body style='font-family: Arial, sans-serif;'>" +
                "<div style='max-width: 600px; margin: 0 auto; padding: 20px; background-color: #f4f4f9; border-radius: 8px;'>" +
                "<h2 style='color: #333;'>Password Reset Request</h2>" +
                "<p>Hello!</p>" +
                "<p>We received a request to reset your password. If you made this request, please use the following code:</p>" +
                "<div style='background-color: #e0f7fa; padding: 10px; border-radius: 5px; font-size: 18px; color: #00796b;'>" +
                "<strong>Your Reset Code:</strong> " + code + "</div>" +
                "<p>If you did not request this change, please disregard this message. Your account remains secure.</p>" +
                "<p>Thank you for using our system! If you have any questions, feel free to contact our support team.</p>" +
                "<footer style='font-size: 12px; color: #888;'>This is an automated message. Please do not reply directly.</footer>" +
                "</div></body></html>";

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            String email = "reservavuelos9@gmail.com";
            helper.setFrom(email);
            helper.setTo(to);
            helper.setSubject("Password Reset Request");
            helper.setText(emailContent, true);  // true indicates HTML content
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
