package bo.dynamicworkflow.dynamicworkflowbackend.app.services.impl;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormGenerationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Request;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;
import bo.dynamicworkflow.dynamicworkflowbackend.app.services.RequestFormGeneratorService;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

@Service
public class RequestFormGeneratorServiceImpl implements RequestFormGeneratorService {

    @Override
    public byte[] generateRequestForm(String processName, Request request, User user)
            throws RequestFormGenerationException {
        String template = "requests/request-form-template.docx";
        try {
            InputStream templateInputStream =
                    RequestFormGeneratorServiceImpl.class.getClassLoader().getResourceAsStream(template);
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