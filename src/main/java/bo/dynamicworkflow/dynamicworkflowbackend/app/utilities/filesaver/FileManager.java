package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.filesaver;

import bo.dynamicworkflow.dynamicworkflowbackend.app.configs.FileStorageConfig;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.DirectoryCreationException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.exceptions.SaveFileException;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.Base64Utility;
import bo.dynamicworkflow.dynamicworkflowbackend.app.utilities.IoUtility;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FileManager {

    protected final FileStorageConfig fileStorageConfig;

    protected static final String SEPARATOR = File.separator;

    public FileManager(FileStorageConfig fileStorageConfig) {
        this.fileStorageConfig = fileStorageConfig;
    }

    protected String getPathWithEntityTimestamp(Timestamp entityTimestamp, Integer entityId) {
        Date date = new Date(entityTimestamp.getTime());
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String year = String.valueOf(localDate.getYear());
        String month = String.valueOf(localDate.getMonthValue());
        String day = String.valueOf(localDate.getDayOfMonth());
        return SEPARATOR.concat(year).concat(SEPARATOR).concat(month).concat(SEPARATOR)
                .concat(day).concat(SEPARATOR).concat(entityId.toString());
    }

    protected String saveFile(String relativePath, SaveFileRequest file) throws DirectoryCreationException,
            SaveFileException {
        String absoluteDirectoryPath = getAbsolutePath(relativePath);
        File directory = new File(absoluteDirectoryPath);
        if (!directory.exists() && !directory.mkdirs()) throw new DirectoryCreationException();

        byte[] fileBytes = Base64Utility.decodeAsByteArray(file.getFileContent());
        String fileLabel = file.getLabel();
        String fileName = fileLabel.concat(".").concat(file.getExtension());
        String absoluteFilePath = absoluteDirectoryPath.concat(SEPARATOR).concat(fileName);
        try {
            IoUtility.saveFile(fileBytes, absoluteFilePath);
        } catch (IOException e) {
            throw new SaveFileException("Ocurrió un error al intentar guardar el archivo", e);
        }
        return relativePath.concat(SEPARATOR).concat(fileName);
    }

    public String getAbsolutePath(String relativePath) {
        return fileStorageConfig.getBasePath().concat(relativePath);
    }

}