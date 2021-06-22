package bo.dynamicworkflow.dynamicworkflowbackend.app.notification.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.NotificationService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing.MailingService;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private MailingService mailingService;

    @Override
    public void sendRequestingUserSignUpNotification(User user) {
        try {
            List<String> addressees = new ArrayList<String>() {{
                add(user.getEmail());
            }};
            String subject = "Confirmación de registro de usuario";
            String message = "Estimad@ Usuari@," + "\n" +
                    "Le damos la bienvenida a nuestra plataforma de control de flujo de solicitudes." + "\n" +
                    "Se registró la cuenta con la siguiente información:" + "\n" +
                    "\tNombre: " + user.fullName() + "\n" +
                    "\tEmail: " + user.getEmail() + "\n" +
                    "\tTeléfono: " + user.getPhoneNumber() + "\n" +
                    EMAIL_MESSAGE_FOOTER;
            mailingService.sendEmail(addressees, subject, message);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendRestorePasswordNotification(String userEmail, String password) {
        try {
            List<String> addressees = new ArrayList<String>() {{
                add(userEmail);
            }};
            String subject = "Restablecer clave de acceso";
            String message = "Estimad@ Usuari@," + "\n" +
                    "Su clave de acceso temporal es: " + password + "\n" +
                    EMAIL_MESSAGE_FOOTER;
            mailingService.sendEmail(addressees, subject, message);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendUpdatedPasswordNotification(String userEmail) {
        try {
            List<String> addressees = new ArrayList<String>() {{
                add(userEmail);
            }};
            String subject = "Clave de acceso actualizada";
            String message = "Estimad@ Usuari@," + "\n" +
                    "Su clave de ingreso al sistema se ha actualizado exitosamente." + "\n" +
                    EMAIL_MESSAGE_FOOTER;
            mailingService.sendEmail(addressees, subject, message);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    private static final String EMAIL_MESSAGE_FOOTER = "Saludos cordiales." + "\n\n" + "El equipo de DYNAMIC WORKFLOW";

}