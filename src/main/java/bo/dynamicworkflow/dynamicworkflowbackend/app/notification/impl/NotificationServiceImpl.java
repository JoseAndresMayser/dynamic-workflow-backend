package bo.dynamicworkflow.dynamicworkflowbackend.app.notification.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.NotificationService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing.Attachment;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing.MailingService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.IoUtility;
import org.apache.commons.mail.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final MailingService mailingService;

    @Autowired
    public NotificationServiceImpl(MailingService mailingService) {
        this.mailingService = mailingService;
    }

    @Override
    public void sendRequestingUserSignUpNotification(User user) {
        try {
            List<String> addressees = new ArrayList<>();
            addressees.add(user.getEmail());
            String subject = "Confirmación de registro de usuario";
            String message = "Estimad@ Usuari@," + "\n" +
                    "Le damos la bienvenida a nuestra plataforma de control de flujo de solicitudes." + "\n" +
                    "Se registró la cuenta con la siguiente información:" + "\n" +
                    "\tNombre: " + user.fullName() + "\n" +
                    "\tEmail: " + user.getEmail() + "\n" +
                    "\tTeléfono: " + user.getPhone() + "\n" +
                    EMAIL_MESSAGE_FOOTER;
            mailingService.sendEmail(addressees, subject, message);
            System.out.println("Correo enviado correctamente");
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendRestorePasswordNotification(String userEmail, String password) {
        try {
            List<String> addressees = new ArrayList<>();
            addressees.add(userEmail);
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
            List<String> addressees = new ArrayList<>();
            addressees.add(userEmail);
            String subject = "Clave de acceso actualizada";
            String message = "Estimad@ Usuari@," + "\n" +
                    "Su clave de ingreso al sistema se ha actualizado correctamente." + "\n" +
                    EMAIL_MESSAGE_FOOTER;
            mailingService.sendEmail(addressees, subject, message);
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendNotificationToStageAnalysts(List<String> analystAddressees, String requestingFullName,
                                                String processName, String requestCode, String requestFormFullPath) {
        try {
            String subject = "Nueva solicitud registrada: " + requestCode;
            String message = "Estimad@s Analistas," + "\n" +
                    "El usuario: " + requestingFullName + " ha registrado una nueva solicitud para el proceso: " +
                    processName + ", se adjunta formulaio de solicitud, por favor revisar." + "\n" +
                    "Código de la solicitud: " + requestCode + "\n" + EMAIL_MESSAGE_FOOTER;

            File requestFormFile = new File(requestFormFullPath);
            Attachment attachment = createAttachment(requestFormFile);
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(attachment);
            mailingService.sendEmailWithAttachments(analystAddressees, subject, message, attachments);
        } catch (EmailException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendRegisteredNewRequestNotification(String requestingEmail, String processName, String requestCode,
                                                     String requestFormFullPath) {
        try {
            String subject = "Nueva solicitud registrada: " + requestCode;
            String message = "Estimad@ Usuari@," + "\n" +
                    "Se ha registrado exitosamente su nueva solicitud para el proceso: " + processName +
                    ", se adjunta formulaio de solicitud." + "\n" + "Código de la solicitud: " + requestCode + "\n" +
                    EMAIL_MESSAGE_FOOTER;

            List<String> addressees = new ArrayList<>();
            addressees.add(requestingEmail);
            File requestFormFile = new File(requestFormFullPath);
            Attachment attachment = createAttachment(requestFormFile);
            List<Attachment> attachments = new ArrayList<>();
            attachments.add(attachment);
            mailingService.sendEmailWithAttachments(addressees, subject, message, attachments);
        } catch (EmailException | IOException e) {
            e.printStackTrace();
        }
    }

    private Attachment createAttachment(File file) throws IOException {
        String fileName = IoUtility.getFileNameWithoutExtension(file);
        String extension = IoUtility.getFileExtension(file);
        return new Attachment(fileName, extension, Files.readAllBytes(file.toPath()));
    }

    private static final String EMAIL_MESSAGE_FOOTER = "Saludos cordiales." + "\n\n" + "El equipo de J-ADS";

}