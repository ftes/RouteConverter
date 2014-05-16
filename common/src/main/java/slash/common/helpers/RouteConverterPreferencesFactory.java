package slash.common.helpers;

import java.util.prefs.Preferences;

public class RouteConverterPreferencesFactory {
    private static final Preferences user = new InMemoryPreferences(null, "");
    
    public static Preferences userNodeForPackage(Class<?> clazz) {
        return user.node(clazz.getName());
    }
}
