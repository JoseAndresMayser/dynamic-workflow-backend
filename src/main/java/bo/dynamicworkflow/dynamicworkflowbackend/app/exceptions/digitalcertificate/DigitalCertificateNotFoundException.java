package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate;

public class DigitalCertificateNotFoundException extends DigitalCertificateException {

    public DigitalCertificateNotFoundException() {
        this("Certificado Digital no encontrado.");
    }

    public DigitalCertificateNotFoundException(String message) {
        super(message);
    }

}