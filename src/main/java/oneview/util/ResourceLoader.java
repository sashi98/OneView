package oneview.util;

import java.net.URL;

public class ResourceLoader {

    private ResourceLoader(){
    }

    public static URL getResource(String resource){
        return ResourceLoader.class.getClass().getResource(resource);
    }


}
