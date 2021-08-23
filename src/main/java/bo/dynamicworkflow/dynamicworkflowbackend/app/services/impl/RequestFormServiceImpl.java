package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.input.InputNotFoundException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormGenerationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormSigningException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Input;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Request;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.RequestInputValue;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.enums.InputTypeName;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.InputRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.repositories.RequestInputValueRepository;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestFormService;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.IoUtility;
import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.cert.Certificate;
import java.io.*;

import com.lowagie.text.pdf.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class RequestFormServiceImpl implements RequestFormService {

    private final RequestInputValueRepository requestInputValueRepository;
    private final InputRepository inputRepository;

    @Autowired
    public RequestFormServiceImpl(RequestInputValueRepository requestInputValueRepository,
                                  InputRepository inputRepository) {
        this.requestInputValueRepository = requestInputValueRepository;
        this.inputRepository = inputRepository;
    }

    @Override
    public byte[] generateRequestForm(String processName, Request request, User requesting)
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
            loadRequestingData(context, requesting);
            loadRequestData(context, request);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Options converterOptions = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            report.convert(context, converterOptions, outputStream);
            byte[] data = outputStream.toByteArray();
            outputStream.close();
            return data;
        } catch (Exception e) {
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

    private void loadRequestingData(IContext context, User user) {
        context.put("requestingFullName", user.fullName());
        context.put("requestingEmail", user.getEmail());
        context.put("requestingPhone", user.getPhone());
        context.put("requestingIdentificationNumber", user.getIdentificationNumber());
    }

    private void loadRequestData(IContext context, Request request) throws ParseException, InputNotFoundException {
        context.put("requestCode", request.getCode());
        String shippingTimestamp =
                new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(request.getShippingTimestamp());
        context.put("requestShippingTimestamp", shippingTimestamp);

        List<RequestInputValue> requestInputValues = requestInputValueRepository.getAllByRequestId(request.getId());
        StringBuilder requestResponses = new StringBuilder();
        int index = 1;
        for (RequestInputValue requestInputValue : requestInputValues) {
            Integer inputId = requestInputValue.getInputId();
            Input input = inputRepository.findById(inputId).orElseThrow(() -> new InputNotFoundException(inputId));
            String value = processInputValue(input.getInputType().getName(), requestInputValue.getValue());
            String inputResponse = Integer.toString(index).concat(". ").concat(input.getName())
                    .concat(":\n").concat("Respuesta: ").concat(value).concat("\n");
            requestResponses.append(inputResponse);
            index++;
        }
        context.put("requestResponses", requestResponses.toString());
    }

    private String processInputValue(InputTypeName inputTypeName, String value) throws ParseException {
        switch (inputTypeName) {
            case TEXT:
            case SELECTION_BOX:
            case DEPLOYABLE_LIST:
            case MULTIPLE_CHOICE:
                break;
            case DATE:
                SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                Timestamp timestamp = new Timestamp(inputDateFormat.parse(value).getTime());
                return new SimpleDateFormat("dd-MM-yyyy").format(timestamp);
            case UPLOAD_FILE:
                return  "Archivo enviado";
        }
        return value;
    }

}