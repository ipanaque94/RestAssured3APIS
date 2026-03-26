package APITests.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties props = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null)
                props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar config.properties", e);
        }
    }

    public static String get(String key) {
        // Convierte "books.api.url" → "BOOKS_API_URL" para buscar en env vars
        String envKey = key.toUpperCase().replace(".", "_");
        String envValue = System.getenv(envKey);
        if (envValue != null && !envValue.isBlank())
            return envValue;

        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank())
            return sysProp;

        return props.getProperty(key);
    }
}