package ua.com.foxminded.university.cache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertiesCacheImpl implements PropertiesCache {
    
    private Properties property = new Properties();
    
    public PropertiesCacheImpl(String fileName) {
        try (InputStream input = this.getClass().getClassLoader()
                                                .getResourceAsStream(fileName)) {
            property.load(input);
        } catch (IOException e) {
            log.error("The external resource was not loaded", e);
        }
    }
    
    @Override
    public String getProperty(String key) {
        return property.getProperty(key);
    }
}
