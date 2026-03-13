package com.jobplatform.notification_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

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
            log.info("Application confirmation sent to {} for application {}", toEmail, applicationId);
        } catch (MailException e) {
            log.error("Failed to send application confirmation to {}: {}", toEmail, e.getMessage());
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
            log.info("Status update email sent to {} for application {} -> {}", toEmail, applicationId, newStatus);
        } catch (MailException e) {
            log.error("Failed to send status update email to {}: {}", toEmail, e.getMessage());
        }
    }

    public void sendJobPostedConfirmation(String toEmail, Long jobId, String title, String companyName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Job Posted Successfully - Job Platform");
            message.setText(
                "Hi,\n\n" +
                "Your job posting has been published successfully.\n\n" +
                "Job ID: " + jobId + "\n" +
                "Title: " + title + "\n" +
                "Company: " + companyName + "\n\n" +
                "Candidates can now apply for this position.\n\n" +
                "Best regards,\nJob Platform Team"
            );
            mailSender.send(message);
            log.info("Job posted confirmation sent to {} for job {}", toEmail, jobId);
        } catch (MailException e) {
            log.error("Failed to send job posted confirmation to {}: {}", toEmail, e.getMessage());
        }
    }
}
