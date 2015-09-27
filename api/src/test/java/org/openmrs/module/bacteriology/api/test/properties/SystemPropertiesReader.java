package org.openmrs.module.bacteriology.api.test.properties;

public class SystemPropertiesReader implements PropertiesReader {
    @Override
    public String getProperty(String key) {
        return System.getProperty(key);
    }
}

