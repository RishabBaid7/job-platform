package com.jobplatform.notification_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.from-email}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApplicationConfirmation(String toEmail, Long applicationId, Long jobId) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Application Received - Job Platform");
            message.setText(
                "Hi,\n\n" +
                "Your application (ID: " + applicationId + ") for Job #" + jobId + " has been received successfully.\n\n" +
                "We will review your application and get back to you soon.\n\n" +
                "Best regards,\nJob Platform Team"
            );
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
        }
    }

    public void sendStatusUpdateNotification(String toEmail, Long applicationId, String newStatus) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Application Status Updated - Job Platform");
            message.setText(
                "Hi,\n\n" +
                "Your application (ID: " + applicationId + ") status has been updated to: " + newStatus + "\n\n" +
                "Best regards,\nJob Platform Team"
            );
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Failed to send status email to " + toEmail + ": " + e.getMessage());
        }
    }
}
