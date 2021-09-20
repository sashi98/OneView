package oneview.ui.screens.server.context;

import oneview.ui.screens.common.context.OneViewContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerContext implements OneViewContext {

    private HashMap<String, Object> hm;

    public ServerContext() {
        hm = new HashMap<>();
    }

    public void set(String key, Object val) {
        hm.put(key, val);
    }

    public void validate() {
        /**
         * Nothing to be implemented.
         */
    }

    public String getStringValue(String key) {
        String val = String.valueOf(hm.get(key));
        return (val == null || val.equals("null") || val.trim().equals("")) ? "" : val;
    }

    public List<Object> getListValue(String key) {
        List<Object> list = (List<Object>) hm.get(key);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public Object getObject(String key) {
        return hm.get(key);
    }

    @Override
    public void remove(String key) {
        hm.remove(key);
    }

}
