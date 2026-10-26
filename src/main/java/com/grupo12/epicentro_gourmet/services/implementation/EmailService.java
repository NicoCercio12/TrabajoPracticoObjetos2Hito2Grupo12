package com.grupo12.epicentro_gourmet.services.implementation;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String remitente;

    public EmailService(JavaMailSender mailSender, @Value("${spring.mail.username}") String remitente) {

        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    public void enviarCredenciales(String destinatario, String nombre, String passwordGenerada){

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Tu cuenta de Epicentro Gourmet");

        String contenido = "Hola, " + nombre + ".\n\n" + "Se creó tu cuenta de empleado en Epicentro Gourmet.\n\n" + "Usuario: " + destinatario + "\n" + "Contraseña: " + passwordGenerada + "\n\n" + "Usá estos datos para iniciar sesión en la aplicación.\n\n" + "Saludos,\n" + "Epicentro Gourmet";


        mensaje.setText(contenido);

        mailSender.send(mensaje);
    }

}


