package oneview.ui.screens.common.context;

import java.io.Serializable;
import java.util.List;

public interface OneViewContext extends Serializable {

    String getStringValue(String key);
    void validate();
    List getListValue(String key);
    Object getObject(String key);
    void remove(String key);

}
