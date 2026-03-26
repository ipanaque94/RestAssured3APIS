package APITests.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static Properties props = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar configuración local", e);
        }
    }

    public static String get(String key) {
        // 1. Primero intenta leer de system properties (CI/CD)
        String value = System.getProperty(key);
        if (value != null && !value.isEmpty()) {
            return value;
        }
        // 2. Si no existe, usa config.properties (local)
        return props.getProperty(key);
    }
}