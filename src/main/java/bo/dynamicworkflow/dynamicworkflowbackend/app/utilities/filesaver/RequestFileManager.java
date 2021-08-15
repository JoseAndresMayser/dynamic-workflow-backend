package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver;

import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.FileStorageConfig;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class RequestFileManager extends FileManager {

    @Autowired
    public RequestFileManager(FileStorageConfig fileStorageConfig) {
        super(fileStorageConfig);
    }

    public String saveRequestFile(Timestamp shippingTimestamp, Integer requestId, SaveFileRequest requestFile)
            throws DirectoryCreationException, SaveFileException {
        String requestRelativePath = getRequestRelativePath(shippingTimestamp, requestId);
        return saveFile(requestRelativePath, requestFile);
    }

    public String getRequestRelativePath(Timestamp requestShippingTimestamp, Integer requestId) {
        String requestsPath = SEPARATOR.concat(super.fileStorageConfig.getRequestsPath());
        String requestsTimestampPath = getPathWithEntityTimestamp(requestShippingTimestamp, requestId);
        return requestsPath.concat(requestsTimestampPath);
    }

}