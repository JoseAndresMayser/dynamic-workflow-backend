package bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.digitalcertificate;

public class InvalidCertificateFileException extends DigitalCertificateException {

    public InvalidCertificateFileException() {
        this("El archivo de Certificado Digital es inválido.");
    }

    public InvalidCertificateFileException(String message) {
        super(message);
    }

}