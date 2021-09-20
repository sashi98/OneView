package oneview.ui.screens.deploy.context;

import oneview.ui.screens.common.context.OneViewContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DeploymentContext implements OneViewContext {

    private HashMap<String, Object> hm;

    public DeploymentContext(){
        hm = new HashMap<>();
    }

    public void set(String key, Object val){
        hm.put(key,val);
    }

    public void validate() {
        /*
        nothing
         */
    }

    public String getStringValue(String key) {
        String val = String.valueOf(hm.get(key));
        return (val == null || val.equals("null") || val.trim().equals(""))?"":val;
    }

    public List<Object> getListValue(String key){
        Object o = hm.get(key);
        if (o != null){
            return (List) o;
        }
        return new ArrayList();
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
