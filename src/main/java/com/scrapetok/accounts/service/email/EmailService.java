package com.scrapetok.accounts.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendWelcomeEmail(String to, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("👋 ¡Bienvenido a ScrapeTok! 🎉");
        
        String body = String.format("""
            Hola %s 😊,
            
            ¡Bienvenido a ScrapeTok! 🚀
            
            Estamos emocionados de tenerte a bordo. Tu cuenta ha sido creada exitosamente.
            
            ¡Disfruta explorando TikTok insights!
            
            Saludos,
            El equipo de ScrapeTok
            """, firstName);
        
        message.setText(body);
        
        try {
            mailSender.send(message);
            System.out.println("Email de bienvenida enviado a: " + to);
        } catch (Exception e) {
            System.err.println("Error enviando email de bienvenida: " + e.getMessage());
        }
    }
    
    public void sendNotificationEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        
        try {
            mailSender.send(message);
            System.out.println("Email de notificación enviado a: " + to);
        } catch (Exception e) {
            System.err.println("Error enviando email de notificación: " + e.getMessage());
        }
    }
}
