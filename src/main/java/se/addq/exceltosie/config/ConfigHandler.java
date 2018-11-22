package se.addq.exceltosie.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

public class ConfigHandler {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Properties properties = new Properties();

    private static final String CONFIG_PROPERTIES_FILE_NAME = "config.properties";

    static {
        String configFile = CONFIG_PROPERTIES_FILE_NAME;
        try (InputStream inputStream = ConfigHandler.class.getClassLoader().getResourceAsStream(configFile)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Could not find " + configFile);
            }
        } catch (IOException e) {
            LOG.error("Could not read file {}", e);
        }
    }

    static String getValue(String propertyKey) {
        String value = properties.getProperty(propertyKey);
        if (value == null) {
            return "";
        }
        return value;
    }

    public static Properties getProperties() {
        return properties;
    }


}
