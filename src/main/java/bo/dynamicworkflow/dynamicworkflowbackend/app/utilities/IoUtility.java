package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IoUtility {

    public static void saveFile(byte[] fileBytes, String path) throws IOException {
        try (OutputStream os = new FileOutputStream(path)) {
            os.write(fileBytes);
        }
    }

    public static String getFileExtension(File file) {
        String extension = null;
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
            }
        } catch (Exception e) {
            // ignored
        }
        return extension;
    }

    public static String getFileNameWithoutExtension(File file) {
        String extension = null;
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(0, name.lastIndexOf("."));
            }
        } catch (Exception e) {
            // ignored
        }
        return extension;
    }

}