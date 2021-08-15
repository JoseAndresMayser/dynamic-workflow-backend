package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormGenerationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormSigningException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Request;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestFormService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.IoUtility;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.Certificate;
import java.io.*;

import com.lowagie.text.pdf.*;

import java.text.SimpleDateFormat;

@Service
public class RequestFormServiceImpl implements RequestFormService {

    @Override
    public byte[] generateRequestForm(String processName, Request request, User user)
            throws RequestFormGenerationException {
        String template = "requests/request-form-template.docx";
        try {
            InputStream templateInputStream =
                    RequestFormServiceImpl.class.getClassLoader().getResourceAsStream(template);
            if (templateInputStream == null)
                throw new RequestFormGenerationException(
                        "No se pudo encontrar la plantilla para generar el formulario de solicitud."
                );
            IXDocReport report =
                    XDocReportRegistry.getRegistry().loadReport(templateInputStream, TemplateEngineKind.Velocity);
            templateInputStream.close();

            IContext context = report.createContext();
            loadProcessName(context, processName);
            loadRequestData(context, request);
            loadUserData(context, user);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Options converterOptions = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            report.convert(context, converterOptions, outputStream);
            byte[] data = outputStream.toByteArray();
            outputStream.close();
            return data;
        } catch (IOException | XDocReportException e) {
            throw new RequestFormGenerationException("Ocurrió un error al generar el formulario de solicitud.", e);
        }
    }

    @Override
    public void signRequestForm(String certificatePath, String certificatePassword, String requestFormPath)
            throws RequestFormSigningException {
        try {
            KeyStore keyStore = KeyStore.getInstance("pkcs12");
            keyStore.load(new FileInputStream(certificatePath), certificatePassword.toCharArray());
            String alias = keyStore.aliases().nextElement();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, certificatePassword.toCharArray());
            Certificate[] chain = keyStore.getCertificateChain(alias);
            // Recibimos como parámetro de entrada el nombre del archivo PDF a firmar
            PdfReader reader = new PdfReader(IoUtility.readFile(requestFormPath));
            FileOutputStream fileOutputStream = new FileOutputStream(requestFormPath);

            // Añadimos firma al documento PDF
            PdfStamper pdfStamper = PdfStamper.createSignature(reader, fileOutputStream, '?');
            PdfSignatureAppearance pdfSignatureAppearance = pdfStamper.getSignatureAppearance();
            pdfSignatureAppearance.setCrypto(privateKey, chain, null,
                    PdfSignatureAppearance.WINCER_SIGNED);
            pdfSignatureAppearance.setReason("Firma Aprobación de Solicitud");
            pdfSignatureAppearance.setLocation("Santa Cruz de la Sierra");
            // Añade la firma visible. Podemos comentarla para que no sea visible.
            //pdfSignatureAppearance.setVisibleSignature(new Rectangle(100, 100, 200, 200), 1, null);
            pdfStamper.close();
        } catch (Exception e) {
            throw new RequestFormSigningException("Ocurrió un error al intentar firmar el formulario de solicitud", e);
        }
    }

    private void loadProcessName(IContext context, String processName) {
        context.put("processName", processName);
    }

    private void loadRequestData(IContext context, Request request) {
        String shippingTimestamp =
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(request.getShippingTimestamp());
        context.put("requestShippingTimestamp", shippingTimestamp);
        context.put("requestCode", request.getCode());
    }

    private void loadUserData(IContext context, User user) {
        context.put("requestingFullName", user.fullName());
        context.put("requestingEmail", user.getEmail());
        context.put("requestingPhone", user.getPhone());
        context.put("requestingIdentificationNumber", user.getIdentificationNumber());
    }

}