package com.itteam.estatesapi.email;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@AllArgsConstructor
public class EmailService implements EmailSender{

    private final static Logger LOGGER = LoggerFactory
            .getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm your email");
            helper.setFrom("mailtrap@demomailtrap.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("failed to send email", e);
            throw new IllegalStateException("failed to send email");
        }
    }

    public String buildEmail(String name, String link) {
        return "<div style=\"font-family: Arial, sans-serif; font-size: 16px; color: #333; margin: 0; padding: 20px; background-color: #f4f4f4;\">\n" +
                "  <div style=\"max-width: 600px; margin: auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);\">\n" +
                "    <div style=\"text-align: center;\">\n" +
                "      <h1 style=\"font-size: 24px; margin-bottom: 10px; color: #333;\">Confirm your email</h1>\n" +
                "      <p style=\"font-size: 18px; margin-bottom: 20px; color: #555;\">Hi " + name + ",</p>\n" +
                "    </div>\n" +
                "    <p style=\"font-size: 16px; line-height: 1.5; color: #555;\">Thank you for registering. Please click on the link below to activate your account:</p>\n" +
                "    <div style=\"text-align: center; margin: 20px 0;\">\n" +
                "      <a href=\"" + link + "\" style=\"font-size: 16px; color: #fff; background-color: #1D70B8; padding: 10px 20px; text-decoration: none; border-radius: 5px;\">Activate Now</a>\n" +
                "    </div>\n" +
                "    <p style=\"font-size: 16px; line-height: 1.5; color: #555;\">Link will expire in 15 minutes.</p>\n" +
                "    <p style=\"font-size: 16px; line-height: 1.5; color: #555;\">See you soon,</p>\n" +
                "    <p style=\"font-size: 16px; line-height: 1.5; color: #555;\">The Team of Inamkhojayev Timur and Daurbekov Ramazan</p>\n" +
                "  </div>\n" +
                "</div>";
    }

}
