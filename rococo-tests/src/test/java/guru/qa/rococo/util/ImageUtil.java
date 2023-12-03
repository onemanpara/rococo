package guru.qa.rococo.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Objects;

public class ImageUtil {

    public static String convertImageToBase64(String filePath) {
        byte[] fileContent = readImageAsByteArray(filePath);
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(fileContent);
    }

    public static String convertImageToBase64(byte[] image) {
        return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(image);
    }

    private static byte[] readImageAsByteArray(String filePath) {
        try (InputStream inputStream = Objects.requireNonNull(ImageUtil.class.getClassLoader().getResourceAsStream(filePath))) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
