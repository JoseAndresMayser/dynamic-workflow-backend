package bo.dynamicworkflow.dynamicworkflowbackend.app.notification.mailing;

import org.apache.commons.mail.EmailException;

import java.util.List;

public interface MailingService {

    void sendEmail(List<String> addressees, String subject, String message) throws EmailException;

    void sendEmail(List<String> addressees, List<String> addresseesWithCopy, String subject, String message)
            throws EmailException;

    void sendEmail(List<String> addressees, List<String> addresseesWithCopy, List<String> blindCopyAddressees,
                   String subject, String message) throws EmailException;

    void sendEmailWithAttachments(List<String> addressees, String subject, String message, List<Attachment> attachments)
            throws EmailException;

    void sendEmailWithAttachments(List<String> addressees, List<String> addresseesWithCopy, String subject,
                                  String message, List<Attachment> attachments) throws EmailException;

    void sendEmailWithAttachments(List<String> addressees, List<String> addresseesWithCopy,
                                  List<String> blindCopyAddressees, String subject, String message,
                                  List<Attachment> attachments) throws EmailException;

}