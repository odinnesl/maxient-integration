package edu.nesl.maxient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {

    private static final Logger logger = LoggerFactory.getLogger(ReadProperties.class);

    public static String read(String property) {
        Properties props = new Properties();
        final String propertiesFile = "config.properties";
        try (final FileInputStream fis = new FileInputStream(propertiesFile)) {
            props.load(fis);
            return props.getProperty(property);
        } catch (IOException e) {
            logger.error("Error reading properties file:{}", e);
        }
        return "";
    }
}

