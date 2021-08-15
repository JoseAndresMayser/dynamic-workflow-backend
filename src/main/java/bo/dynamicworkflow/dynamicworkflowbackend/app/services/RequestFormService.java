package bo.dynamicworkflow.dynamicworkflowbackend.app.services;

import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormGenerationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.request.RequestFormSigningException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.Request;
import bo.dynamicworkflow.dynamicworkflowbackend.app.models.User;

public interface RequestFormService {

    byte[] generateRequestForm(String processName, Request request, User user) throws RequestFormGenerationException;

    void signRequestForm(String requestFormPath, String certificatePath, String certificatePassword)
            throws RequestFormSigningException;

}