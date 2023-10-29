package Objects;

import java.util.HashMap;
import java.util.Map;

public class CustomObject implements UObject {
    private final Map<String,Object> properties;

    public CustomObject() {
        this.properties = new HashMap<>();
    }

    @Override
    public Object getProperty(String key) {
        if (properties.containsKey(key))
            return properties.get(key);
        throw new RuntimeException(key + " is not set");
    }

    @Override
    public void setProperty(String key, Object newValue) {
        properties.merge(key,newValue,(o, o2) -> o2);
    }
}
