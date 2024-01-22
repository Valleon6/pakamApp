package com.valleon.pakamapp.modules.authentication.service;

import com.valleon.pakamapp.modules.customer.entity.Customer;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private Environment env;

    private final OkHttpClient client;
    @Autowired
    private JavaMailSender mailSender;

    public EmailService() {
        this.client = new OkHttpClient().newBuilder().build();
    }

    private SimpleMailMessage constructCustomerEmail(String subject, String body, Customer customer) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(customer.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

//    private SimpleMailMessage constructAdminEmail(String subject, String body,
//                                                  String adminEmail) {
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setSubject(subject);
//        email.setText(body);
//        email.setTo(adminEmail);
//        email.setFrom(env.getProperty("support.email"));
//        return email;
//    }


    public void sendRegistrationEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        String subject = "Registration Completed";
        String body = "Dear " + name + "," + ",\n" +
                "Congratulations you have been registered Successfully. " + ",\n" +
                "Click the url to redirect you to the login page if you are not redirect automatically";
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    public SimpleMailMessage constructResetTokenEmail(String apiPath, String token, Customer customer) {
        String url = apiPath + token;
        String message = "\n" +
                "Hi " + customer.getFirstName() + ",\n" +
                "\n" +
                "There was a request to change your password!\n" +
                "\n" +
                "If you did not make this request then please ignore this email.\n" +
                "\n" +
                "Otherwise, please click this link to change your password: ";
        return constructCustomerEmail("Reset Password", message + " \r\n" + url, customer);
    }

    public SimpleMailMessage constructValidationEmail(
            String contextPath, String apiPath, String token, Customer customer) {
        String url = contextPath + apiPath + token;
        String message = "\n" +
                "Hi " + customer.getFirstName() + ",\n" +
                "\n" +
                "Thank you for signing up, we’re thrilled to have you here\n" +
                "\n" +
                "On Pakam app, you can create, update and delete your trash assessments.\n" +
                "If you did not make this request then please ignore this email.\n" +
                "\n" +
                "Otherwise, please click this link to activate your email: ";
        return constructCustomerEmail("Validate Email for Pakam", message + " \r\n" + url, customer);
    }

}
