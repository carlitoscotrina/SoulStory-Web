package pe.edu.upc.soulstoryapi.serviceimpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import pe.edu.upc.soulstoryapi.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String correoOrigen;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void enviarRecuperacion(
            String destinatario,
            String enlaceRecuperacion) {

        SimpleMailMessage mensaje = new SimpleMailMessage();

        mensaje.setFrom(correoOrigen);
        mensaje.setTo(destinatario);
        mensaje.setSubject(
                "SoulStory - Recuperación de contraseña"
        );

        mensaje.setText(
                "Hola,\n\n" +
                        "Has solicitado restablecer tu contraseña en SoulStory.\n\n" +
                        "Ingresa al siguiente enlace:\n" +
                        enlaceRecuperacion +
                        "\n\nEste enlace tiene una vigencia de 15 minutos.\n\n" +
                        "Si no realizaste esta solicitud, puedes ignorar este mensaje."
        );

        mailSender.send(mensaje);
    }
}