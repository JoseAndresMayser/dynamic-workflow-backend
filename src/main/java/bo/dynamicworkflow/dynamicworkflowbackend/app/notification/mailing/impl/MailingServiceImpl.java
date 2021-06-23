package bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.MailConfig;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing.Attachment;
import bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing.MailingService;
import lombok.AllArgsConstructor;
import org.apache.commons.mail.*;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class MailingServiceImpl implements MailingService {

    @Override
    public void sendEmail(List<String> addressees, String subject, String message) throws EmailException {
        sendAnyEmail(addressees, new ArrayList<>(), new ArrayList<>(), subject, message, new ArrayList<>(),
                EmailType.SIMPLE);
    }

    @Override
    public void sendEmail(List<String> addressees, List<String> addresseesWithCopy, String subject, String message)
            throws EmailException {
        sendAnyEmail(addressees, addresseesWithCopy, new ArrayList<>(), subject, message, new ArrayList<>(),
                EmailType.SIMPLE);
    }

    @Override
    public void sendEmail(List<String> addressees, List<String> addresseesWithCopy, List<String> blindCopyAddressees,
                          String subject, String message) throws EmailException {
        sendAnyEmail(addressees, addresseesWithCopy, blindCopyAddressees, subject, message, new ArrayList<>(),
                EmailType.SIMPLE);
    }

    @Override
    public void sendEmailWithAttachments(List<String> addressees, String subject, String message,
                                         List<Attachment> attachments) throws EmailException {
        sendAnyEmail(addressees, new ArrayList<>(), new ArrayList<>(), subject, message, attachments,
                EmailType.WITH_ATTACHMENTS);
    }

    @Override
    public void sendEmailWithAttachments(List<String> addressees, List<String> addresseesWithCopy, String subject,
                                         String message, List<Attachment> attachments) throws EmailException {
        sendAnyEmail(addressees, addresseesWithCopy, new ArrayList<>(), subject, message, attachments,
                EmailType.WITH_ATTACHMENTS);
    }

    @Override
    public void sendEmailWithAttachments(List<String> addressees, List<String> addresseesWithCopy,
                                         List<String> blindCopyAddressees, String subject, String message,
                                         List<Attachment> attachments) throws EmailException {
        sendAnyEmail(addressees, addresseesWithCopy, blindCopyAddressees, subject, message, attachments,
                EmailType.WITH_ATTACHMENTS);
    }

    private void sendAnyEmail(List<String> addressees, List<String> addresseesWithCopy,
                              List<String> blindCopyAddressees, String subject, String message,
                              List<Attachment> attachments, EmailType emailType) throws EmailException {
        Email email = getEmail(emailType);
        setUpEmailConfiguration(email);
        addAddressees(email, addressees);
        addAddresseesWithCopy(email, addresseesWithCopy);
        addBlindCopyAddressees(email, blindCopyAddressees);
        email.setSubject(subject);
        addMessageBody(email, message, attachments, emailType);
        new EmailSender(email).start();
    }

    private Email getEmail(EmailType emailType) {
        Email email = null;
        switch (emailType) {
            case SIMPLE:
                email = new SimpleEmail();
                break;
            case WITH_ATTACHMENTS:
                email = new MultiPartEmail();
                break;
        }
        return email;
    }

    private void setUpEmailConfiguration(Email email) throws EmailException {
        email.setHostName(MailConfig.MAIL_SMTP_SERVER);
        email.setSmtpPort(MailConfig.MAIL_SMTP_PORT);
        email.setSslSmtpPort(MailConfig.MAIL_SMTP_PORT_SSL);
        email.setAuthenticator(new DefaultAuthenticator(MailConfig.MAIL_SMTP_USERNAME, MailConfig.MAIL_SMTP_PASSWORD));
        email.setSSLOnConnect(MailConfig.MAIL_SMTP_SSL);
        email.setFrom(MailConfig.MAIL_SMTP_SENDER, MailConfig.MAIL_SMTP_SENDER_NAME);
    }

    private void addAddressees(Email email, List<String> addressees) throws EmailException {
        for (String addressee : addressees) email.addTo(addressee);
    }

    private void addAddresseesWithCopy(Email email, List<String> addresseesWithCopy) throws EmailException {
        for (String addresseeWithCopy : addresseesWithCopy) email.addCc(addresseeWithCopy);
    }

    private void addBlindCopyAddressees(Email email, List<String> blindCopyAddressees) throws EmailException {
        for (String blindCopyAddressee : blindCopyAddressees) email.addBcc(blindCopyAddressee);
    }

    private void addMessageBody(Email email, String message, List<Attachment> attachments, EmailType emailType)
            throws EmailException {
        switch (emailType) {
            case SIMPLE:
                email.setMsg(message);
                break;
            case WITH_ATTACHMENTS:
                addMessageBodyWithAttachments(email, message, attachments);
                break;
        }
    }

    private void addMessageBodyWithAttachments(Email email, String message, List<Attachment> attachments) {
        try {
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(message);

            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart(mimeBodyPart);

            for (Attachment attachment : attachments) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                String filename = attachment.getName() + "." + attachment.getExtension();
                javax.mail.util.ByteArrayDataSource dataSource = new ByteArrayDataSource(
                        attachment.getContent(),
                        "application/" + attachment.getExtension()
                );
                attachmentPart.setDataHandler(new DataHandler(dataSource));
                attachmentPart.setFileName(filename);
                mimeMultipart.addBodyPart(attachmentPart);
            }

            email.setContent(mimeMultipart);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private enum EmailType {

        SIMPLE,
        WITH_ATTACHMENTS

    }

    @AllArgsConstructor
    private static class EmailSender extends Thread {

        private final Email email;

        @Override
        public void run() {
            try {
                email.send();
                System.out.println("Correo enviado correctamente...");
            } catch (EmailException e) {
                e.printStackTrace();
            }
        }

    }

}