package config;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {
	private Set<String> getAllMessageKeys(String keyPrefix, Locale locale) {
        Set<String> keys = new LinkedHashSet<>();
        
        if (log.isDebugEnabled()) {
            log.debug("getAllMessageKeys: keyPrefix[{}], locale[{}]", keyPrefix, locale);
        }
        
        PropertiesHolder propHolder = this.getMergedProperties(locale);
        Properties prop = propHolder.getProperties();
        for (String key : prop.stringPropertyNames()) {
            if (key.startsWith(keyPrefix)) {
                keys.add(key);
            }
        }
        
        return keys;
    }
	
    private Map<String, String> createCache(String keyPrefix, Locale locale) {
        Map<String, String> messageMap = new HashMap<>();
        Set<String> keys = getAllMessageKeys(keyPrefix, locale);
        
        for(String key : keys) {
            String message = getMessage(key, null, locale);
            messageMap.put(key, message);
        }
        
        return messageMap;
    }
    
    public Map<String, String> getAllMessages(String keyPrefix, Locale locale) {
        return createCache(keyPrefix, locale);
    }
}
