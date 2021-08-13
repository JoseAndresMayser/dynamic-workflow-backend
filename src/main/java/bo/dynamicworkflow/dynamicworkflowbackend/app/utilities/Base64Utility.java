package bo.dynamicworkflow.dynamicworkflowbackend.app.utilities;

import java.util.Base64;

public class Base64Utility {

    /**
     * Obtiene la representación base 64 de un {@link String} (lo codifica)
     * @param stringSrc {@link String} a codificar
     * @return {@link String} codificado en base 64
     */
    public static String encodeAsString(String stringSrc) {
        return encodeAsString(stringSrc.getBytes());
    }

    /**
     * Obtiene la representación base 64 de un arreglo de bytes (lo codifica)
     * @param bytesSrc Arreglo de bytes a codificar
     * @return {@link String} codificado en base 64
     */
    public static String encodeAsString(byte[] bytesSrc) {
        byte[] encoded = encodeAsByteArray(bytesSrc);
        return new String(encoded);
    }

    /**
     * Obtiene la representación base 64 de un {@link String} (lo codifica)
     * @param stringSrc {@link String} a codificar
     * @return Arreglo de bytes codificado en base 64
     */
    public static byte[] encodeAsByteArray(String stringSrc) {
        return encodeAsByteArray(stringSrc.getBytes());
    }

    /**
     * Obtiene la representación base 64 de un arreglo de bytes (lo codifica)
     * @param bytesSrc Arreglo de bytes a codificar
     * @return Arreglo de bytes codificado en base 64
     */
    public static byte[] encodeAsByteArray(byte[] bytesSrc) {
        return Base64.getEncoder().encode(bytesSrc);
    }

    /**
     * Decodifica un arreglo de bytes en base 64
     * @param base64Bytes
     * @return Arreglo de bytes decodificado
     */
    public static byte[] decodeAsByteArray(byte[] base64Bytes) {
        return Base64.getDecoder().decode(base64Bytes);
    }

    /**
     * Decodifica un {@link String} en base 64
     * @param base64String Arreglo de bytes decodificado
     * @return Arreglo de bytes decodificado
     */
    public static byte[] decodeAsByteArray(String base64String) {
        return decodeAsByteArray(base64String.getBytes());
    }

    /**
     * Decodifica un {@link String} en base 64
     * @param base64String
     * @return {@link String} decodificado
     */
    public static String decodeAsString(String base64String) {
        byte[] decoded = decodeAsByteArray(base64String);
        return new String(decoded);
    }

    /**
     * Decodifica un arreglo de bytes en base 64
     * @param base64Bytes
     * @return {@link String} decodificado
     */
    public static String decodeAsString(byte[] base64Bytes) {
        return decodeAsString(new String(base64Bytes));
    }

}