package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;

public class IoUtility {

    /**
     * Serializa un objeto a un arreglo de bytes
     *
     * @param object Objeto a serializar
     * @return Arreglo de bytes del objeto serializado
     * @throws IOException
     */
    public static byte[] objectToByteArray(Object object) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ObjectOutputStream os = new ObjectOutputStream(baos);
            os.writeObject(object);
            return baos.toByteArray();
        }
    }

    /**
     * Desserializa un objeto a partir de un arreglo de bytes
     *
     * @param byteArray Arreglo de bytes del objeto serializado
     * @return Objeto ({@link Object}) desserializado
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Object byteArrayToObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(byteArray)) {
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }
    }

    /**
     * Lee los bytes de un objeto {@code inputStream}
     *
     * @param inputStream {@code InputStream} del que se quiere leer los bytes
     * @return Arreglo de bytes correspondientes al stream
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192];
            int readBytes;
            while ((readBytes = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, readBytes);
            }
            return outputStream.toByteArray();
        }
    }

    /**
     * Obtiene un objeto {@code InputStream} que representa un recurso, a partir de una
     * {@code URL} representada por el {@code String} {@code path}
     *
     * @param path Lugar desde el cual se obtendrá el recurso
     * @return El objeto {@code InputStream} obtenido
     * @throws IOException
     */
    public static InputStream getInputStream(String path) throws IOException {
        return getInputStream(new URL(path));
    }

    /**
     * Obtiene un objeto {@code InputStream} que representa un recurso, a partir de
     * una {@code URI}
     *
     * @param uri Lugar desde el cual se obtendrá el recurso
     * @return El objeto {@code InputStream} obtenido
     * @throws IOException
     */
    public static InputStream getInputStream(URI uri) throws IOException {
        return getInputStream(uri.toURL());
    }

    /**
     * Obtiene un objeto {@code InputStream} que representa un recurso, a partir de
     * una {@code URL}
     *
     * @param url Lugar desde el cual se obtendrá el recurso
     * @return El objeto {@code InputStream} obtenido
     * @throws IOException
     */
    public static InputStream getInputStream(URL url) throws IOException {
        return url.openStream();
    }

    /**
     * Lee los bytes de un archivo que se encuentra almacenado en disco (de
     * manra local)
     *
     * @param path Dirección de la cual se desea leer los bytes
     * @return Arreglo de bytes correspondientes al archivo leido
     * @throws IOException
     */
    public static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(new File(path).toPath());
    }

    /**
     * Guarda un arreglo de bytes en disco (almacenamiento local), almacenándolos en
     * la dirección proporcionada
     *
     * @param fileBytes Bytes a guardar en disco
     * @param path      Dirección en la cual se desea almacenar los bytes
     * @throws IOException
     */
    public static void saveFile(byte[] fileBytes, String path) throws IOException {
        try (OutputStream os = new FileOutputStream(path)) {
            os.write(fileBytes);
        }
    }

    /**
     * Obtiene la extensión de un archivo a partir del nombre del mismo
     *
     * @param file {@code FileInfo} que corresponde al archivo a procesar
     * @return El {@code String} que corresponde a la extensión del archivo
     */
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

    /**
     * Obtiene el nombre de un archivo sin la extensión.
     *
     * @param file Archivo que corresponde al archivo a procesar
     * @return El {@code String} que corresponde al nombre del archivo sin la extensión
     */
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