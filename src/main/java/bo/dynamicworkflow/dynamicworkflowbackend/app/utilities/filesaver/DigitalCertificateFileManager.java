package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver;

import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.FileStorageConfig;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DigitalCertificateFileManager extends FileManager {

    @Autowired
    public DigitalCertificateFileManager(FileStorageConfig fileStorageConfig) {
        super(fileStorageConfig);
    }

    public String saveDigitalCertificateFile(Integer departmentMemberId, SaveFileRequest digitalCertificateFile)
            throws DirectoryCreationException, SaveFileException {
        String digitalCertificateRelativePath =
                SEPARATOR.concat(super.fileStorageConfig.getDepartmentMemberCertificatesPath()).concat(SEPARATOR)
                        .concat(departmentMemberId.toString());
        return saveFile(digitalCertificateRelativePath, digitalCertificateFile);
    }

}