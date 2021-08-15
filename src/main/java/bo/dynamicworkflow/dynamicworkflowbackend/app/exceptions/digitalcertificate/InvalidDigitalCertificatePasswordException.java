package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate;

public class InvalidDigitalCertificatePasswordException extends DigitalCertificateException {

    public InvalidDigitalCertificatePasswordException() {
        this("Contraseña del certificado digital inválida.");
    }

    public InvalidDigitalCertificatePasswordException(String message) {
        super(message);
    }

}