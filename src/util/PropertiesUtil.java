package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    
    public static Properties getProperties(String resource) {
        InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(resource);
        
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return prop;
    }
}
