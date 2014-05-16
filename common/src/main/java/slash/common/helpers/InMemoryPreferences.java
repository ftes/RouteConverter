package slash.common.helpers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

public class InMemoryPreferences extends AbstractPreferences {
    protected InMemoryPreferences(AbstractPreferences parent, String name) {
        super(parent, name);
    }

    private final Map<String, String> data = new HashMap<String, String>();
    private final Set<InMemoryPreferences> children = new HashSet<InMemoryPreferences>();

    @Override
    protected void putSpi(String key, String value) {
        data.put(key, value);
    }

    @Override
    protected String getSpi(String key) {
        return data.get(key);
    }

    @Override
    protected void removeSpi(String key) {
        data.remove(key);
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        for (InMemoryPreferences child : children) {
            child.removeNodeSpi();
        }
        children.clear();
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        return data.keySet().toArray(new String[0]);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        return new String[0];
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        InMemoryPreferences child = new InMemoryPreferences(this, name);
        children.add(child);
        return child;
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        
    }
}